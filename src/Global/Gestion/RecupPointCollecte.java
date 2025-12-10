package Global.Gestion;

import Global.Architecture.Arc;
import Global.Architecture.Sommet.PointDeCollecte;
import Global.Architecture.Sommet.Sommets;
import Global.Exploration.AlgorithmeExplo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecupPointCollecte {
    public static List<Arc> recupPointsCollectes(List<PointDeCollecte> pointsCollecte,
                                          String pointDeDepot, Map<String, Sommets> mapSommets, Map<String, Arc> mapArcs) {
        //Initialisation
        List<Arc> chemin_total = new LinkedList<>();
        Arc[] localisations = new Arc[pointsCollecte.size()];
        for (int i = 0; i < pointsCollecte.size(); i++) {
            localisations[i] = pointsCollecte.get(i).getLocalisation();
        }
        //Définition de l'origine
        String origineBFS = pointDeDepot;
        //Succession de BFS
        while (!pointsCollecte.isEmpty()) {
            //BFS
            List<Arc> chemin = AlgorithmeExplo.bfsMultiArcs(origineBFS, localisations ,mapSommets, mapArcs);
            //Actualisation de la position pour pouvoir enchainer avec un autre BFS
            for (int i = 0; i < pointsCollecte.size(); i++) {
                if (chemin.contains(pointsCollecte.get(i).getLocalisation())) {
                    origineBFS = pointsCollecte.get(i).getLocalisation().getSommet2().getNom();
                    pointsCollecte.remove(pointsCollecte.get(i));
                    break;
                }
            }
            //Ajout du nouveau chemin à l'ensemble
            if (!chemin.isEmpty()) {
                chemin_total.addAll(chemin);
            }
        }
        //Retour au dépot
        List <Arc> chemin_fermeture = new LinkedList<>();
        chemin_fermeture = AlgorithmeExplo.dfs(origineBFS,pointDeDepot,mapSommets, mapArcs);
        chemin_total.addAll(chemin_fermeture);
        //Retour du chemin total
        return chemin_total;
    }
}
