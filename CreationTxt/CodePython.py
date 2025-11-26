!pip install shapely pyproj geojson tqdm
#==============================================================================A METTRE SUR GOOGLE COLAB DANS 2 CELLULES DIFFERENTES
# Coller ceci dans la 2ème cellule (après installation des paquets)
from shapely.geometry import shape, Point, LineString, MultiLineString
from shapely.ops import nearest_points
from collections import Counter, defaultdict
import json
from geopy.distance import geodesic
import math
import string

# ---- paramètres ----
MAX_DIST_NORMAL = 5
MAX_DIST_IMPASSE = 2
# tolérance pour considérer un point sur une ligne (en degrés cartésiens Shapely) :
ON_LINE_TOL = 1e-8

# charger geojson
with open("montbrunlesbains.geojson", encoding="utf-8") as f:
    data = json.load(f)

streets = [f for f in data['features'] if f['properties'].get('highway')]
buildings = [f for f in data['features'] if f['properties'].get('building')]

houses_per_street = Counter()
street_lengths = {}
street_geoms = {}
street_house_coords = defaultdict(list)

# util: convertir géom multi -> liste de LineString pour traitement local
def lines_from_geom(geom):
    if geom.geom_type == 'LineString':
        return [geom]
    elif geom.geom_type == 'MultiLineString':
        return list(geom.geoms)
    else:
        return []

# calculer longueur et stocker geometries (comme LineString ou MultiLineString)
for s in streets:
    name = s['properties'].get('name')
    if not name:
        continue

    geom = shape(s['geometry'])
    # longueur calculée en sommant les segments géodésiques
    total_length = 0.0
    for line in lines_from_geom(geom):
        coords = list(line.coords)
        for i in range(len(coords)-1):
            # geopy uses (lat, lon)
            total_length += geodesic((coords[i][1], coords[i][0]), (coords[i+1][1], coords[i+1][0])).meters

    street_lengths[name] = round(total_length, 3)
    street_geoms[name] = geom

# associer bâtiments aux rues (par addr:street si présent sinon rue la plus proche)
for b in buildings:
    props = b.get('properties', {})
    street_name = props.get('addr:street')
    housenumber = props.get('addr:housenumber')

    b_geom = shape(b['geometry'])
    if street_name in street_lengths and housenumber:
        houses_per_street[street_name] += 1
        street_house_coords[street_name].append(b_geom)
    else:
        # trouver rue la plus proche géométriquement
        min_dist = float('inf')
        closest_street = None
        for name, geom in street_geoms.items():
            dist = b_geom.distance(geom)
            if dist < min_dist:
                min_dist = dist
                closest_street = name
        if closest_street is None:
            continue
        houses_per_street[closest_street] += 1
        street_house_coords[closest_street].append(b_geom)

# -----------------------------
# Construire l'ensemble des points d'intersection (sommets)
# -----------------------------
raw_intersection_points = []  # list of shapely Points (exact intersection coords)

street_names = list(street_geoms.keys())
for i in range(len(street_names)):
    s1 = street_names[i]
    g1 = street_geoms[s1]
    for j in range(i+1, len(street_names)):
        s2 = street_names[j]
        g2 = street_geoms[s2]
        if g1.intersects(g2):
            inter = g1.intersection(g2)
            # inter peut être Point, MultiPoint, LineString (chevauchement), GeometryCollection...
            if inter.is_empty:
                continue
            if inter.geom_type == 'Point':
                raw_intersection_points.append((inter.x, inter.y, [s1, s2]))
            elif inter.geom_type == 'MultiPoint':
                for p in inter.geoms:
                    raw_intersection_points.append((p.x, p.y, [s1, s2]))
            else:
                # gérer chevauchement (LineString intersection) en échantillonnant ses endpoints
                if hasattr(inter, 'geoms'):
                    parts = list(inter.geoms)
                else:
                    parts = [inter]
                for part in parts:
                    if part.geom_type == 'Point':
                        raw_intersection_points.append((part.x, part.y, [s1, s2]))
                    elif part.geom_type in ('LineString', 'LinearRing'):
                        # prendre les sommets de la portion commune comme intersections potentielles
                        coords = list(part.coords)
                        if len(coords) >= 1:
                            raw_intersection_points.append((coords[0][0], coords[0][1], [s1, s2]))
                            raw_intersection_points.append((coords[-1][0], coords[-1][1], [s1, s2]))
                        else:
                            # fallback: centroid
                            c = part.centroid
                            raw_intersection_points.append((c.x, c.y, [s1, s2]))
                    else:
                        # fallback: centroid
                        c = part.centroid
                        raw_intersection_points.append((c.x, c.y, [s1, s2]))

