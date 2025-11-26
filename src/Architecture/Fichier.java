package Architecture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Fichier {

    private String nomFichier = "MontBrunlesBainsv2.txt";

    public Map<String, Set<String>> listeRues = new LinkedHashMap<>();
    public Map<String, Set<String>> listeIntersections = new LinkedHashMap<>();
    public Map<String, Set<String>> listePointsCollectes = new LinkedHashMap<>();
    public Map<String, Set<String>> listePointsDepots = new LinkedHashMap<>();

    // --- NOUVEAU ---
    public Map<String, Set<String>> listeVertices = new LinkedHashMap<>();
    public Map<String, Set<String>> listeArcs = new LinkedHashMap<>();


    public Fichier() throws IOException {
        chargerAgglo();
    }

    private void chargerAgglo() throws IOException {

        String motCle1 = "MAISONS PAR RUE:";
        String motCle2 = "CARREFOURS";
        String motCle3 = "ARCS";
        String motCle4 = "POINTS DE COLLECTE:";
        String motCle5 = "DEPOT:";

        boolean lectureRues = false;
        boolean lectureVertices = false;
        boolean lectureArcs = false;
        boolean lectureCollecte = false;
        boolean lectureDepot = false;

        BufferedReader br = new BufferedReader(new FileReader(nomFichier));
        String line;

        while ((line = br.readLine()) != null) {

            line = line.trim();
            System.out.println("LU : >" + line + "<");
            if (line.isEmpty()) continue;

            String upper = line.toUpperCase();

            if (upper.startsWith("MAISONS PAR RUE")) {
                lectureRues = true;
                lectureVertices = lectureArcs = lectureCollecte = lectureDepot = false;
                continue;
            }

            if (upper.startsWith("CARREFOURS")) {       // <<<<<<<<<<<<<<
                lectureVertices = true;
                lectureRues = lectureArcs = lectureCollecte = lectureDepot = false;
                continue;
            }

            if (upper.startsWith("ARCS")) {           // <<<<<<<<<<<<<<
                lectureArcs = true;
                lectureRues = lectureVertices = lectureCollecte = lectureDepot = false;
                continue;
            }

            if (upper.startsWith("POINTS DE COLLECTE")) {
                lectureCollecte = true;
                lectureVertices = lectureArcs = lectureRues = lectureDepot = false;
                continue;
            }

            if (upper.startsWith("DEPOT")) {
                lectureDepot = true;
                lectureVertices = lectureArcs = lectureCollecte = lectureRues = false;
                continue;
            }
            // ---- LECTURE DES RUES ----
            if (lectureRues) {
                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String rue = parts[0].trim();
                String nbMaisons = parts[1].trim();
                String longueur = parts[2].trim();

                listeRues.putIfAbsent(rue, new LinkedHashSet<>());
                listeRues.get(rue).add(nbMaisons);
                listeRues.get(rue).add(longueur);

                continue;
            }

            // ---- LECTURE DES VERTICES ----
            if (lectureVertices) {
                // Format : A ; 44.174745,5.441666 ; Rue1;Rue2;Rue3
                String[] parts = line.split(";");
                if (parts.length < 2) continue;

                String sommet = parts[0].trim();
                String coords = parts[1].trim();

                listeVertices.putIfAbsent(sommet, new LinkedHashSet<>());
                listeVertices.get(sommet).add(coords);

                // Ajout des rues associées au carrefour
                for (int i = 2; i < parts.length; i++)
                    listeVertices.get(sommet).add(parts[i].trim());

                continue;
            }

            // ---- LECTURE DES ARCS ----
            if (lectureArcs) {
                // Format :
                // D-E ; Rue ; nbMaisons ; longueur ; lat1,lng1 ; lat2,lng2

                String[] parts = line.split(";");
                if (parts.length < 6) continue;

                String nomArc = parts[0].trim();
                listeArcs.putIfAbsent(nomArc, new LinkedHashSet<>());

                for (int i = 1; i < parts.length; i++)
                    listeArcs.get(nomArc).add(parts[i].trim());

                continue;
            }

            // ---- LECTURE DES POINTS DE COLLECTE ----
            if (lectureCollecte) {
                String[] parts = line.split(";");
                if (parts.length < 4) continue;

                String nom = parts[0].trim();
                String capacite = parts[1].trim();
                String rue = parts[2].trim();
                String longueur = parts[3].trim();

                listePointsCollectes.putIfAbsent(nom, new LinkedHashSet<>());
                listePointsCollectes.get(nom).add(capacite);
                listePointsCollectes.get(nom).add(rue);
                listePointsCollectes.get(nom).add(longueur);

                continue;
            }

            // ---- LECTURE DU DEPOT ----
            if (lectureDepot) {
                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String nom = parts[0].trim();
                String rue = parts[1].trim();
                String longueur = parts[2].trim();

                listePointsDepots.putIfAbsent(nom, new LinkedHashSet<>());
                listePointsDepots.get(nom).add(rue);
                listePointsDepots.get(nom).add(longueur);

                continue;
            }
        }

        br.close();
    }


    // --- GETTERS ---

    public Map<String, Set<String>> getListeRues() { return listeRues; }

    public Map<String, Set<String>> getListeVertices() { return listeVertices; }

    public Map<String, Set<String>> getListeArcs() { return listeArcs; }

    public Map<String, Set<String>> getListePointsCollectes() { return listePointsCollectes; }

    public Map<String, Set<String>> getListePointsDepots() { return listePointsDepots; }
}
