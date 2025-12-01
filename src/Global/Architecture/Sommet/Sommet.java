package Global.Architecture.Sommet;

import Global.Architecture.Arc;

import java.util.Map;
import java.util.Set;

public class Sommet {
    private Map<String, Arc> arcs_sortants;
    private Map<String , Arc> arcs_entrant;
    private Set<String> rues;
    private String nom;

    public Sommet(Map<String,Arc> arcs_sortants, Map<String, Arc> arcs_entrant, Set<String> rues, String nom) {
        this.arcs_sortants = arcs_sortants;
        this.arcs_entrant = arcs_entrant;
        this.rues = rues;
        this.nom = nom;
    }

    // getter
    public Map<String,Arc> getArcs_sortants() {return arcs_sortants;}
    public Map<String,Arc> getArcs_entrant() {return arcs_entrant;}
    public Set<String> getRue() {return rues;}

    // setter
    public void addArcSortant(Arc arc) {
        if (arc != null)
            arcs_sortants.putIfAbsent(arc.getNom_rue(), arc);
    }
    public void addArcEntrant(Arc arc) {
        if (arc != null)
            arcs_entrant.putIfAbsent(arc.getNom_rue(), arc);
    }

}
