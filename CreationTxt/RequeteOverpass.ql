[out:json][timeout:45]; //temps avant l'abandon de la requête en cas de problème

{{geocodeArea:Tourcoing}}->.searchArea; //choix de la ville / village

(
  way["building"](area.searchArea); //batiments
  way["highway"](area.searchArea); //rues
  node["amenity"="townhall"](area.searchArea); //mairie
);

out geom;
