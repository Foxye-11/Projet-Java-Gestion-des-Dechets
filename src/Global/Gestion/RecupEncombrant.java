package Global.Gestion;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Sommet.PointDeDepot;
import Global.Entite.Encombrant;
import Global.Exploration.BFS;


import java.util.LinkedList;
import java.util.List;


import java.util.LinkedList;
import java.util.List;



public class RecupEncombrant {
    public List<Arc> RecupEncombrant(List<Encombrant> encombrant, String pointDeDepot, Fichier mapVille) {
        //Initialisation 
        List<Arc> chemin_total = new LinkedList<>();
        Arc[] localisations = new Arc[encombrant.size()];
        for (int i = 0; i < encombrant.size(); i++) {
            localisations[i] = encombrant.get(i).getLocalisation();
        }
        //Définition de l'origine
        String origineBFS = pointDeDepot;
        //Succession de BFS
        while (!encombrant.isEmpty()) {
            //BFS
            List<Arc> chemin = BFS.bfsMultiArcs(origineBFS, localisations ,mapVille.getListeSommets(), mapVille.getListeArcs());
            //Actualisation de la position pour pouvoir enchainer avec un autre BFS
            for (int i = 0; i < encombrant.size(); i++) {
                if (chemin.contains(encombrant.get(i).getLocalisation())) {
                    origineBFS = encombrant.get(i).getLocalisation().getSommet2().getNom();
                    encombrant.remove(encombrant.get(i));
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
