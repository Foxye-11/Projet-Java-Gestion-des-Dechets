package Global.Exploration;

import Global.Architecture.Arc;
import Global.Architecture.Sommet.Sommet;

import java.util.*;

public class BFS {

    public static List<Arc> bfs(String nomDepart, String nomDestination, Map<String, Sommet> sommets, Map<String, Arc> arcs) {
        Sommet depart = sommets.get(nomDepart);
        Sommet destination = sommets.get(nomDestination);

        // Vérification des sommets
        if (depart == null || destination == null) {
            throw new IllegalArgumentException("Sommet inconnu.");
        }
        if (depart.equals(destination)) {
            return new ArrayList<>(); // chemin vide
        }

        // Marquage et file d'attente
        Map<String, Arc> decouvertParArc = new HashMap<>();
        Queue<Arc> file = new LinkedList<>();

        // Initialisation au point de départ
        decouvertParArc.put(depart.getNom(), null);
        for (Arc arc : depart.getArcsSortants()) {
            file.add(arc);
        }

        // Parcours BFS
        while (!file.isEmpty()) {
            // Recupération de la station d'arrivé de l'arc
            Arc arc = file.poll();
            Sommet arrivee = arc.getSommet2();

            // Marquage des stations non découverte
            if (!decouvertParArc.containsKey(arrivee.getNom())) {
                decouvertParArc.put(arrivee.getNom(), arc);
                // Si destination trouvée, fin
                if (arrivee.equals(destination)) break;
                // Ajout à la file d'attente les nouvelles stations découvertes
                for (Arc suivant : arrivee.getArcsSortants()) {
                    // BFS classique → file FIFO
                    ///file.add(suivant);
                    // algorithme BFS 0-1
                    if (suivant.getNomRue().equals(arc.getNomRue())) {
                        ((LinkedList<Arc>) file).addFirst(suivant); // même ligne → priorité
                    } else {
                        ((LinkedList<Arc>) file).addLast(suivant);  // changement → pénalité
                    }

                }
            }
        }

        // Reconstruction du chemin
        if (!decouvertParArc.containsKey(destination.getNom())) {
            System.out.println("Aucun itinéraire trouvé.");
            return null;
        }

        List<Arc> chemin = new LinkedList<>();
        Sommet courant = destination;

        while (!courant.equals(depart)) {
            Arc arc = decouvertParArc.get(courant.getNom());
            chemin.add(0, arc);
            courant = arc.getSommet1(); // sommet de départ de l’arc
        }

        return chemin;
    }


    public static List<Sommet> bfsAll(
            String nomDepart,
            Map<String, Sommet> sommets) {

        Sommet depart = sommets.get(nomDepart);
        if (depart == null) throw new IllegalArgumentException("Sommet inconnu.");

        List<Sommet> ordreVisite = new ArrayList<>();
        Set<String> visites = new HashSet<>();
        Queue<Sommet> file = new LinkedList<>();

        visites.add(depart.getNom());
        file.add(depart);

        while (!file.isEmpty()) {
            Sommet courant = file.poll();
            ordreVisite.add(courant);

            for (Arc arc : courant.getArcsSortants()) {
                Sommet voisin = arc.getSommet2();
                if (!visites.contains(voisin.getNom())) {
                    visites.add(voisin.getNom());
                    file.add(voisin);
                }
            }
        }
        return ordreVisite;
    }


    public static List<Arc> dfs(
            String nomDepart,
            String nomDestination,
            Map<String, Sommet> sommets,
            Map<String, Arc> arcs) {

        Sommet depart = sommets.get(nomDepart);
        Sommet destination = sommets.get(nomDestination);

        if (depart == null || destination == null) {
            throw new IllegalArgumentException("Sommet inconnu.");
        }
        if (depart.equals(destination)) {
            return new ArrayList<>(); // chemin vide
        }

        // Marquage et pile
        Map<String, Arc> decouvertParArc = new HashMap<>();
        Stack<Arc> pile = new Stack<>();

        // Initialisation
        decouvertParArc.put(depart.getNom(), null);
        for (Arc arc : depart.getArcsSortants()) {
            pile.push(arc);
        }

        // Parcours DFS
        while (!pile.isEmpty()) {
            Arc arc = pile.pop();
            Sommet arrivee = arc.getSommet2();

            if (!decouvertParArc.containsKey(arrivee.getNom())) {
                decouvertParArc.put(arrivee.getNom(), arc);

                if (arrivee.equals(destination)) break;

                // Ajout des arcs sortants du sommet découvert
                for (Arc suivant : arrivee.getArcsSortants()) {
                    pile.push(suivant);
                }
            }
        }

        // Reconstruction du chemin
        if (!decouvertParArc.containsKey(destination.getNom())) {
            System.out.println("Aucun itinéraire trouvé.");
            return null;
        }

        List<Arc> chemin = new LinkedList<>();
        Sommet courant = destination;

        while (!courant.equals(depart)) {
            Arc arc = decouvertParArc.get(courant.getNom());
            chemin.add(0, arc);
            courant = arc.getSommet1();
        }

        return chemin;
    }


