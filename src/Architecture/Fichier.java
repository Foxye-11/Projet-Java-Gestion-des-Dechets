package Architecture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Fichier {
    private String nomFichier = "MontBrunlesBains.txt";
    public Map<String, Set<String>> listeRues = new LinkedHashMap<>();
    public Map<String, Set<String>> listeIntersections = new LinkedHashMap<>();
    public Map<String, Set<String>> listePointsCollectes = new LinkedHashMap<>();
    public Map<String, Set<String>> listePointsDepots = new LinkedHashMap<>();

    public Fichier() throws IOException {
        chargerAgglo();
    }

    private void chargerAgglo() throws IOException{
        String motCle1 = "MAISONS PAR RUE:";
        String motCle2 = "INTERSECTIONS ENTRE RUES:";
        String motCle3 = "POINTS DE COLLECTE:";
        String motCle4 = "DEPOT:";
        boolean lecture1 = false;
        boolean lecture2 = false;
        boolean lecture3 = false;
        boolean lecture4 = false;

        String line;

        BufferedReader br = new BufferedReader(new FileReader(nomFichier));

        while ((line = br.readLine()) != null) {
            if (line.contains(motCle1)) {
                lecture1 = true;
                continue;
            }

            if (line.contains(motCle2)) {
                lecture1 = false;
            }

            if  (line.contains(motCle2)) {
                lecture2 = true;
                continue;
            }

            if (line.contains(motCle3)) {
                lecture2 = false;
            }

            if (line.contains(motCle3)) {
                lecture3 = true;
            }

            if (line.contains(motCle4)) {
                lecture3 = false;
            }

            if (line.contains(motCle4)) {
                lecture4 = true;
            }

            if (lecture1) {
                String[] parties = line.split(";");

                String rues = parties[0].trim();
                String nb_maison = parties[1].trim();
                String longueur = parties[2].trim();

                listeRues.putIfAbsent(rues, new LinkedHashSet<>());
                listeRues.get(rues).add(nb_maison);
                listeRues.get(rues).add(longueur);
            }

            if (lecture2) {
                String[] parties = line.split(";");

                String intersection = parties[0].trim();
                String nb_maison = parties[1].trim();
                String longueur = parties[2].trim();

                listeIntersections.putIfAbsent(intersection, new LinkedHashSet<>());
                listeIntersections.get(intersection).add(nb_maison);
                listeIntersections.get(intersection).add(longueur);
            }

            if (lecture3) {
                String[] parties = line.split(";");

                String p_collecte = parties[0].trim();
                Integer capacite = Integer.parseInt(parties[1].trim());
                String rue = parties[2].trim();
                Integer longueur = Integer.parseInt(parties[3].trim());

                listePointsCollectes.putIfAbsent(p_collecte, new LinkedHashSet<>());
                listePointsCollectes.get(p_collecte).add(capacite.toString());
                listePointsCollectes.get(p_collecte).add(rue);
                listePointsCollectes.get(p_collecte).add(longueur.toString());
            }

            if (lecture4) {
                String[] parties = line.split(";");

                String p_depot = parties[0].trim();
                String rue = parties[1].trim();
                Integer longueur = Integer.parseInt(parties[2].trim());

                listePointsDepots.putIfAbsent(p_depot, new LinkedHashSet<>());
                listePointsDepots.get(p_depot).add(rue);
                listePointsDepots.get(p_depot).add(longueur.toString());
            }
        }
    }

    public void afficherRues(){
        for (Map.Entry<String, Set<String>> entry : listeRues.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("======================================");
    }

    public void afficherIntersections(){
        for (Map.Entry<String, Set<String>> entry : listeIntersections.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("======================================");
    }
    public void afficherPointsCollectes(){
        for (Map.Entry<String, Set<String>> entry : listePointsCollectes.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("======================================");
    }
    public void afficherPointsDepots(){
        for (Map.Entry<String, Set<String>> entry : listePointsDepots.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public Map<String, Set<String>> getListeRues(){
        return listeRues;
    }

    public Map<String, Set<String>> getListeIntersections(){
        return listeIntersections;
    }

    public Map<String, Set<String>> getListePointsCollectes(){
        return listePointsCollectes;
    }
    
    public Map<String, Set<String>> getListePointsDepots(){
        return listePointsDepots;
    }


}
