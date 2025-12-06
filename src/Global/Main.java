package Global;

import Global.Architecture.Fichier;
import Global.Graphique.GraphiqueFenetre;
import Global.Graphique.GraphismeControle;
import Global.Planification.Calendrier;
import Global.Planification.Planifier;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {

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

        planifier.addTournee(LocalDate.of(2025, 1, 20), "Dechets Organiques", "collecte", calendrier, null);
        calendrier.affichage(2025);

        calendrier.setVisible(true);
    }
    /*
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
    }
    */

}
