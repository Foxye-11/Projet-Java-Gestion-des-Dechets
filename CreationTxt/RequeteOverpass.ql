[out:json][timeout:45];

{{geocodeArea:Tourcoing}}->.searchArea;

// Bâtiments, routes, mairie
(
  way["building"](area.searchArea);
  way["highway"](area.searchArea);
  node["amenity"="townhall"](area.searchArea);
);

out geom;
