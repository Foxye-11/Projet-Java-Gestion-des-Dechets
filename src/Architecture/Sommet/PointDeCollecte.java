package Architecture.Sommet;

import Architecture.Arc;
import Entite.Poubelle;

import java.util.ArrayList;
import java.util.Map;

public class PointDeCollecte extends Sommets {
    private int nombre_poubelles;
    private ArrayList<Poubelle> poubelles;

    public PointDeCollecte(Map<String, Arc> arcs_sortants, Map<String, Arc> arcs_entrant, String nom_rue,String nom_voie, int nombre_poubelles, ArrayList<Poubelle> poubelles) {
        super(arcs_sortants, arcs_entrant, nom_rue);
        this.nombre_poubelles = nombre_poubelles;
        this.poubelles = poubelles;
    }

    public int getNombre_poubelles() {return nombre_poubelles;}
    public ArrayList<Poubelle> getPoubelles() {return poubelles;}
}
