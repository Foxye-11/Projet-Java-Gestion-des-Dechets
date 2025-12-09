package Global;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Quartier;
import Global.Architecture.Rue;
import Global.Architecture.Sommet.PointDeDepot;
import Global.Architecture.Sommet.Sommets;
import Global.Entite.Encombrant;
import Global.Gestion.RecupEncombrant;
import Global.Graphique.GraphiqueFenetre;
import Global.Graphique.GraphismeControle;
import Global.Planification.Calendrier;
import Global.Planification.Planifier;
import Global.Gestion.RecupPoubelle;
import Global.Planification.Tournee;

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

    public static void colorierQuartiers(Map<Integer, Quartier> quartiers) {
        // Trier les quartiers par degré décroissant
        List<Quartier> liste = new ArrayList<>(quartiers.values());
        liste.sort((q1, q2) -> Integer.compare(degreQuartier(q2, quartiers), degreQuartier(q1, quartiers)));

        int couleur = 1;

        for (Quartier q : liste) {
            if (q.getCouleur() == -1) {
                q.setCouleur(couleur);

                // Colorier les quartiers non adjacents avec la même couleur
                for (Quartier autre : liste) {
                    if (autre.getCouleur() == -1 && !quartiersAdjacents(q, autre)) {
                        autre.setCouleur(couleur);
                    }
                }

                couleur++;
            }
        }
    }

    // Calcul du degré d’un quartier
    private static int degreQuartier(Quartier q, Map<Integer, Quartier> quartiers) {
        int degre = 0;
        for (Quartier autre : quartiers.values()) {
            if (!autre.equals(q) && quartiersAdjacents(q, autre)) {
                degre++;
            }
        }
        return degre;
    }

    // Vérifie si deux quartiers sont adjacents (au moins un arc entre eux)
    private static boolean quartiersAdjacents(Quartier q1, Quartier q2) {
        for (Arc arc : q1.getArcs().values()) {
            if (arc.getSommet1().getQuartier() == q2.getIdQuartier() ||
                    arc.getSommet2().getQuartier() == q2.getIdQuartier()) {
                return true;
            }
        }
        return false;
    }


    // Entrer avec blindage
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
    public static List<DayOfWeek> choisirJourAutorise(String type, List<DayOfWeek> joursInterdits){
        Scanner sc = new Scanner(System.in);
        List<DayOfWeek> joursAutorises = new ArrayList<>();
        boolean saisieValide = false;

        while (!saisieValide) {
            System.out.println("Entrez les jours autorisés pour " + type + " (séparés par des virgules, ex: MONDAY,THURSDAY).");
            if (joursInterdits != null){System.out.println("Jours interdits d’office : " + joursInterdits);}

            String saisie = sc.nextLine().trim().toUpperCase();
            String[] jours = saisie.split(",");
            joursAutorises.clear();
            saisieValide = true;

            for (String j : jours) {
                try {
                    DayOfWeek jour = DayOfWeek.valueOf(j);
                    // Vérifie si le jour est interdit
                    if (joursInterdits.contains(jour)) {
                        System.out.println("Erreur : " + jour + " est interdit d’office.");
                        saisieValide = false;
                        break;
                    }

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
    public static int choisirQuartier(){
        Scanner sc = new Scanner(System.in);
        int quartier = -1;
        boolean saisieValide = false;

        while (!saisieValide) {
            System.out.print("Choisis un quartier 1 à 3 ou 0 si version global : ");
            if (sc.hasNextInt()) {
                quartier = sc.nextInt();

                if (quartier >= 0 && quartier <= 3) {
                    saisieValide = true;
                } else {
                    System.out.println("Choix invalide, veuillez entrer un nombre entre 0 et 3.");
                }
            } else {
                System.out.println("Saisie incorrecte, veuillez entrer un nombre.");
            }
        }
        return quartier;
    }
    public static int choisirHOX(){
        Scanner sc = new Scanner(System.in);
        int choix = -1;
        boolean saisieValide = false;

        while (!saisieValide) {
            System.out.print("Selectionner entre 1 et 3 : " +
                    "\n1. Le graphe est non orienté" +
                    "\n2. Le graphe est orienté" +
                    "\n3. Le graphe est orienté et non orienté\n");
            if (sc.hasNextInt()) {
                choix = sc.nextInt();

                if (choix >= 1 && choix <= 3) {
                    saisieValide = true;
                } else {
                    System.out.println("Choix invalide, veuillez entrer un nombre entre 1 et 3.");
                }
            } else {
                System.out.println("Saisie incorrecte, veuillez entrer un nombre.");
            }
        }
        return choix;
    }




    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ******************************************* Boucle principale ******************************************* //
        boolean end = false;
        while (!end) {

            // choix de la ville
            System.out.println("Bienvenue dans le programme");
            System.out.println("Veuillez choisir votre ville :"); /// "MontBrunlesBainsv2.txt" "LesAmandiers"
            String fileName = sc.nextLine() + ".txt";

            try {
                // Chargement de la ville
                int HOX = choisirHOX();
                Fichier fichier = new Fichier(fileName, HOX);
                System.out.println("Chargement de la ville effectué");
                fichier.afficherDonnees();

                // Recuperation des sites de sommets & arcs
                Map<String, Sommets> listeSommets = fichier.getListeSommets();
                Map<String, Arc> listeArcs = fichier.getListeArcs();

                // Creation du graphe
                Map<String, Set<GraphismeControle.Arc>> graphe = construireGraphe(fichier);
                new GraphiqueFenetre(graphe);

                // -------------------------------------- Repartition par quartier -------------------------------------- //
                // Création des quartiers
                Map<Integer, Quartier> quartiers = new HashMap<>();

                // Initialisation des sommets
                for (Sommets s : listeSommets.values()) {
                    quartiers.putIfAbsent(s.getQuartier(), new Quartier(s.getQuartier()));
                    quartiers.get(s.getQuartier()).addSommet(s);
                }

                // Initialisation des arcs
                for (Map.Entry<String, Arc> entry : listeArcs.entrySet()) {
                    Arc a = entry.getValue();
                    quartiers.putIfAbsent(a.getQuartier(), new Quartier(a.getQuartier()));
                    quartiers.get(a.getQuartier()).addArc(entry.getKey(), a);
                }

                // Coloration des quartiers
                colorierQuartiers(quartiers);

                // Affichage
                for (Quartier q : quartiers.values()) {
                    q.afficherQuartier();
                }

                // -------------------------------------- Calcul de l'itineraire -------------------------------------- //

                // Récupération du dépôt principal
                PointDeDepot depotPrincipal = fichier.getListePointsDepots().get("Dépôt principal");
                //PointDeDepot pd = new PointDeDepot("Dépôt principal", fichier.getListeArcs().get(0));

                // Chemin sans consideration des quartiers
                List <Arc> cheminPoubelleGlobal = RecupPoubelle.recupPoubelle(depotPrincipal, listeSommets, listeArcs);

                // Stocker les chemins par quartier
                Map<Integer, List<Arc>> cheminsPoubelleQuartier = new HashMap<>();

                // Boucle sur tous les quartiers
                for (Map.Entry<Integer, Quartier> entry : quartiers.entrySet()) {
                    int numQuartier = entry.getKey();
                    Quartier quartier = entry.getValue();

                    List<Arc> chemin = RecupPoubelle.recupPoubelleQuartier(depotPrincipal, listeSommets, listeArcs, quartier);
                    cheminsPoubelleQuartier.put(numQuartier, chemin);
                    System.out.println("Chemin pour Quartier " + numQuartier + " : " + chemin);
                }



                // -------------------------------------- Creation du planning -------------------------------------- //

                String[] typesDechets = {"OM", "Recyclable", "Verre", "Dechets Organiques"};
                Map<String, Double> frequence = new HashMap<>();
                Map<String, List<DayOfWeek>> joursParTypeGlobal = new HashMap<>();

                // Stocker les jours par type pour chaque quartier
                Map<Integer, Map<String, List<DayOfWeek>>> joursParTypeQuartier = new HashMap<>();

                // Boucle sur tous les quartiers pour initialiser leur Map
                for (Map.Entry<Integer, Quartier> entry : quartiers.entrySet()) {
                    int numQuartier = entry.getKey();
                    joursParTypeQuartier.put(numQuartier, new HashMap<>());
                }


                System.out.println("\n********************* Création du planning *********************\n");

                // --- Saisie de l'année --- //
                int annee = -1;
                boolean saisieValide = false;

                while (!saisieValide) {
                    System.out.println("Saisir l'année du calendrier : ");

                    if (sc.hasNextInt()) {
                        annee = sc.nextInt();

                        if (annee >= 2020 && annee <= 2050) {
                            saisieValide = true; // sortie de la boucle
                        } else {
                            System.out.println("Année invalide, veuillez entrer une année entre 2020 et 2050.");
                        }
                    } else {
                        System.out.println("Saisie incorrecte, veuillez entrer un nombre.");
                        sc.next(); // consommer la mauvaise saisie
                    }
                }

                // --- Configuration des types de déchets --- //
                for (String type : typesDechets) {
                    System.out.println("\n--- Configuration pour " + type + " ---");

                    // --- Choix de la fréquence --- //
                    double freq = choisirFrequance();
                    frequence.put(type, freq);

                    // --- Choix des jours autorisés (global) --- //
                    System.out.println("\n Choix pour la version global : \n");
                    List<DayOfWeek> joursAutorisesGlobal = choisirJourAutorise(type, null);
                    joursParTypeGlobal.put(type, joursAutorisesGlobal);


                    // --- Choix des jours autorisés par couleur --- //
                    // On regroupe les quartiers par couleur
                    Map<Integer, List<Integer>> quartiersParCouleur = new HashMap<>();
                    for (Quartier q : quartiers.values()) {
                        quartiersParCouleur.computeIfAbsent(q.getCouleur(), c -> new ArrayList<>()).add(q.getIdQuartier());
                    }


                    // Liste cumulative des jours interdits (commence vide)
                    List<DayOfWeek> joursInterditsCumul = new ArrayList<>();

                    // Pour chaque couleur, demander une fois les jours autorisés
                    for (Map.Entry<Integer, List<Integer>> entry : quartiersParCouleur.entrySet()) {
                        int couleur = entry.getKey();
                        List<Integer> quartiersDeCetteCouleur = entry.getValue();

                        System.out.println("\nChoix des jours pour la couleur " + couleur + " (quartiers " + quartiersDeCetteCouleur + ")");
                        List<DayOfWeek> joursAutorisesQuartier = choisirJourAutorise(type, joursInterditsCumul);
                        joursInterditsCumul.addAll(joursAutorisesQuartier);

                        // Assigner les jours choisis à tous les quartiers de cette couleur
                        for (int numQuartier : quartiersDeCetteCouleur) {
                            joursParTypeQuartier.get(numQuartier).put(type, joursAutorisesQuartier);
                        }
                    }
                }


                // --- Création des plannings par quartier --- //
                Map<Integer, Calendrier> calendriersQuartier = new HashMap<>();
                Map<Integer, Planifier> planifiersQuartier = new HashMap<>();


                for (Map.Entry<Integer, Quartier> entry : quartiers.entrySet()) {
                    int numQuartier = entry.getKey();
                    Map<String, List<DayOfWeek>> joursQuartier = joursParTypeQuartier.get(numQuartier);

                    Planifier planifierQuartier = new Planifier(joursQuartier, frequence);
                    Calendrier calendrierQuartier = planifierQuartier.creerPlanning(annee, null);

                    planifiersQuartier.put(numQuartier, planifierQuartier);
                    calendriersQuartier.put(numQuartier, calendrierQuartier);
                }

                // --- Création du planning global --- //
                Planifier planifierGlobal = new Planifier(joursParTypeGlobal, frequence);
                Calendrier calendrierGlobal = planifierGlobal.creerPlanning(annee, null);



                // ******************************************* Choix du Thème ******************************************* //

                List<DayOfWeek> joursAutorises = null;
                LocalDate date = null;
                String type = null;
                String type_tournee = null;
                int quartier = -1;
                List<Arc> chemin = null;
                List<Arc> chemin1 = null;
                List<Arc> chemin2 = null;
                List<Arc> chemin3 = null;

                boolean end2 = false;
                while (!end2) {
                    System.out.println("\n=== Menu des choix ===");
                    System.out.println("1. Afficher la calendrier");
                    System.out.println("2. Ajout d'une collecte auprès de chaque habitation");
                    System.out.println("3. Ajout d'une collecte d'encombrant ");
                    System.out.println("4. Ajout d'une collecte des points de collectes");
                    System.out.println("5. Obtenir le chemin d'une tournee");
                    System.out.println("0. Quitter");
                    System.out.print("Votre choix : ");

                    if (sc.hasNextInt()) {
                        int choix = sc.nextInt();

                        switch (choix) {
                            case 1:
                                System.out.println("\n* --- Affichage du calendrier --- *\n");
                                quartier = choisirQuartier();
                                if (quartier==0) {
                                    calendrierGlobal.getContentPane().removeAll();
                                    calendrierGlobal.affichage(annee);
                                    calendrierGlobal.setVisible(true);
                                }
                                else {
                                    for (Map.Entry<Integer, Calendrier> entry : calendriersQuartier.entrySet()) {
                                        int numQuartier = entry.getKey();
                                        Calendrier calendrier = entry.getValue();

                                        if (quartier == numQuartier) {
                                            calendrier.getContentPane().removeAll();
                                            calendrier.affichage(annee);
                                            calendrier.setVisible(true);
                                        }
                                    }
                                }
                                break;
                            case 2:
                                System.out.println("\n* --- Ajout d'une collecte auprès de chaque habitation --- *\n");
                                quartier = choisirQuartier();
                                type = choisirTypeDechet();
                                joursAutorises = choisirJourAutorise(type, null);
                                date = choisirDate(annee);
                                type_tournee = "Habitations";
                                if (quartier==0) chemin = null;
                                else if (quartier==1) chemin = null;
                                else if (quartier==2) chemin = null;
                                else if (quartier==3) chemin = null;

                                break;
                            case 3:
                                System.out.println("* --- Ajout d'une collecte d'encombrant --- *");
                                quartier = choisirQuartier();
                                joursAutorises = choisirJourAutorise(null, null);
                                date = choisirDate(annee);
                                type_tournee = "Encombrants";

                                System.out.println("Combien y'a t'il d'encombrants ?");
                                int nombre = sc.nextInt();
                                List <Encombrant> encombrants = new LinkedList<>();
                                for (int i = 0; i < nombre; i++) {
                                    System.out.println("Entrez le numéro de domicile :");
                                    int domicile = sc.nextInt();
                                    System.out.println("Entrez la rue du domicile : ");
                                    String rue_temp = sc.nextLine();
                                    Rue rue = fichier.getListeRues().get(rue_temp);

                                    Encombrant newEncombrant = new Encombrant(rue, domicile);
                                    encombrants.add(newEncombrant);
                                }
                                //RecupEncombrant re = new RecupEncombrant();
                                //chemin = re.recupEncombrant(encombrants, fichier.getListePointsDepots().get(0).getNom(), fichier.getListeSommets(), fichier.getListeArcs());

                                break;
                            case 4:
                                System.out.println("* --- Ajout d'une collecte des points de collectes --- *");
                                quartier = choisirQuartier();
                                type = choisirTypeDechet();
                                joursAutorises = choisirJourAutorise(type, null);
                                date = choisirDate(annee);
                                type_tournee = "Points de collecte";
                                chemin = null;
                                break;
                                /*
                                      Fichier fichier = new Fichier("ezfherhg"); throw IOException;
        List<PointDeCollecte> pointsCollecte = new LinkedList<>();
        for (int i =0; i<fichier.getListePointsCollectes().size();i++) {
            pointsCollecte.add(fichier.getListePointsCollectes().get(i));
        }

        List<Arc> cheminPointsCollecte = recupPointsCollecte (pointsCollecte,fichier.getListePointsDepots().get(0).getNom(),fichier.getListeSommets(),fichier.getListeArcs());
                                 */

                            case 5:
                                try {
                                    System.out.println("* --- Recuperation d'un chemin d'une tournee via sa date --- *");
                                    System.out.println("Entrez le mois (1-12) : ");
                                    int mois = sc.nextInt();
                                    System.out.println("Entrez le jour : ");
                                    int jour = sc.nextInt();
                                    sc.nextLine(); // consommer retour ligne

                                    date = LocalDate.of(annee, mois, jour);

                                    Tournee tournee = calendrierGlobal.getTournee(date);

                                    if (tournee != null) {
                                        List<Arc> arcs = tournee.getListe();
                                        if (arcs.isEmpty()) {
                                            System.out.println("Aucun arc dans la tournee du " + date);
                                        } else {
                                            System.out.println("Liste des arcs pour la tournee du " + date + " :");
                                            for (Arc arc : arcs) {
                                                System.out.println(" - " + arc);
                                            }
                                        }
                                    } else {
                                        System.out.println("Aucune tournee trouvee pour la date " + date);
                                    }
                                } catch (Exception e) {
                                    System.out.println("Date invalide !");
                                    sc.nextLine(); // consommer mauvaise saisie
                                }
                                break;
                            case 0:
                                System.out.println("Fin du programme.");
                                end2 = true;
                                break;
                            default:
                                System.out.println("Choix invalide, veuillez entrer 0, 1, 2, 3, 4.");
                        }


                        if (choix >= 2 && choix <= 4) {
                            if (quartier == 0) { // Cas global
                                planifierGlobal.addTournee(date, type, type_tournee, calendrierGlobal, joursAutorises, chemin);
                            } else { // Cas par quartier (boucle générique)
                                for (Map.Entry<Integer, Planifier> entry : planifiersQuartier.entrySet()) {
                                    int numQuartier = entry.getKey();
                                    Planifier planifierQuartier = entry.getValue();
                                    Calendrier calendrierQuartier = calendriersQuartier.get(numQuartier);

                                    if (quartier == numQuartier && planifierQuartier != null && calendrierQuartier != null) {
                                        planifierQuartier.addTournee(date, type, type_tournee, calendrierQuartier, joursAutorises, chemin);
                                        break; // Sorti dès qu'on a trouvé le bon quartier
                                    }
                                }
                            }
                        }

                    } else {
                        System.out.println("Erreur de saisie, veuillez entrer un nombre !");
                        sc.next(); // consommer mauvaise saisie
                    }
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
