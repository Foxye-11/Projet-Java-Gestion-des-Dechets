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

    public Fichier(String nomFichier, int HOX) throws IOException {
        this.nomFichier = nomFichier;
        chargerAgglo(HOX);
    }

    private void chargerAgglo(int HOX) throws IOException {
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
                int quartier = Integer.parseInt(parts[4].trim());

                Sommets newSommets = new Sommets(ArcsSortants, arcsEntrant, rues, sommet, quartier);
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
                int quartier = Integer.parseInt(parts[7].trim());

                // Création des arcs selon HOX et sens
                Arc newArc = null; // déclaration en amont
                Arc newArc2 = null;

                if (HOX == 1) { // bidirectionnel
                    newArc = new Arc(nomRue, nbMaisons, longueur, sens, sommets1, sommets2, quartier);
                    newArc2 = new Arc(nomRue, nbMaisons, longueur, sens, sommets2, sommets1, quartier);

                    listeArcs.put(nomArc + "_direct", newArc);
                    listeArcs.put(nomArc + "_oppose", newArc2);

                    sommets1.addArcSortant(newArc);
                    sommets2.addArcEntrant(newArc);

                    sommets2.addArcSortant(newArc2);
                    sommets1.addArcEntrant(newArc2);

                } else if (HOX == 2 || HOX == 3) {
                    if (sens == 0) { // sens direct
                        newArc = new Arc(nomRue, nbMaisons, longueur, sens, sommets1, sommets2, quartier);
                        listeArcs.put(nomArc + "_direct", newArc);

                        sommets1.addArcSortant(newArc);
                        sommets2.addArcEntrant(newArc);

                    } else if (sens == 1) { // sens opposé
                        newArc = new Arc(nomRue, nbMaisons, longueur, sens, sommets2, sommets1, quartier);
                        listeArcs.put(nomArc + "_oppose", newArc);

                        sommets2.addArcSortant(newArc);
                        sommets1.addArcEntrant(newArc);

                    } else if (sens == 2) { // bidirectionnel
                        newArc = new Arc(nomRue, nbMaisons, longueur, sens, sommets1, sommets2, quartier);
                        newArc2 = new Arc(nomRue, nbMaisons, longueur, sens, sommets2, sommets1, quartier);

                        listeArcs.put(nomArc + "_direct", newArc);
                        listeArcs.put(nomArc + "_oppose", newArc2);

                        sommets1.addArcSortant(newArc);
                        sommets2.addArcEntrant(newArc);

                        sommets2.addArcSortant(newArc2);
                        sommets1.addArcEntrant(newArc2);
                    }
                }

                // Association avec la rue
                Rue rue = listeRues.get(nomRue);
                if (rue == null) System.err.println("Rue introuvable : '" + nomRue + "' dans l'arc '" + nomArc + "'");
                else if (newArc != null) rue.addArc(newArc);


                continue;
            }


            if (lectureCollecte) {
                String[] parts = line.split(";");
                if (parts.length < 4) continue;

                String nom = parts[0].trim();
                int capacite = Integer.parseInt(parts[1].trim());
                String arc = parts[2].trim();
                float longueur = Float.parseFloat(parts[3].trim());
                Arc location = listeArcs.get(arc);

                PointDeCollecte pointDeCollecte = new PointDeCollecte(arc, nom, capacite, location);
                listePointsCollectes.putIfAbsent(nom, pointDeCollecte);
                continue;
            }


            if (lectureDepot) {
                String[] parts = line.split(";");
                if (parts.length < 3) continue;

                String nom = parts[0].trim();
                String arc = parts[1].trim();
                String longueur = parts[2].trim();
                Arc location = listeArcs.get(arc);

                PointDeDepot pointDeDepot = new PointDeDepot(nom, location);
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
                    ", Quartier: " + sommets.getQuartier() +   // <-- ajout quartier
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
                    ", Quartier: " + arc.getQuartier() +       // <-- ajout quartier
                    ", De: " + sommets1.getNom() +
                    " (Q" + sommets1.getQuartier() + ")" +     // <-- quartier du sommet1
                    " -> " + sommets2.getNom() +
                    " (Q" + sommets2.getQuartier() + ")");     // <-- quartier du sommet2
        }

        System.out.println("\n=== POINTS DE COLLECTE ===");
        for (PointDeCollecte pc : listePointsCollectes.values()) {
            System.out.println("Point de Collecte: " + pc.getNom() +
                    ", Rue: " + pc.getRue() +
                    ", Capacité: " + pc.getCapacite());
        }

        System.out.println("\n=== POINTS DE DEPOT ===");
        for (PointDeDepot pd : listePointsDepots.values()) {
            Arc a = pd.getLocalisation();
            if (a == null) {
                System.out.println("Point de Depot n'existe pas");
            } else {
                System.out.println("Point de Dépôt: " +
                        a.getSommet1().getNom() + " (Q" + a.getSommet1().getQuartier() + ")" +
                        " -> " +
                        a.getSommet2().getNom() + " (Q" + a.getSommet2().getQuartier() + ")");
            }
        }
    }
    public Map<String, Rue> getListeRues() { return listeRues; }
    public Map<String, Sommets> getListeSommets() { return listeSommets; }
    public Map<String, Arc> getListeArcs() { return listeArcs; }
    public Map<String, PointDeCollecte> getListePointsCollectes() { return listePointsCollectes; }
    public Map<String, PointDeDepot> getListePointsDepots() { return listePointsDepots; }

}
