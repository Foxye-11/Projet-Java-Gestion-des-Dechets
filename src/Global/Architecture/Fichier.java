package Global.Architecture;

import Global.Architecture.Sommet.PointDeCollecte;
import Global.Architecture.Sommet.PointDeDepot;
import Global.Architecture.Sommet.Sommets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Fichier {

    private String nomFichier ;

    public Map<String, Rue> listeRues = new LinkedHashMap<>();
    public Map<String, PointDeCollecte> listePointsCollectes = new LinkedHashMap<>();
    public Map<String, PointDeDepot> listePointsDepots = new LinkedHashMap<>();
    public Map<String, Sommets> listeSommets = new LinkedHashMap<>();
    public Map<String, Arc> listeArcs = new LinkedHashMap<>();

    public Fichier(String nomFichier) throws IOException {
        this.nomFichier = nomFichier;
        chargerAgglo();
    }

    private void chargerAgglo() throws IOException {
        String motCle1 = "MAISONS PAR RUE:";
        String motCle2 = "CARREFOURS";
        String motCle3 = "ARCS";
        String motCle4 = "POINTS DE COLLECTE:";
        String motCle5 = "DEPOT:";

        boolean lectureRues = false;
        boolean lectureSommets = false;
        boolean lectureArcs = false;
        boolean lectureCollecte = false;
        boolean lectureDepot = false;

        BufferedReader br = new BufferedReader(new FileReader(nomFichier));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String upper = line.toUpperCase();

            // trouve la catégorie dans laquelle on est
            if (upper.startsWith(motCle1)) {
                lectureRues = true;
                lectureSommets = lectureArcs = lectureCollecte = lectureDepot = false;
                continue;
            }
            if (upper.startsWith(motCle2)) {
                lectureSommets = true;
                lectureRues = lectureArcs = lectureCollecte = lectureDepot = false;
                continue;
            }
            if (upper.startsWith(motCle3)) {
                lectureArcs = true;
                lectureRues = lectureSommets = lectureCollecte = lectureDepot = false;
                continue;
            }
            if (upper.startsWith(motCle4)) {
                lectureCollecte = true;
                lectureSommets = lectureArcs = lectureRues = lectureDepot = false;
                continue;
            }
            if (upper.startsWith(motCle5)) {
                lectureDepot = true;
                lectureSommets = lectureArcs = lectureCollecte = lectureRues = false;
                continue;
            }


            // Lit et creer les objets selon la categorie dans laquelle on est
            if (lectureRues) {
                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String rue = parts[0].trim();
                int nbMaisons = Integer.parseInt(parts[1].trim());
                float longueur = Float.parseFloat(parts[2].trim());
                LinkedList<Arc> ensembleRue = new LinkedList<>();

                Rue newRue = new Rue(rue, ensembleRue, nbMaisons, longueur);
                listeRues.putIfAbsent(rue, newRue);
                continue;
            }


            if (lectureSommets) {
                String[] parts = line.split(";");
                if (parts.length < 2) continue;

                String sommet = parts[0].trim();
                String coords = parts[1].trim();
                Set<String> rues = new LinkedHashSet<>();
                List<Arc> ArcsSortants = new ArrayList<>();
                List<Arc> arcsEntrant = new ArrayList<>();
                for (int i = 2; i < parts.length; i++)
                    rues.add(parts[i].trim());

                Sommets newSommets = new Sommets(ArcsSortants, arcsEntrant, rues, sommet);
                listeSommets.putIfAbsent(sommet, newSommets);
                continue;
            }


            if (lectureArcs) {
                String[] parts = line.split(";");
                if (parts.length < 7) continue;

                String nomArc = parts[0].trim();
                String[] sommet = nomArc.split("-");
                Sommets sommets1 = listeSommets.get(sommet[0]);
                Sommets sommets2 = listeSommets.get(sommet[1]);

                String nomRue = parts[1].trim();
                int nbMaisons = Integer.parseInt(parts[2].trim());
                float longueur = Float.parseFloat(parts[3].trim());
                String cooX = parts[4].trim();
                String cooY = parts[5].trim();
                int sens =  Integer.parseInt(parts[6].trim());

                Arc newArc = new Arc(nomRue, nbMaisons, longueur, sens, sommets1, sommets2);
                listeArcs.put(nomArc, newArc);

                Rue rue = listeRues.get(nomRue);
                if (rue == null) System.err.println("⚠️ Rue introuvable : '" + nomRue + "' dans l'arc '" + nomArc + "'");
                else rue.addArc(newArc);

                if (sens == 0){ // sens direct
                    sommets1.addArcSortant(newArc);
                    sommets2.addArcEntrant(newArc);
                }
                else if (sens == 1) { // sens opposé
                    sommets1.addArcEntrant(newArc);
                    sommets2.addArcSortant(newArc);
                }
                else if (sens == 2) { // bidirectionnel
                    sommets1.addArcEntrant(newArc);
                    sommets1.addArcSortant(newArc);
                    sommets2.addArcEntrant(newArc);
                    sommets2.addArcSortant(newArc);
                }
                continue;
            }


            if (lectureCollecte) {
                String[] parts = line.split(";");
                if (parts.length < 4) continue;

                String nom = parts[0].trim();
                int capacite = Integer.parseInt(parts[1].trim());
                String rue = parts[2].trim();
                float longueur = Float.parseFloat(parts[3].trim());

                PointDeCollecte pointDeCollecte = new PointDeCollecte(rue, nom, capacite);
                listePointsCollectes.putIfAbsent(nom, pointDeCollecte);
                continue;
            }


            if (lectureDepot) {
                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String nom = parts[0].trim();
                String rue = parts[1].trim();
                String longueur = parts[2].trim();

                PointDeDepot pointDeDepot = new PointDeDepot(nom);
                listePointsDepots.putIfAbsent(nom, pointDeDepot);
                continue;
            }

        }
        br.close();
    }

    public void afficherDonnees() {
        System.out.println("=== LISTE DES RUES ===");
        for (Rue rue : listeRues.values()) {
            System.out.println("Rue: " + rue.getNom() +
                    ", Maisons: " + rue.getNbHabitations() +
                    ", Longueur: " + rue.getLongueur());
        }

        System.out.println("\n=== LISTE DES SOMMETS ===");
        for (Sommets sommets : listeSommets.values()) {
            System.out.println("Sommet: " + sommets.getNom() +
                    ", Rues reliées: " + sommets.getRues());
        }

        System.out.println("\n=== LISTE DES ARCS ===");
        for (Arc arc : listeArcs.values()) {
            Sommets sommets1 = arc.getSommet1();
            Sommets sommets2 = arc.getSommet2();
            System.out.println("Arc: " + arc.getNomRue() +
                    ", Longueur: " + arc.getLongueur() +
                    ", Maisons: " + arc.getNbMaisons() +
                    ", Sens: " + arc.getSens() +
                    ", De: " + sommets1.getNom() +
                    " -> " + sommets2.getNom());
        }

        System.out.println("\n=== POINTS DE COLLECTE ===");
        for (PointDeCollecte pc : listePointsCollectes.values()) {
            System.out.println("Point de Collecte: " + pc.getNom() +
                    ", Rue: " + pc.getRue() +
                    ", Capacité: " + pc.getCapacite());
        }

        System.out.println("\n=== POINTS DE DEPOT ===");
        for (PointDeDepot pd : listePointsDepots.values()) {
            System.out.println("Point de Dépôt: " + pd.getNom());
        }
    }
    // getter
    public Map<String, Rue> getListeRues() { return listeRues; }
    public Map<String, Sommets> getListeSommets() { return listeSommets; }
    public Map<String, Arc> getListeArcs() { return listeArcs; }
    public Map<String, PointDeCollecte> getListePointsCollectes() { return listePointsCollectes; }
    public Map<String, PointDeDepot> getListePointsDepots() { return listePointsDepots; }


    public static void main(String[] args) {
        try {
            // Remplace "data.txt" par le chemin réel de ton fichier
            String cheminFichier = "MontBrunLesBainsv2.txt";

            // Création de l'objet Fichier (chargement automatique via le constructeur)
            Fichier fichier = new Fichier(cheminFichier);

            // Appel de la méthode d'affichage
            fichier.afficherDonnees();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
