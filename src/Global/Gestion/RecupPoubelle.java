package Global.Gestion;

import Global.Architecture.Arc;
import Global.Architecture.Quartier;
import Global.Architecture.Sommet.PointDeDepot;
import Global.Architecture.Sommet.Sommets;
import Global.Entite.Camion;
import Global.Exploration.AlgorithmeExplo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecupPoubelle {
    public List<Arc> allerRetourPointDeDepot(PointDeDepot pointDeDepot, Sommets sommet,
                                             Map<String, Sommets> mapSommet, Map<String, Arc> mapArc) {
        List<Arc> allerPointDeDepot = new LinkedList<>();
        List<Arc> retourPointDeDepot = new LinkedList<>();
        List<Arc> allerRetourPointDepot = new LinkedList<>();

        allerPointDeDepot = AlgorithmeExplo.bfsSommet(sommet.getNom(), pointDeDepot.getNom(),mapSommet, mapArc);
        retourPointDeDepot = AlgorithmeExplo.bfsSommet(pointDeDepot.getNom(),sommet.getNom(),mapSommet, mapArc);

        if (allerPointDeDepot.isEmpty() || retourPointDeDepot.isEmpty()){
            throw new NullPointerException();
        }

        allerRetourPointDepot.addAll(allerPointDeDepot);
        allerPointDeDepot.addAll(retourPointDeDepot);
        return allerRetourPointDepot;
    }

    public List<Arc> recupPoubelleQuartier(PointDeDepot pointDeDepot, Map<String, Sommets> mapSommets, Map<String, Arc> mapArcs, Quartier quartier) {

        List<Arc> chemin_total = new LinkedList<>();
        List<Arc> cheminNonBind = AlgorithmeExplo.hierholzerArcsQuartier(pointDeDepot.getNom(), mapSommets, quartier);

        int i=0;
        Camion camion = new Camion(69, pointDeDepot.getNom());
        while(cheminNonBind.get(i) != null) {
            camion.charger(cheminNonBind.get(i).getNbMaisons()); ;
            if(!(camion.getCharge_actuelle() <= camion.getCharge_max())) {
                List <Arc> temp = new LinkedList<>();
                temp = allerRetourPointDeDepot(pointDeDepot,cheminNonBind.get(i).getSommet1(),mapSommets, mapArcs);
                if (temp.isEmpty()){
                    throw new NullPointerException();
                }else{
                    chemin_total.addAll(temp);
                    camion.decharger_camion();
                }
            }else{
                chemin_total.add(cheminNonBind.get(i));
            }
            i++;
        }
        return chemin_total;
    }

    public List<Arc> recupPoubelle(PointDeDepot pointDeDepot, Map<String, Sommets> mapSommets,
                                   Map<String, Arc> mapArcs) {

        List<Arc> chemin_total = new LinkedList<>();
        List<Arc> cheminNonBind = AlgorithmeExplo.hierholzerArcs(pointDeDepot.getNom(), mapSommets);

        int i=0;
        Camion camion = new Camion(69, pointDeDepot.getNom());
        while(cheminNonBind.get(i) != null) {
            camion.charger(cheminNonBind.get(i).getNbMaisons()); ;
            if(!(camion.getCharge_actuelle() <= camion.getCharge_max())) {
                List <Arc> temp = new LinkedList<>();
                temp = allerRetourPointDeDepot(pointDeDepot,cheminNonBind.get(i).getSommet1(),mapSommets, mapArcs);
                if (temp.isEmpty()){
                    throw new NullPointerException();
                }else{
                    chemin_total.addAll(temp);
                    camion.decharger_camion();
                }
            }else{
                chemin_total.add(cheminNonBind.get(i));
            }
            i++;
        }
        return chemin_total;
    }
}
