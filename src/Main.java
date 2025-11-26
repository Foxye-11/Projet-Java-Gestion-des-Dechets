import Architecture.Fichier;
import Graphique.GraphiqueFenetre;
import Graphique.GraphismeControle;

import java.io.IOException;
import java.util.*;

public class Main {

    public static Map<String, Set<GraphismeControle.Arc>> construireGraphe(Fichier fichier) {

        Map<String, Set<GraphismeControle.Arc>> graph = new LinkedHashMap<>();

        // --- Initialisation des sommets ---
        for (String sommet : fichier.getListeVertices().keySet()) {
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
            Fichier fichier = new Fichier();

            Map<String, Set<GraphismeControle.Arc>> graph = construireGraphe(fichier);

            new GraphiqueFenetre(graph);

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}
