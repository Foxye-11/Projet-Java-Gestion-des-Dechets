package Architecture.Sommet;

import Architecture.Arc;

import java.util.Map;

public abstract class Sommets {
    private Map<String, Arc> arcs_sortants;
    private Map<String , Arc> arcs_entrant;
    private String nom_rue;

    public Sommets(Map<String,Arc> arcs_sortants, Map<String, Arc> arcs_entrant, String nom_rue) {
        this.arcs_sortants = arcs_sortants;
        this.arcs_entrant = arcs_entrant;
        this.nom_rue = nom_rue;
    }

    public void addArcSortant(Arc arc) {
        if (arc != null) {
            arcs_sortants.put(arc.getNom_rue(), arc);
        }
    }

    public void addArcEntrant(Arc arc) {
        if (arc != null) {
            arcs_entrant.put(arc.getNom_rue(), arc);
        }
    }


    public Map<String,Arc> getArcs_sortants() {return arcs_sortants;}
    public Map<String,Arc> getArcs_entrant() {return arcs_entrant;}
    public String getNom_rue() {return nom_rue;}


}
