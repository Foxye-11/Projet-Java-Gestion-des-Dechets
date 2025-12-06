package Global.Gestion;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Sommet.PointDeCollecte;
import Global.Exploration.BFS;

import java.util.LinkedList;
import java.util.List;

public class RecupPointCollecte {
    public List<Arc> RecupPointsCollectes(List<PointDeCollecte> pointsCollecte, String pointDeDepot, Fichier mapVille) {
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
            List<Arc> chemin = BFS.bfsMultiArcs(origineBFS, localisations ,mapVille.getListeSommets(), mapVille.getListeArcs());
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
        chemin_fermeture = BFS.dfs(origineBFS,pointDeDepot,mapVille.getListeSommets(),mapVille.getListeArcs());
        chemin_total.addAll(chemin_fermeture);
        //Retour du chemin total
        return chemin_total;
    }
}