# Dédupliquer et regrouper les points proches (en arrondissant les coordonnées)
# Ajuster le nombre de décimales selon la précision attendue (6 décimales ~ 0.11 m à l'équateur)
DECIMALS = 6
pt_dict = {}  # key: (rx,ry) -> {'coord':(x,y), 'streets': set()}
for x, y, s_list in raw_intersection_points:
    key = (round(x, DECIMALS), round(y, DECIMALS))
    if key not in pt_dict:
        pt_dict[key] = {'coord': (x, y), 'streets': set()}
    pt_dict[key]['streets'].update(s_list)

# transformer en liste et assigner noms A, B, C...
vertices = {}  # 'A': {'coord': (x,y), 'streets': [..]}
vertex_by_coord = {}  # (x_rounded,y_rounded) -> 'A'
letters = list(string.ascii_uppercase)
# si plus de 26 sommets, on étendra la nomenclature A, B, ..., Z, AA, AB, ...
def vertex_name_from_index(idx):
    # base 26 like Excel columns
    name = ""
    i = idx + 1
    while i > 0:
        i, rem = divmod(i-1, 26)
        name = chr(65 + rem) + name
    return name

pt_items = list(pt_dict.items())
for idx, (key, val) in enumerate(pt_items):
    vname = vertex_name_from_index(idx)
    vertices[vname] = {'coord': val['coord'], 'streets': sorted(list(val['streets']))}
    vertex_by_coord[key] = vname

# -----------------------------
# Pour chaque rue, segmenter en arcs entre sommets (points d'intersection présents sur cette rue)
# -----------------------------
def is_point_on_line(point, line):
    # tiny tolerance check
    return line.distance(point) <= ON_LINE_TOL

def geodetic_length_of_linestring(linestring):
    coords = list(linestring.coords)
    total = 0.0
    for i in range(len(coords)-1):
        total += geodesic((coords[i][1], coords[i][0]), (coords[i+1][1], coords[i+1][0])).meters
    return total

arcs = []  # list of dicts: {'u': 'A', 'v': 'B', 'street': name, 'n_houses': int, 'length_m': float, 'coords': [(x,y),(x,y)]}

# Préparer une liste de Points pour tester appartenance aux lignes plus rapidement
intersection_points = []
for key, vname in vertex_by_coord.items():
    x_rounded, y_rounded = key
    x, y = pt_dict[key]['coord'] if key in pt_dict else (x_rounded, y_rounded)
    intersection_points.append((Point(x, y), key, vname))

