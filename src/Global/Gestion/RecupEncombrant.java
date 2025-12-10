package Global.Gestion;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Sommet.Sommets;
import Global.Entite.Encombrant;
import Global.Exploration.AlgorithmeExplo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static Global.Main.choisirHOX;


public class RecupEncombrant {

    public static List<Arc> recupEncombrant(List<Encombrant> encombrants, String pointDeDepot,
                                            Map<String, Sommets> mapSommets, Map<String, Arc> mapArcs) {

        List<Arc> chemin_total = new LinkedList<>();
        String origineDijkstra = pointDeDepot;

        while (!encombrants.isEmpty()) {

            // On reconstruit les localisations ACTUELLES
            Arc[] localisations = new Arc[encombrants.size()];
            for (int i = 0; i < encombrants.size(); i++) {
                localisations[i] = encombrants.get(i).getLocalisation();
            }

            // Dijkstra multi-arcs
            List<Arc> chemin = AlgorithmeExplo.dijkstraMultiArc(
                    origineDijkstra, localisations, mapSommets, mapArcs);

            if (chemin == null || chemin.isEmpty()) {
                throw new NullPointerException("Chemin de la requête non trouvé");
            }

            // Le dernier arc du chemin mène à l’encombrant atteint
            Arc dernier = chemin.get(chemin.size() - 1);

            // Trouver l’encombrant correspondant
            Encombrant cible = null;
            for (Encombrant e : encombrants) {
                if (e.getLocalisation().getSommet2().equals(dernier.getSommet2())) {
                    cible = e;
                    break;
                }
            }


            if (cible == null) {
                throw new IllegalStateException("Impossible d'associer le chemin à un encombrant.");
            }

            // Mise à jour de la position
            origineDijkstra = dernier.getSommet2().getNom();

            // On retire l’encombrant atteint
            encombrants.remove(cible);

            // Ajout du chemin trouvé
            chemin_total.addAll(chemin);
        }

        // Retour au dépôt
        List<Arc> retour = AlgorithmeExplo.dijkstra(origineDijkstra, pointDeDepot, mapSommets, mapArcs);

        if (retour == null || retour.isEmpty()) {
            throw new NullPointerException("Chemin de fermeture non trouvé");
        }

        chemin_total.addAll(retour);

        return chemin_total;
    }


    public static List<Arc> recup1Encombrant(Encombrant encombrant, String pointDeDepot, Map<String,
            Sommets> mapSommets, Map<String, Arc> mapArcs) {
        //Initialisation
        List<Arc> chemin_total = new LinkedList<>();

        //Définition de l'origine
        String origineDijkstra = pointDeDepot;

        //Dijkstra
        List<Arc> chemin = AlgorithmeExplo.dijkstraArc(origineDijkstra, encombrant.getLocalisation(),
                mapSommets, mapArcs);

        if (!chemin.contains(encombrant.getLocalisation())) {
            chemin.add(encombrant.getLocalisation());
        }
        //Retour au dépot
        List <Arc> chemin_fermeture = new LinkedList<>();
        if (chemin.isEmpty()) {
            throw new NullPointerException("Chemin d'aller Encombrant1 non trouvé");
        }

        chemin_total.addAll(chemin);
        origineDijkstra = encombrant.getLocalisation().getSommet2().getNom();
        chemin_fermeture = AlgorithmeExplo.dijkstra(origineDijkstra,pointDeDepot,mapSommets,mapArcs);

        if (chemin_fermeture.isEmpty()) {
            throw new NullPointerException("chemin du retour Encombrant1 non trouvé");
        }
        chemin_total.addAll(chemin_fermeture);
        //Retour du chemin total
        return chemin_total;
    }

    public static void main(String[] args) throws IOException {
        int HOX = choisirHOX();
        Fichier fichier = new Fichier ("Die.txt", HOX);

        //1 encombrant
        Encombrant encombrantNEW = new Encombrant(fichier.getListeArcs().get("I-N_direct"));
        List<Arc> chemin1 = recup1Encombrant(encombrantNEW, fichier.getListePointsDepots().get("Dépôt principal").getLocalisation().getSommet1().getNom(),fichier.getListeSommets(), fichier.getListeArcs());
        System.out.println(chemin1.getFirst().getSommet1().getNom());
        for (Arc arc : chemin1) {
            System.out.println(arc.getSommet2().getNom());
        }
        //plusieurs encombrants
        System.out.println("Plusieurs encombrants");
        List <Encombrant> encombrants = new LinkedList<>();
        Encombrant encombrantNEW2 = new Encombrant(fichier.getListeArcs().get("S-Q_oppose"));
        encombrants.add(encombrantNEW);
        encombrants.add(encombrantNEW2);

        List <Arc> chemin2 = recupEncombrant(encombrants, fichier.getListePointsDepots().get("Dépôt principal").getLocalisation().getSommet1().getNom(),fichier.getListeSommets(), fichier.getListeArcs());
        System.out.println(chemin2.getFirst().getSommet1().getNom());
        for (Arc arc : chemin2) {
            System.out.println(arc.getSommet2().getNom());
        }
    }
}
