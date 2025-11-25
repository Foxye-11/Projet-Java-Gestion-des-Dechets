import Architecture.Fichier;
import Graphique.GraphiqueFenetre;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static Map<String, Set<String>> construireGraphe(Fichier fichier) {
        Map<String, Set<String>> graph = new LinkedHashMap<>();

        for (String ligne : fichier.getListeIntersections().keySet()) {
            String[] rues = ligne.split(",");
            if (rues.length < 2) continue;

            String rue1 = rues[0].trim();
            String rue2 = rues[1].trim();

            graph.putIfAbsent(rue1, new LinkedHashSet<>());
            graph.putIfAbsent(rue2, new LinkedHashSet<>());

            graph.get(rue1).add(rue2);
            graph.get(rue2).add(rue1);
        }
        return graph;
    }

    public static void main(String[] args) {
        try {
            Fichier fichier = new Fichier();

            Map<String, Set<String>> graph = construireGraphe(fichier);

            new GraphiqueFenetre(graph);

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}