    public static List<Sommet> dfsAll(
            String nomDepart,
            Map<String, Sommet> sommets) {

        Sommet depart = sommets.get(nomDepart);
        if (depart == null) throw new IllegalArgumentException("Sommet inconnu.");

        List<Sommet> ordreVisite = new ArrayList<>();
        Set<String> visites = new HashSet<>();
        Stack<Sommet> pile = new Stack<>();

        pile.push(depart);

        while (!pile.isEmpty()) {
            Sommet courant = pile.pop();
            if (!visites.contains(courant.getNom())) {
                visites.add(courant.getNom());
                ordreVisite.add(courant);

                for (Arc arc : courant.getArcsSortants()) {
                    pile.push(arc.getSommet2());
                }
            }
        }
        return ordreVisite;
    }

    
    public static List<Arc> dijkstra(
            String nomDepart,
            String nomDestination,
            Map<String, Sommet> sommets,
            Map<String, Arc> arcs) {

        Sommet depart = sommets.get(nomDepart);
        Sommet destination = sommets.get(nomDestination);

        if (depart == null || destination == null) {
            throw new IllegalArgumentException("Sommet inconnu.");
        }
        if (depart.equals(destination)) {
            return new ArrayList<>(); // chemin vide
        }

        // Distances et prédécesseurs
        Map<Sommet, Double> distance = new HashMap<>();
        Map<Sommet, Arc> precedent = new HashMap<>();

        for (Sommet s : sommets.values()) {
            distance.put(s, Double.POSITIVE_INFINITY);
        }
        distance.put(depart, 0.0);

        // File de priorité (min-heap)
        PriorityQueue<Sommet> file = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
        file.add(depart);

        // Algorithme principal
        while (!file.isEmpty()) {
            Sommet courant = file.poll();

            if (courant.equals(destination)) break; // plus court chemin trouvé

            for (Arc arc : courant.getArcsSortants()) {
                Sommet voisin = arc.getSommet2();
                double coutArc = arc.getLongueur(); // chois du critère (longueur, nbMaisons, etc.)
                double nouvelleDistance = distance.get(courant) + coutArc;

                if (nouvelleDistance < distance.get(voisin)) {
                    distance.put(voisin, nouvelleDistance);
                    precedent.put(voisin, arc);
                    file.remove(voisin); // mise à jour
                    file.add(voisin);
                }
            }
        }

        // Reconstruction du chemin
        if (!precedent.containsKey(destination)) {
            System.out.println("Aucun itinéraire trouvé.");
            return null;
        }

        List<Arc> chemin = new LinkedList<>();
        Sommet courant = destination;

        while (!courant.equals(depart)) {
            Arc arc = precedent.get(courant);
            chemin.add(0, arc);
            courant = arc.getSommet1();
        }

        return chemin;
    }


    public static Map<Sommet, Double> dijkstraAll(
            String nomDepart,
            Map<String, Sommet> sommets) {

        Sommet depart = sommets.get(nomDepart);
        if (depart == null) throw new IllegalArgumentException("Sommet inconnu.");

        Map<Sommet, Double> distance = new HashMap<>();
        for (Sommet s : sommets.values()) {
            distance.put(s, Double.POSITIVE_INFINITY);
        }
        distance.put(depart, 0.0);

        PriorityQueue<Sommet> file = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
        file.add(depart);

        while (!file.isEmpty()) {
            Sommet courant = file.poll();

            for (Arc arc : courant.getArcsSortants()) {
                Sommet voisin = arc.getSommet2();
                double coutArc = arc.getLongueur(); // ou autre critère
                double nouvelleDistance = distance.get(courant) + coutArc;

                if (nouvelleDistance < distance.get(voisin)) {
                    distance.put(voisin, nouvelleDistance);
                    file.remove(voisin);
                    file.add(voisin);
                }
            }
        }
        return distance; // distances minimales vers tous les sommets
    }
}
