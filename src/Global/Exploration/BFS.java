package Global.Exploration;

import Global.Architecture.Arc;
import Global.Architecture.Quartier;
import Global.Architecture.Sommet.Sommets;

import java.util.*;

public class BFS {

    public static List<Arc> bfsSommet(String nomDepart, String nomDestination, Map<String, Sommets> sommets, Map<String, Arc> arcs) {
        Sommets depart = sommets.get(nomDepart);
        Sommets destination = sommets.get(nomDestination);

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
            Sommets arrivee = arc.getSommet2();

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
        Sommets courant = destination;

        while (!courant.equals(depart)) {
            Arc arc = decouvertParArc.get(courant.getNom());
            chemin.add(0, arc);
            courant = arc.getSommet1(); // sommet de départ de l’arc
        }

        return chemin;
    }

    public static List<Arc> bfsArc(String nomDepart, String nomDestination, Map<String, Sommets> sommets, Map<String, Arc> arcs) {
        Sommets depart = sommets.get(nomDepart);
        Arc destination = arcs.get(nomDestination);

        // Vérification des sommets
        if (depart == null || destination == null) {
            throw new IllegalArgumentException("Sommet inconnu.");
        }
        if (depart.equals(destination.getSommet1()) ) {
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
            Sommets arrivee = arc.getSommet2();

            // Marquage des stations non découverte
            if (!decouvertParArc.containsKey(arrivee.getNom())) {
                decouvertParArc.put(arrivee.getNom(), arc);
                // Si destination trouvée, fin
                if (arc.equals(destination)) break;
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
        if (!decouvertParArc.containsKey(destination.getSommet1().getNom())|| !decouvertParArc.containsKey(destination.getSommet2().getNom())) {
            System.out.println("Aucun itinéraire trouvé.");
            return null;
        }

        List<Arc> chemin = new LinkedList<>();
        Arc courant = destination;

        while (!courant.equals(depart.getArcsEntrant())) {
            Arc arc = decouvertParArc.get(courant.getSommet1().getNom());
            chemin.add(0, arc);
            courant = arc; // arc de départ de l’arc
        }

        return chemin;
    }

    public static List<Arc> bfsMultiArcs(String nomDepart, Arc[] destination, Map<String, Sommets> sommets, Map<String, Arc> arcs) {
        Sommets depart = sommets.get(nomDepart);

        Arc destination1 = null;

        // Vérification des sommets
        if (depart == null || destination == null) {
            throw new IllegalArgumentException("Sommet inconnu.");
        }
        for (int i = 0; i < destination.length; i++) {
            if (depart.equals(destination[i].getSommet1()) ) {
                return new ArrayList<>(); // chemin vide
            }
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
            Sommets arrivee = arc.getSommet2();

            // Marquage des stations non découverte
            if (!decouvertParArc.containsKey(arrivee.getNom())) {
                decouvertParArc.put(arrivee.getNom(), arc);
                // Si destination trouvée, fin
                for (int  i = 0; i < destination.length; i++) {
                    if (arc.equals(destination[i])){
                        destination1 = destination[i];
                        break;
                    }
                }

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
        if (!decouvertParArc.containsKey(destination1.getSommet1().getNom())|| !decouvertParArc.containsKey(destination1.getSommet2().getNom())) {
            System.out.println("Aucun itinéraire trouvé.");
            return null;
        }

        List<Arc> chemin = new LinkedList<>();
        Arc courant = destination1;

        while (!courant.equals(depart.getArcsEntrant())) {
            Arc arc = decouvertParArc.get(courant.getSommet1().getNom());
            chemin.add(0, arc);
            courant = arc; // arc de départ de l’arc
        }

        return chemin;
    }


    public static List<Sommets> bfsAll(
            String nomDepart,
            Map<String, Sommets> sommets) {

        Sommets depart = sommets.get(nomDepart);
        if (depart == null) throw new IllegalArgumentException("Sommet inconnu.");

        List<Sommets> ordreVisite = new ArrayList<>();
        Set<String> visites = new HashSet<>();
        Queue<Sommets> file = new LinkedList<>();

        visites.add(depart.getNom());
        file.add(depart);

        while (!file.isEmpty()) {
            Sommets courant = file.poll();
            ordreVisite.add(courant);

            for (Arc arc : courant.getArcsSortants()) {
                Sommets voisin = arc.getSommet2();
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
            Map<String, Sommets> sommets,
            Map<String, Arc> arcs) {

        Sommets depart = sommets.get(nomDepart);
        Sommets destination = sommets.get(nomDestination);

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
            Sommets arrivee = arc.getSommet2();

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
        Sommets courant = destination;

        while (!courant.equals(depart)) {
            Arc arc = decouvertParArc.get(courant.getNom());
            chemin.add(0, arc);
            courant = arc.getSommet1();
        }

        return chemin;
    }


    public static List<Sommets> dfsAll(
            String nomDepart,
            Map<String, Sommets> sommets) {

        Sommets depart = sommets.get(nomDepart);
        if (depart == null) throw new IllegalArgumentException("Sommet inconnu.");

        List<Sommets> ordreVisite = new ArrayList<>();
        Set<String> visites = new HashSet<>();
        Stack<Sommets> pile = new Stack<>();

        pile.push(depart);

        while (!pile.isEmpty()) {
            Sommets courant = pile.pop();
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
            Map<String, Sommets> sommets,
            Map<String, Arc> arcs) {

        Sommets depart = sommets.get(nomDepart);
        Sommets destination = sommets.get(nomDestination);

        if (depart == null || destination == null) {
            throw new IllegalArgumentException("Sommet inconnu.");
        }
        if (depart.equals(destination)) {
            return new ArrayList<>(); // chemin vide
        }

        // Distances et prédécesseurs
        Map<Sommets, Double> distance = new HashMap<>();
        Map<Sommets, Arc> precedent = new HashMap<>();

        for (Sommets s : sommets.values()) {
            distance.put(s, Double.POSITIVE_INFINITY);
        }
        distance.put(depart, 0.0);

        // File de priorité (min-heap)
        PriorityQueue<Sommets> file = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
        file.add(depart);

        // Algorithme principal
        while (!file.isEmpty()) {
            Sommets courant = file.poll();

            if (courant.equals(destination)) break; // plus court chemin trouvé

            for (Arc arc : courant.getArcsSortants()) {
                Sommets voisin = arc.getSommet2();
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
        Sommets courant = destination;

        while (!courant.equals(depart)) {
            Arc arc = precedent.get(courant);
            chemin.add(0, arc);
            courant = arc.getSommet1();
        }

        return chemin;
    }

    public static List<Arc> dijkstraMultiArc(
            String nomDepart,
            Arc [] destination,
            Map<String, Sommets> sommets,
            Map<String, Arc> arcs) {

        Sommets depart = sommets.get(nomDepart);


        if (depart == null || destination == null) {
            throw new IllegalArgumentException("Sommet inconnu.");
        }
        for (int j = 0; j < destination.length; j++) {
            if (depart.equals(destination[j].getSommet1())) {
                return new ArrayList<>(); // chemin vide
            }
        }


        // Distances et prédécesseurs
        Map<Sommets, Double> distance = new HashMap<>();
        Map<Sommets, Arc> precedent = new HashMap<>();

        for (Sommets s : sommets.values()) {
            distance.put(s, Double.POSITIVE_INFINITY);
        }
        distance.put(depart, 0.0);

        // File de priorité (min-heap)
        PriorityQueue<Sommets> file = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
        file.add(depart);

        Arc arrive = null;
        // Algorithme principal
        while (!file.isEmpty()) {
            Sommets courant = file.poll();

            for (int i = 0; i < destination.length; i++) {
                if (courant.equals(destination[i].getSommet2())){
                    arrive = destination[i];
                    break; // plus court chemin trouvé
                }
            }


            for (Arc arc : courant.getArcsSortants()) {
                Sommets voisin = arc.getSommet2();
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
        if (!precedent.containsKey(arrive.getSommet2())) {
            System.out.println("Aucun itinéraire trouvé.");
            return null;
        }

        List<Arc> chemin = new LinkedList<>();
        Sommets courant = arrive.getSommet2();

        while (!courant.equals(depart)) {
            Arc arc = precedent.get(courant);
            chemin.add(0, arc);
            courant = arc.getSommet1();
        }

        return chemin;
    }


    public static Map<Sommets, Double> dijkstraAll(
            String nomDepart,
            Map<String, Sommets> sommets) {

        Sommets depart = sommets.get(nomDepart);
        if (depart == null) throw new IllegalArgumentException("Sommet inconnu.");

        Map<Sommets, Double> distance = new HashMap<>();
        for (Sommets s : sommets.values()) {
            distance.put(s, Double.POSITIVE_INFINITY);
        }
        distance.put(depart, 0.0);

        PriorityQueue<Sommets> file = new PriorityQueue<>(Comparator.comparingDouble(distance::get));
        file.add(depart);

        while (!file.isEmpty()) {
            Sommets courant = file.poll();

            for (Arc arc : courant.getArcsSortants()) {
                Sommets voisin = arc.getSommet2();
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

    //Algorithme de Hierholzer
    public static Sommets getNext(Sommets depart, Arc arc) {
        if (arc.getSommet1().equals(depart))
            return arc.getSommet2();
        return null;  // L’arc ne part pas de ce sommet
    }

    public static List<Arc> hierholzerArcs(String nomDepart, Map<String, Sommets> mapSommet) {

        //Initialisation de l'origine
        Sommets start = mapSommet.get(nomDepart);

        if (start == null) return Collections.emptyList();

        Stack<Sommets> stack = new Stack<>();
        Stack<Arc> stackArcs = new Stack<>();
        List<Arc> resultat = new ArrayList<>();
        Set<Arc> utilises = new HashSet<>();

        stack.push(start);

        while (!stack.isEmpty()) {

            Sommets top = stack.peek();

            // Cherche un arc sortant non utilisé
            Arc arcDispo = null;
            for (Arc arc : top.getArcsSortants()) {
                if (!utilises.contains(arc)) {
                    arcDispo = arc;
                    break;
                }
            }

            if (arcDispo != null) {

                utilises.add(arcDispo);
                Sommets suivant = getNext(top, arcDispo);

                if (suivant == null) {
                    throw new IllegalStateException("Arc " + arcDispo.getNomRue() + " ne part pas du sommet " + top.getNom());
                }

                stack.push(suivant);
                stackArcs.push(arcDispo);
            }
            else {
                // Aucun arc sortant restant donc on revient sur nos pas (sur les arcs déjà explorés)
                stack.pop();
                if (!stackArcs.isEmpty()) {
                    resultat.add(stackArcs.pop());
                }
            }
        }

        Collections.reverse(resultat);

        return resultat;
    }

    //Hierholzer dans un seul quartier
    public static Sommets getNextInQuartier(Sommets depart, Arc arc, Quartier quartier) {
        if (arc.getSommet1().equals(depart)) {
            for (int i=0; i<quartier.getRues().size();i++){
                if (quartier.getRues().get(i).getEnsemble_rue().contains(arc)){
                    return arc.getSommet2();
                }
            }
        }
        return null;  // L’arc ne part pas de ce sommet
    }

    public static List<Arc> hierholzerArcsQuartier(String nomDepart, Map<String, Sommets> mapSommet, Quartier quartier) {

        //Initialisation de l'origine
        Sommets start = mapSommet.get(nomDepart);

        if (start == null) return Collections.emptyList();

        Stack<Sommets> stack = new Stack<>();
        Stack<Arc> stackArcs = new Stack<>();
        List<Arc> resultat = new ArrayList<>();
        Set<Arc> utilises = new HashSet<>();

        stack.push(start);

        while (!stack.isEmpty()) {

            Sommets top = stack.peek();

            // Cherche un arc sortant non utilisé
            Arc arcDispo = null;
            for (Arc arc : top.getArcsSortants()) {
                if (!utilises.contains(arc)) {
                    arcDispo = arc;
                    break;
                }
            }

            if (arcDispo != null) {

                utilises.add(arcDispo);
                Sommets suivant = getNextInQuartier(top, arcDispo,quartier);

                if (suivant == null) {
                    throw new IllegalStateException("Arc " + arcDispo.getNomRue() + " ne part pas du sommet " + top.getNom());
                }

                stack.push(suivant);
                stackArcs.push(arcDispo);
            }
            else {
                // Aucun arc sortant restant donc on revient sur nos pas (sur les arcs déjà explorés)
                stack.pop();
                if (!stackArcs.isEmpty()) {
                    resultat.add(stackArcs.pop());
                }
            }
        }

        Collections.reverse(resultat);

        return resultat;
    }



    public static List<Arc> kruskal(List<Sommets> sommets, List<Arc> aretes) {
        List<Arc> arbre = new ArrayList<>();
        Map<Sommets, Integer> numCC = new HashMap<>(); // composantes connexes
        int nb_aretes = 0;

        // Initialiser les numéros de composante connexe
        int compteur = 0;
        for (Sommets s : sommets) {
            numCC.put(s, compteur++);
        }

        // Trier les aretes par poids croissant
        aretes.sort(Comparator.comparingDouble(Arc::getLongueur));

        // Tant que nb_aretes < ordre - 1
        while (nb_aretes < sommets.size() - 1) {
            Arc a = aretes.remove(0); // prochaine arête

            int cc1 = numCC.get(a.getSommet1()); // num de composante connexe
            int cc2 = numCC.get(a.getSommet2()); // num de composante connexe

            if (cc1 != cc2) {
                arbre.add(a);

                // fusion des composantes
                for (Sommets s : sommets) {
                    if (numCC.get(s) == cc2) {
                        numCC.put(s, cc1); // mise à jour vers la composante de s1
                    }
                }
                nb_aretes++;
            }
        }
        return arbre;
    }

}
