package Global;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Quartier;
import Global.Architecture.Rue;
import Global.Exploration.AlgorithmeExplo;
import Global.Graphique.GraphiqueFenetre;
import Global.Graphique.GraphismeControle;
import Global.Planification.Calendrier;
import Global.Planification.Planifier;

import java.io.IOException;
import java.time.*;
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


    public static double choisirFrequance(){
        Scanner sc = new Scanner(System.in);
        double freq = -1;
        while (freq < 0) {
            System.out.print("Entrez la fréquence (>=0, ex: 2 ; 1 ; 0,5 ; 0,3) : ");
            if (sc.hasNextDouble()) {
                freq = sc.nextDouble();
                if (freq < 0) {
                    System.out.println("Erreur, la fréquence doit être positive !");
                }
            } else {
                System.out.println("Erreur, veuillez entrer un nombre valide !");
            }
        }
        return freq;
    }


    public static List<DayOfWeek> choisirJourAutorise(String type){
        Scanner sc = new Scanner(System.in);
        List<DayOfWeek> joursAutorises = new ArrayList<>();
        boolean saisieValide = false;

        while (!saisieValide) {
            System.out.println("Entrez les jours autorisés pour " + type + " (séparés par des virgules, ex: MONDAY,THURSDAY) : ");
            String saisie = sc.nextLine().trim().toUpperCase();

            String[] jours = saisie.split(",");
            joursAutorises.clear();
            saisieValide = true;

            for (String j : jours) {
                try {
                    DayOfWeek jour = DayOfWeek.valueOf(j);
                    joursAutorises.add(jour);
                } catch (IllegalArgumentException e) {
                    System.out.println("Erreur, jour invalide : " + j + ". Les jours valides sont MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY.");
                    saisieValide = false;
                    break;
                }
            }
        }
        return joursAutorises;
    }


    public static String choisirTypeDechet(){
        Scanner sc = new Scanner(System.in);

        String[] typesDechets = {"OM", "Recyclable", "Verre", "Dechets Organiques"};

        System.out.println("Choisir le type de déchet à collecter :");
        System.out.println("1. OM");
        System.out.println("2. Recyclable");
        System.out.println("3. Verre");
        System.out.println("4. Déchets Organiques");
        System.out.println("5. Aucun");

        int choix = -1;
        boolean saisieValide = false;

        while (!saisieValide) {
            System.out.print("Votre choix (1-5) : ");
            if (sc.hasNextInt()) {
                choix = sc.nextInt();

                if (choix >= 1 && choix <= 5) {
                    saisieValide = true;
                } else {
                    System.out.println("Choix invalide, veuillez entrer un nombre entre 1 et 5.");
                }
            } else {
                System.out.println("Saisie incorrecte, veuillez entrer un nombre.");
            }
        }
        return typesDechets[choix];
    }


    public static LocalDate choisirDate(int annee){
        Scanner sc = new Scanner(System.in);
        LocalDate dateChoisie = null;
        boolean saisieValide = false;

        while (!saisieValide) {
            try {
                System.out.println("\nSaisir le mois (1-12) : ");
                int mois = sc.nextInt();

                System.out.println("Saisir le jour : ");
                int jour = sc.nextInt();

                // Vérification que la date est valide
                dateChoisie = LocalDate.of(annee, mois, jour);
                saisieValide = true; // si pas d'exception, la date est correcte
            } catch (DateTimeException e) {
                System.out.println("Date invalide, veuillez entrer un jour et un mois valides pour l'année " + annee + ".");
            } catch (InputMismatchException e) {
                System.out.println("Saisie incorrecte, veuillez entrer des nombres.");
            }
        }
        return dateChoisie;
    }

    public static boolean choisirSortie() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String saisie = sc.nextLine().trim().toUpperCase();

            if (saisie.equals("OUI")) {return true;}
            else if (saisie.equals("NON")) {return false;}
            else System.out.println("Saisie invalide ! Veuillez entrer OUI ou NON.");
        }
    }



    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ******************************************* Boucle principale ******************************************* //
        boolean end = false;
        while (!end) {

            // choix de la ville
            System.out.println("Bienvenue dans le programme");
            System.out.println("Veuillez choisir votre ville :"); // "MontBrunlesBainsv2.txt"
            String fileName = sc.nextLine() + ".txt";

            try {
                // Chargement de la ville
                Fichier fichier = new Fichier(fileName);
                System.out.println("Chargement de la ville effectué");
                fichier.afficherDonnees();

                Map<String, Set<GraphismeControle.Arc>> graphe = construireGraphe(fichier);
                new GraphiqueFenetre(graphe);

                // -------------------------------------- Repartition par quartier -------------------------------------- //
                /*
                System.out.print("Combien de quartiers voulez-vous créer ? ");
                int nbQuartiers = sc.nextInt();

                LinkedList<Quartier> quartiers = new LinkedList<>();
                for (int i = 1; i <= nbQuartiers; i++) {
                    System.out.print("Nom du quartier " + i + " : ");
                    String nomQuartier = sc.nextLine();
                    Quartier q = new Quartier(nomQuartier);
                    quartiers.add(q);
                }
                */
                Quartier q1 = new Quartier("Centre");
                Quartier q2 = new Quartier("Nord");
                Quartier q3 = new Quartier("Sud");

                for (Rue r : fichier.getListeRues().values()) {
                    if (r.getNom().contains("A")) q1.addRue(r);
                    else if (r.getNom().contains("B")) q2.addRue(r);
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


                // -------------------------------------- Creation du planning -------------------------------------- //
                String[] typesDechets = {"OM", "Recyclable", "Verre", "Dechets Organiques"};
                Map<String, Double> frequence = new HashMap<>();
                Map<String, List<DayOfWeek>> joursParType = new HashMap<>();

                System.out.println("\n********************* Creation du planning *********************\n");

                // --- Saisit de l'année --- //
                int annee = -1;
                boolean saisieValide = false;

                while (!saisieValide) {
                    System.out.println("Saisir l'année du calendrier : ");

                    if (sc.hasNextInt()) {
                        annee = sc.nextInt();

                        if (annee >= 2020 && annee <= 2050) {
                            saisieValide = true; // sortie de la boucle
                        } else {
                            System.out.println(" Année invalide, veuillez entrer une année entre 2020 et 2050.");
                        }
                    } else {
                        System.out.println("Saisie incorrecte, veuillez entrer un nombre.");
                        sc.next(); // consommer la mauvaise saisie
                    }
                }


                for (String type : typesDechets) {
                    System.out.println("\n--- Configuration pour " + type + " ---");

                    // --- Choix de la fréquence --- //
                    double freq = choisirFrequance();
                    frequence.put(type, freq);

                    // --- Choix des jours autorisés --- //
                    List<DayOfWeek> joursAutorises = choisirJourAutorise(type);
                    joursParType.put(type, joursAutorises);
                }

                Planifier planifier = new Planifier(joursParType, frequence);
                Calendrier calendrier = planifier.creerPlanning(2025);


                // ******************************************* Choix du Thème ******************************************* //

                List<DayOfWeek> joursAutorises;
                LocalDate date;
                String type;

                boolean end2 = false;
                while (!end2) {
                    System.out.println("\n=== Menu des choix ===");
                    System.out.println("1. Afficher la calendrier");
                    System.out.println("2. Ajout d'une collecte auprès de chaque habitation");
                    System.out.println("3. Ajout d'une collecte d'encombrant ");
                    System.out.println("4. Ajout d'une collecte des points de collectes");
                    System.out.println("0. Quitter");
                    System.out.print("Votre choix : ");

                    if (sc.hasNextInt()) {
                        int choix = sc.nextInt();

                        switch (choix) {
                            case 1:
                                System.out.println("\n* --- Affichage du calendrier --- *\n");
                                calendrier.getContentPane().removeAll();
                                calendrier.affichage(annee);
                                calendrier.setVisible(true);
                                break;
                            case 2:
                                System.out.println("\n* --- Ajout d'une collecte auprès de chaque habitation --- *\n");
                                type = choisirTypeDechet();
                                joursAutorises = choisirJourAutorise(type);
                                date = choisirDate(annee);
                                planifier.addTournee(date, type, "collecte", calendrier, joursAutorises);
                                break;
                            case 3:
                                System.out.println("* --- Ajout d'une collecte d'encombrant --- *");
                                joursAutorises = choisirJourAutorise(null);
                                date = choisirDate(annee);
                                planifier.addTournee(date, null, "Encombrants", calendrier, joursAutorises);
                                break;
                            case 4:
                                System.out.println("* --- Ajout d'une collecte des points de collectes --- *");
                                type = choisirTypeDechet();
                                joursAutorises = choisirJourAutorise(type);
                                date = choisirDate(annee);
                                planifier.addTournee(date, type, "Points de collecte", calendrier, joursAutorises);
                                break;
                            case 0:
                                System.out.println("Fin du programme.");
                                end2 = true;
                                break;
                            default:
                                System.out.println("Choix invalide, veuillez entrer 0, 1, 2, 3, 4.");
                        }
                    } else {
                        System.out.println("Erreur de saisie, veuillez entrer un nombre !");
                        sc.next(); // consommer mauvaise saisie
                    }

                    System.out.println("Voulez-vous quitter la selection des choix ? (OUI / NON)");
                    end2 = choisirSortie();
                }
            }
            catch (IOException e) {
                System.err.println("Erreur de chargement du fichier : " + e.getMessage());
            }

            System.out.println("Voulez-vous quitter le programme ? (OUI / NON)");
            end = choisirSortie();
        }
        sc.close();
    }

}
