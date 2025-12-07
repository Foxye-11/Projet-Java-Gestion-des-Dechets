package Global;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Quartier;
import Global.Architecture.Rue;
import Global.Exploration.BFS;
import Global.Graphique.GraphiqueFenetre;
import Global.Graphique.GraphismeControle;
import Global.Planification.Calendrier;
import Global.Planification.Planifier;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.*;

public class Main {

    public static Map<String, Set<GraphismeControle.Arc>> construireGraphe(Fichier fichier) {

        Map<String, Set<GraphismeControle.Arc>> graph = new LinkedHashMap<>();

        // initialistaion des sommets
        for (String sommet : fichier.getListeSommets().keySet()) {
            graph.putIfAbsent(sommet.trim(), new LinkedHashSet<>());
        }

        // ajout des arcs
        for (Map.Entry<String, Arc> entry : fichier.getListeArcs().entrySet()) {

            Arc arc = entry.getValue();
            String s1 = arc.getSommet1().getNom();
            String s2 = arc.getSommet2().getNom();
            int type = arc.getSens(); // 0 = direct, 1 = inverse, 2 = bidirectionnel

            // sens 0 -> s1 -> s2
            if (type == 0 || type == 2) {
                graph.get(s1).add(new GraphismeControle.Arc(s2, type));
            }
            // sens 1 -> s2 -> s1
            if (type == 1 || type == 2) {
                graph.get(s2).add(new GraphismeControle.Arc(s1, type));
            }
        }

        return graph;
    }

    public static void main(String[] args) {
/*
        // Définition des jours autorisés par type
        Map<String, List<DayOfWeek>> joursParType = new HashMap<>();
        joursParType.put("OM", Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.THURSDAY));
        joursParType.put("Recyclable", Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY));
        joursParType.put("Verre", Arrays.asList(DayOfWeek.WEDNESDAY));
        joursParType.put("Dechets Organiques", Arrays.asList(DayOfWeek.WEDNESDAY));

        // Définition des fréquences
        Map<String, Double> frequence = new HashMap<>();
        frequence.put("OM", 2.0);
        frequence.put("Recyclable", 2.0);
        frequence.put("Verre", 0.5);
        frequence.put("Dechets Organiques", 0.3);

        Planifier planifier = new Planifier(joursParType, frequence);
        Calendrier calendrier = planifier.creerPlanning(2025);

        planifier.addTournee(LocalDate.of(2025, 1, 20), "null", "Encombrants", calendrier, null);
        calendrier.affichage(2025);

        calendrier.setVisible(true);
    }

    public static Map<String, Set<GraphismeControle.Arc>> construireGraphe(Fichier fichier) {

        Map<String, Set<GraphismeControle.Arc>> graph = new LinkedHashMap<>();

        // --- Initialisation des sommets ---
        for (String sommet : fichier.getListeSommets().keySet()) {
            graph.putIfAbsent(sommet.trim(), new LinkedHashSet<>());
        }

        // --- Ajout des arcs ---
        for (String arcNom : fichier.getListeArcs().keySet()) {
            String[] parts = arcNom.split("-"); // "D-E"
            if (parts.length != 2) continue;

            String a = parts[0].trim();
            String b = parts[1].trim();

            Set<String> info = fichier.getListeArcs().get(arcNom);
            int type = 2; // par défaut bidirectionnel
            for (String s : info) {
                try {
                    int t = Integer.parseInt(s);
                    if (t >= 0 && t <= 2) type = t;
                } catch (NumberFormatException ignored) {}
            }

            // Ajouter dans les deux sens si bidirectionnel
            if (type == 0 || type == 2) graph.get(a).add(new GraphismeControle.Arc(b, type));
            if (type == 1 || type == 2) graph.get(b).add(new GraphismeControle.Arc(a, type));
        }

        return graph;
    }

    public static void main(String[] args) {
        try {
            Fichier fichier = new Fichier("MontBrunlesBainsv2.txt");

            Map<String, Set<GraphismeControle.Arc>> graph = construireGraphe(fichier);

            new GraphiqueFenetre(graph);

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
        */

        try{
            Fichier fichier = new Fichier("MontBrunlesBainsv2.txt");
            System.out.println("Chargementde la ville effectué");
            fichier.afficherDonnees();

            Quartier q1 = new Quartier("Centre");
            Quartier q2 = new Quartier("Nord");
            Quartier q3 = new Quartier("Sud");

            for (Rue r : fichier.getListeRues().values()){
                if (r.getNom().contains("A"))q1.addRue(r);
                else if (r.getNom().contains("B"))q2.addRue(r);
                else q3.addRue(r);
            }

            q1.addVoisin(q2);
            q1.addVoisin(q3);
            q2.addVoisin(q3);

            LinkedList<Quartier> quartiers = new LinkedList<>();
            quartiers.add(q1);
            quartiers.add(q2);
            quartiers.add(q3);

            Quartier.colorierQuartiers(quartiers);

            System.out.println("\n--- Couleurs des quartiers ---");
            for (Quartier q : quartiers) {
                System.out.println(q.getNom() + " -> couleur : " + q.getCouleur());
            }

            Map<String, List<DayOfWeek>> joursParType = new HashMap<>();
            joursParType.put("OM", Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.THURSDAY));
            joursParType.put("Recyclable", Arrays.asList(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY));
            joursParType.put("Verre", List.of(DayOfWeek.WEDNESDAY));
            joursParType.put("Dechets Organiques", List.of(DayOfWeek.WEDNESDAY));

            Map<String, Double> frequence = new HashMap<>();
            frequence.put("OM", 2.0);
            frequence.put("Recyclable", 1.0);
            frequence.put("Verre", 0.5);
            frequence.put("Dechets Organiques", 1.0);

            Planifier planifier = new Planifier(joursParType, frequence);
            Calendrier calendrier = planifier.creerPlanning(2025);

            planifier.addTournee(
                    java.time.LocalDate.of(2025, 1, 20),
                    "Encombrants",
                    "Encombrants",
                    calendrier,
                    null
            );

            calendrier.affichage(2025);
            calendrier.setVisible(true);

            Map<String, Set<GraphismeControle.Arc>> graphe = construireGraphe(fichier);
            new GraphiqueFenetre(graphe);

            System.out.println("\n--- Test BFS du sommet A au sommet D ---");
            var chemin = BFS.bfsSommet("A", "D",
                    fichier.getListeSommets(),
                    fichier.getListeArcs()
            );

            if (chemin != null) {
                System.out.println("Chemin trouvé :");
                chemin.forEach(a -> System.out.println(a.getSommet1().getNom() + " -> " + a.getSommet2().getNom()));
            }
        }catch (IOException e) {
            System.err.println("Erreur de chargement du fichier : " + e.getMessage());
        }


    }
}
