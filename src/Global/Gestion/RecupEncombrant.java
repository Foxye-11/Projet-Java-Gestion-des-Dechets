package Global.Gestion;

import Global.Architecture.Arc;
import Global.Architecture.Fichier;
import Global.Architecture.Sommet.PointDeDepot;
import Global.Architecture.Sommet.Sommets;
import Global.Entite.Encombrant;
import Global.Exploration.BFS;


import java.util.*;


import java.util.LinkedList;
import java.util.List;



public class RecupEncombrant {
    public List<Arc> RecupEncombrant(List<Encombrant> encombrant, String pointDeDepot, Map<String, Sommets> mapSommets, Map<String, Arc> mapArcs) {
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
            List<Arc> chemin = BFS.dijkstraMultiArc(origineBFS, localisations ,mapSommets, mapArcs);
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
        chemin_fermeture = BFS.dfs(origineBFS,pointDeDepot,mapSommets,mapArcs);
        chemin_total.addAll(chemin_fermeture);
        //Retour du chemin total
        return chemin_total;
    }

    public List<Arc> Recup1Encombrant(Encombrant encombrant, String pointDeDepot, Map<String, Sommets> mapSommets, Map<String, Arc> mapArcs) {
        //Initialisation
        List<Arc> chemin_total = new LinkedList<>();

        //Définition de l'origine
        String origineBFS = pointDeDepot;

        //DFS
        List<Arc> chemin = BFS.dfs(origineBFS, encombrant.getLocalisation().getSommet1().getNom(),mapSommets, mapArcs);

        //Retour au dépot
        List <Arc> chemin_fermeture = new LinkedList<>();
        if (!chemin.contains(encombrant.getLocalisation())) {
            chemin_total.addAll(chemin);
            origineBFS = encombrant.getLocalisation().getSommet2().getNom();
            chemin_fermeture = BFS.dfs(origineBFS,pointDeDepot,mapSommets,mapArcs);
        }else{
            origineBFS = encombrant.getLocalisation().getSommet1().getNom();
            chemin_fermeture = BFS.dfs(origineBFS,pointDeDepot,mapSommets,mapArcs);
        }

        chemin_total.addAll(chemin_fermeture);
        //Retour du chemin total
        return chemin_total;
    }
}
