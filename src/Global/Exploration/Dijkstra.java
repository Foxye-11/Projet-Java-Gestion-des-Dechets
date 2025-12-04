package Global.Exploration;

public class Dijkstra {

    /*
    public Map<Sommet, Integer> dijkstra_map(Sommet source) {

        Map<Sommet, Integer> distance = new HashMap<>();
        Map<Sommet, Sommet> parent = new HashMap<>();
        PriorityQueue<Sommet> file = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        // Initialisation
        // Warning : besoin d'une liste globale des sommets du graphe
        for (Sommet s : graphe.getTousLesSommets()) {
            distance.put(s, Integer.MAX_VALUE);
        }

        distance.put(source, 0);
        file.add(source);

        while (!file.isEmpty()) {

            Sommet u = file.poll();

            for (Arc arc : u.getArcsSortants().values()) {

                Sommet v = arc.chgtSommet(u);
                int poids = arc.getNbMaisons();

                int newDist = distance.get(u) + poids;

                if (newDist < distance.get(v)) {
                    distance.put(v, newDist);
                    parent.put(v, u);
                    file.add(v);
                }
            }
        }

        return distance; // ou parent si tu veux reconstruire les chemins
    }

    public Map<Sommet, Sommet> dijkstra(Sommet source, Sommet destination) {

        Map<Sommet, Integer> distance = new HashMap<>();
        Map<Sommet, Sommet> parent = new HashMap<>();
        PriorityQueue<Sommet> file = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        // Initialisation
        // Warning : besoin d'une liste globale des sommets du graphe
        for (Sommet s : graphe.getTousLesSommets()) {
            distance.put(s, Integer.MAX_VALUE);
        }

        distance.put(source, 0);
        file.add(source);

        while (!file.isEmpty()) {
            Sommet u = file.poll();

            // 🔥 Condition d'arrêt : on a atteint la destination
            if (u.equals(destination)) {
                break;
            }

            for (Arc arc : u.getArcsSortants().values()) {

                Sommet v = arc.chgtSommet(u);
                int poids = arc.getNbMaisons();

                int newDist = distance.get(u) + poids;

                if (newDist < distance.get(v)) {
                    distance.put(v, newDist);
                    parent.put(v, u);
                    file.add(v);
                }
            }
        }

        return parent; // Sert à reconstruire le chemin
    }
    */

}


