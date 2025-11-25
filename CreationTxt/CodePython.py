!pip install shapely pyproj geojson tqdm
#==============================================================================A METTRE SUR GOOGLE COLAB DANS 2 CELLULES DIFFERENTES
from shapely.geometry import shape, Point, LineString
from shapely.ops import nearest_points
from collections import Counter, defaultdict
import json
from geopy.distance import geodesic

MAX_DIST_NORMAL = 5
MAX_DIST_IMPASSE = 2


with open("portdesbarques.geojson") as f:
    data = json.load(f)

streets = [f for f in data['features'] if f['properties'].get('highway')]
buildings = [f for f in data['features'] if f['properties'].get('building')]

houses_per_street = Counter()
street_lengths = {}
street_geoms = {}
street_house_coords = defaultdict(list)

for s in streets:
    name = s['properties'].get('name')
    if not name:
        continue

    geom = shape(s['geometry'])
    coords = []

    if geom.geom_type == 'LineString':
        coords = list(geom.coords)
    elif geom.geom_type == 'MultiLineString':
        coords = [pt for line in geom.geoms for pt in line.coords]

    length_m = 0
    for i in range(len(coords)-1):
        length_m += geodesic((coords[i][1], coords[i][0]), (coords[i+1][1], coords[i+1][0])).meters
    street_lengths[name] = round(length_m, 1)
    street_geoms[name] = geom

for b in buildings:
    props = b.get('properties', {})
    street_name = props.get('addr:street')
    housenumber = props.get('addr:housenumber')

    b_geom = shape(b['geometry'])
    if street_name in street_lengths and housenumber:
        houses_per_street[street_name] += 1
        street_house_coords[street_name].append(b_geom)
    else:

        min_dist = float('inf')
        closest_street = None
        for name, geom in street_geoms.items():
            dist = b_geom.distance(geom)
            if dist < min_dist:
                min_dist = dist
                closest_street = name
        houses_per_street[closest_street] += 1
        street_house_coords[closest_street].append(b_geom)

intersection_segments = []

for street1, geom1 in street_geoms.items():
    for street2, geom2 in street_geoms.items():
        if street1 >= street2:
            continue
        if geom1.intersects(geom2):
            pt = geom1.intersection(geom2)
            points = []
            if pt.geom_type == 'Point':
                points = [pt]
            elif pt.geom_type == 'MultiPoint':
                points = list(pt.geoms)

            for p in points:

                proj1 = geom1.project(p)
                proj2 = geom2.project(p)

                length1 = proj1 * street_lengths[street1] / geom1.length
                length2 = proj2 * street_lengths[street2] / geom2.length

                count1 = 0
                for h in street_house_coords[street1]:
                    pt = h.centroid 
                    if abs(geom1.project(pt) - proj1) < 1:  
                        count1 += 1

                count2 = 0
                for h in street_house_coords[street2]:
                    pt = h.centroid
                    if abs(geom2.project(pt) - proj2) < 1:
                        count2 += 1

                total_count = count1 + count2

                intersection_segments.append((street1, street2, total_count, round(length1+length2,1)))

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

with open("PortDesBarques.txt", "w", encoding="utf-8") as f:
    f.write("MAISONS PAR RUE:\n")
    for street, count in houses_per_street.items():
        length = street_lengths.get(street, 0)
        f.write(f"{street} ; {count} ; {length}\n")

    f.write("\nINTERSECTIONS ENTRE RUES:\n")
    for s1, s2, count, length in intersection_segments:
        f.write(f"{s1} , {s2} ; {count} ; {length}\n")

    f.write("\nPOINTS DE COLLECTE:\n")
    for p in collect_points:
        f.write(f"{p['name']} ; {p['capacity']} ; {p['street']} ; {p['distance_to_street_m']}\n")

    f.write("\nDEPOT:\n")
    for d in depots:
        f.write(f"{d['name']} ; {d['street']} ; {d['distance_to_street_m']}\n")

print("Fichier final généré avec arcs ayant une longueur correcte.")
