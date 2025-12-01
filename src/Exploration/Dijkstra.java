package Exploration;

import Architecture.Arc;
import Architecture.Sommet.Sommets;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {

    public Map<Sommets, Integer> dijkstra_map(Sommets source) {

        Map<Sommets, Integer> distance = new HashMap<>();
        Map<Sommets, Sommets> parent = new HashMap<>();
        PriorityQueue<Sommets> file = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        // Initialisation
        // Warning : besoin d'une liste globale des sommets du graphe
        for (Sommets s : graphe.getTousLesSommets()) {
            distance.put(s, Integer.MAX_VALUE);
        }

        distance.put(source, 0);
        file.add(source);

        while (!file.isEmpty()) {

            Sommets u = file.poll();

            for (Arc arc : u.getArcs_sortants().values()) {

                Sommets v = arc.chgtSommet(u);
                int poids = arc.getNb_habitations();

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

    public Map<Sommets, Sommets> dijkstra(Sommets source, Sommets destination) {

        Map<Sommets, Integer> distance = new HashMap<>();
        Map<Sommets, Sommets> parent = new HashMap<>();
        PriorityQueue<Sommets> file = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        // Initialisation
        // Warning : besoin d'une liste globale des sommets du graphe
        for (Sommets s : graphe.getTousLesSommets()) {
            distance.put(s, Integer.MAX_VALUE);
        }

        distance.put(source, 0);
        file.add(source);

        while (!file.isEmpty()) {
            Sommets u = file.poll();

            // 🔥 Condition d'arrêt : on a atteint la destination
            if (u.equals(destination)) {
                break;
            }

            for (Arc arc : u.getArcs_sortants().values()) {

                Sommets v = arc.chgtSommet(u);
                int poids = arc.getNb_habitations();

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

}


