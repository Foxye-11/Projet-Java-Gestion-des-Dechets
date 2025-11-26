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

        // --- Initialisation des sommets ---
        for (String sommet : fichier.getListeVertices().keySet()) {
            graph.putIfAbsent(sommet.trim(), new LinkedHashSet<>());
        }

        // --- Ajout des arcs (D-E) ---
        for (String arc : fichier.getListeArcs().keySet()) {

            String cleanArc = arc.trim(); // IMPORTANT
            if (!cleanArc.contains("-")) continue;

            String[] s = cleanArc.split("-");
            if (s.length != 2) continue;

            String a = s[0].trim();
            String b = s[1].trim();

            // Vérifier que les sommets existent dans listeVertices
            if (!graph.containsKey(a) || !graph.containsKey(b)) {
                System.out.println("Arc ignoré : " + a + " - " + b);
                continue;
            }

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        return graph;
    }


    public static void main(String[] args) {

        try {
            Fichier fichier = new Fichier(); // si un chemin est requis, ajoute-le ici
            System.out.println("=== VERTICES ===");
            System.out.println(fichier.getListeVertices());

            System.out.println("=== ARCS ===");
            System.out.println(fichier.getListeArcs());

            Map<String, Set<String>> graph = construireGraphe(fichier);
            System.out.println("=== GRAPH FINAL ===");
            System.out.println(graph);

            new GraphiqueFenetre(graph);

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}