# Pour chaque street, pour chaque LineString composant, récupérer les intersections dessus et découper
for street_name, geom in street_geoms.items():
    lines = lines_from_geom(geom)
    for line in lines:
        # trouver tous les intersection_points qui sont sur cette line
        pts_on_line = []
        for p, key, vname in intersection_points:
            # on cherche les points d'intersection dont les rues incluent cette rue
            # mais on permet aussi la proximité si l'intersection a été créée en recoupement d'autres rues
            # vérifier si la géométrie 'line' contient (ou est très proche) du point
            if is_point_on_line(p, line):
                # assurer que ce vertex est lié à la rue (ou accepter quand même)
                # récupérer la version arrondie du key (déjà key)
                # pour éviter points hors segment, utiliser project + within [0, length]
                proj = line.project(p)
                if proj >= -1e-9 and proj <= line.length + 1e-9:
                    pts_on_line.append((proj, p, key, vname))
        # ajouter les extrémités de la ligne comme potentiels sommets (mais seulement si elles correspondent à un vertex)
        # on préfère découper strictement entre intersections ; si pas d'intersections -> pas d'arc défini entre sommets
        if len(pts_on_line) < 2:
            # pas d'arc délimité par deux sommets sur ce portion
            continue

        # trier par position le long de la ligne
        pts_on_line.sort(key=lambda x: x[0])

        # créer segments entre points successifs
        for k in range(len(pts_on_line)-1):
            proj_a, pa, key_a, v_a = pts_on_line[k]
            proj_b, pb, key_b, v_b = pts_on_line[k+1]
            if v_a == v_b:
                # sauter segments nuls
                continue

            # extraire la portion de ligne entre ces deux projections
            # shapely n'a pas slice direct; on reconstruira via coords et coupure par distance param
            # méthode : récupérer les points de coord du linestring, et couper entre indices
            coords = list(line.coords)
            # trouver indices des coordonnées brutes entourant proj_a et proj_b
            # pour simplicité, échantillonner une petite ligne entre pa et pb :
            segment = LineString([ (pa.x, pa.y), (pb.x, pb.y) ])
            # Mais ce segment est une droite entre les deux points — si la voie est courbe, on perd la courbure.
            # Alternative pratique : interpoler points le long de 'line' en découpant par projection.
            # Construire segment via interpolation
            try:
                sub_coords = []
                # inclure l'exact coord a
                a_pt = line.interpolate(proj_a)
                b_pt = line.interpolate(proj_b)
                sub_coords = [ (a_pt.x, a_pt.y) ]
                # extraire coords internes strictement entre proj_a et proj_b
                accumulated = 0.0
                for i in range(len(coords)-1):
                    p0 = coords[i]
                    p1 = coords[i+1]
                    # compute projection along line up to this vertex to decide inclusion
                    seg = LineString([p0, p1])
                    start_proj = line.project(Point(p0))
                    end_proj = line.project(Point(p1))
                    if end_proj <= proj_a or start_proj >= proj_b:
                        continue
                    # include p1 if it's strictly between
                    if start_proj > proj_a and end_proj < proj_b:
                        sub_coords.append((p1[0], p1[1]))
                sub_coords.append((b_pt.x, b_pt.y))
                seg_line = LineString(sub_coords)
            except Exception as e:
                # fallback minimal segment
                seg_line = LineString([(pa.x, pa.y), (pb.x, pb.y)])

            length_m = geodetic_length_of_linestring(seg_line)

            # compter maisons dont le centroid projeté se situe entre proj_a et proj_b sur 'line'
            count_houses = 0
            for h in street_house_coords.get(street_name, []):
                hcent = h.centroid
                # si hcent pas sur cette line, projectera inertiellement; vérifier si proche
                proj_h = line.project(hcent)
                # accepter si projection entre et que la distance du centroid au lignestring est raisonnable
                if proj_h + 1e-9 >= proj_a and proj_h - 1e-9 <= proj_b and line.distance(hcent) < 0.001:
                    count_houses += 1
                else:
                    # alternative: tester géodésique si centroid geographically lies within bounding box of subsegment
                    pass

            arcs.append({
                'u': v_a,
                'v': v_b,
                'street': street_name,
                'n_houses': count_houses,
                'length_m': round(length_m, 3),
                'coords': [(sub_coords[0][0], sub_coords[0][1]), (sub_coords[-1][0], sub_coords[-1][1])]
            })

# nettoyer doublons (un arc peut être trouvé deux fois si lignes multi-part entretenues) :
unique_arcs = {}
for arc in arcs:
    key = tuple(sorted([arc['u'], arc['v']])) + (arc['street'], round(arc['length_m'],3))
    # si doublon garder le plus grand count/harmoniser length si nécessaire
    if key not in unique_arcs:
        unique_arcs[key] = arc
    else:
        # choisir arc avec plus de maisons (ou moyenne)
        exist = unique_arcs[key]
        if arc['n_houses'] > exist['n_houses']:
            unique_arcs[key] = arc

