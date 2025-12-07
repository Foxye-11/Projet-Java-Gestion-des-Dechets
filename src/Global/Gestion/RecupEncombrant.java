package Global.Gestion;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Rue;
import Global.Architecture.Sommet.Sommets;
import Global.Entite.Encombrant;
import Global.Exploration.AlgorithmeExplo;


import java.io.IOException;
import java.util.*;


import java.util.LinkedList;
import java.util.List;


public class RecupEncombrant {
    public static List<Arc> recupEncombrant(List<Encombrant> encombrant, String pointDeDepot,
                                     Map<String, Sommets> mapSommets, Map<String, Arc> mapArcs) {
        //Initialisation
        List<Arc> chemin_total = new LinkedList<>();
        Arc[] localisations = new Arc[encombrant.size()];
        for (int i = 0; i < encombrant.size(); i++) {
            localisations[i] = encombrant.get(i).getLocalisation();
        }
        //Définition de l'origine
        String origineDijkstra = pointDeDepot;
        //Succession de Dijkstra
        while (!encombrant.isEmpty()) {
            //Dijkstra
            List<Arc> chemin = AlgorithmeExplo.dijkstraMultiArc(origineDijkstra, localisations ,mapSommets, mapArcs);
            //Actualisation de la position pour pouvoir enchainer avec un autre Dijkstra
            for (int i = 0; i < encombrant.size(); i++) {
                if (chemin.contains(encombrant.get(i).getLocalisation())) {
                    origineDijkstra = encombrant.get(i).getLocalisation().getSommet2().getNom();
                    encombrant.remove(encombrant.get(i));
                    break;
                }
            }
            //Ajout du nouveau chemin à l'ensemble
            if (!chemin.isEmpty()) {
                chemin_total.addAll(chemin);
            }else throw new NullPointerException("Chemin de la requête non trouvé");
        }
        //Retour au dépot
        List <Arc> chemin_fermeture = new LinkedList<>();
        chemin_fermeture = AlgorithmeExplo.dijkstra(origineDijkstra,pointDeDepot,mapSommets,mapArcs);
        if (chemin_fermeture.isEmpty()) throw new NullPointerException("Chemin de fermeture non trouvé");
        chemin_total.addAll(chemin_fermeture);
        //Retour du chemin total
        return chemin_total;
    }

    public static List<Arc> Recup1Encombrant(Encombrant encombrant, String pointDeDepot, Map<String,
            Sommets> mapSommets, Map<String, Arc> mapArcs) {
        //Initialisation
        List<Arc> chemin_total = new LinkedList<>();

        //Définition de l'origine
        String origineDijkstra = pointDeDepot;

        //Dijkstra
        List<Arc> chemin = AlgorithmeExplo.dijkstraArc(origineDijkstra, encombrant.getLocalisation(),
                mapSommets, mapArcs);

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
}
