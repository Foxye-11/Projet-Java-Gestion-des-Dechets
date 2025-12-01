package Global.Architecture.Sommet;

import Global.Architecture.Arc;
import Global.Entite.Camion;

import java.util.ArrayList;
import java.util.Map;

public class PointDeDepot extends Sommets {
    private int nombre_poubelles;
    private ArrayList<String> poubelles;

    public PointDeDepot(Map<String, Arc> arcs_sortants, Map<String, Arc> arcs_entrant, String nom_rue, int nombre_poubelles) {
        super(arcs_sortants,arcs_entrant,nom_rue);
        this.nombre_poubelles = nombre_poubelles;
    }

    public void chargerCamion(Camion camion, int quantite_dechets){
        camion.charger(quantite_dechets);
    }

    public void dechargerCamion(Camion camion){
        camion.decharger_camion();
    }

    public int getNombre_poubelles() {return nombre_poubelles;}
    public ArrayList<String> getPoubelles() {return poubelles;}
}