final_arcs = list(unique_arcs.values())

# Construire mapping sommets -> rues qui en partent (mise à jour plus complète)
vertices_streets = defaultdict(set)
for arc in final_arcs:
    vertices_streets[arc['u']].add(arc['street'])
    vertices_streets[arc['v']].add(arc['street'])

for vname in vertices.keys():
    if vname in vertices_streets:
        vertices[vname]['streets'] = sorted(list(vertices_streets[vname]))
    else:
        # si sommet isolé (rare), garder la liste précédente
        vertices[vname]['streets'] = vertices[vname].get('streets', [])

collect_points = [
    {"id": "P_Mairie", "name": "Mairie", "lat": 45.9450, "lon": -1.0995, "capacity": 6},
    {"id": "P_Camping", "name": "Camping", "lat": 45.9420, "lon": -1.0850, "capacity": 8},
    {"id": "P_École", "name": "École", "lat": 45.9440, "lon": -1.0960, "capacity": 4},
    {"id": "P_ZoneIndus", "name": "Zone industrielle", "lat": 45.9480, "lon": -1.0720, "capacity": 10},
]

depots = [
    {"id": "DEPOT1", "name": "Dépôt principal", "lat": 45.9455, "lon": -1.1000}
]

def nearest_street(lat, lon):
    point = Point(lon, lat)
    min_dist = float('inf')
    closest = None
    for name, geom in street_geoms.items():
        nearest_pt = nearest_points(point, geom)[1]
        dist = geodesic((lat, lon), (nearest_pt.y, nearest_pt.x)).meters
        if dist < min_dist:
            min_dist = dist
            closest = name
    return closest, round(min_dist, 2)

for p in collect_points + depots:
    street, dist = nearest_street(p['lat'], p['lon'])
    p['street'] = street
    p['distance_to_street_m'] = dist
# -----------------------------
# écrire le fichier texte final
# -----------------------------
with open("MontBrunLesBainsv2.txt", "w", encoding="utf-8") as f:
    f.write("MAISONS PAR RUE:\n")
    for street, count in houses_per_street.items():
        length = street_lengths.get(street, 0)
        f.write(f"{street} ; {count} ; {length}\n")

    f.write("\nVERTICES (sommets = carrefours) :\n")
    for vname, info in sorted(vertices.items(), key=lambda x: x[0]):
        x, y = info['coord']
        streets_here = ";".join(info['streets'])
        f.write(f"{vname} ; {round(y,6)},{round(x,6)} ; {streets_here}\n")

    f.write("\nARCS (segments entre sommets) :\n")
    for arc in final_arcs:
        coord_u = vertices[arc['u']]['coord']
        coord_v = vertices[arc['v']]['coord']
        f.write(
            f"{arc['u']}-{arc['v']} ; {arc['street']} ; {arc['n_houses']} ; "
            f"{arc['length_m']} ; "
            f"{round(coord_u[1],6)},{round(coord_u[0],6)} ; "
            f"{round(coord_v[1],6)},{round(coord_v[0],6)}\n"
        )

    # ---------- AJOUT IMPORTANT : POINTS DE COLLECTE ----------
    f.write("\nPOINTS DE COLLECTE:\n")
    for p in collect_points:
        f.write(
            f"{p['name']} ; {p['capacity']} ; {p['street']} ; {p['distance_to_street_m']}\n"
        )

    # ---------- AJOUT IMPORTANT : DEPOTS ----------
    f.write("\nDEPOT:\n")
    for d in depots:
        f.write(
            f"{d['name']} ; {d['street']} ; {d['distance_to_street_m']}\n"
        )

print("Fichier final généré avec arcs + sommets + points de collecte + dépôts.")

