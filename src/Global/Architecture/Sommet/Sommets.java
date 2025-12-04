package Global.Architecture.Sommet;

import Global.Architecture.Arc;

import java.util.List;
import java.util.Set;

public class Sommets {
    private List<Arc> arcsSortants;
    private List<Arc> arcsEntrant;
    private Set<String> rues;
    private String nom;

    public Sommets(List<Arc> arcsSortants, List<Arc> arcsEntrant, Set<String> rues, String nom) {
        this.arcsSortants = arcsSortants;
        this.arcsEntrant = arcsEntrant;
        this.rues = rues;
        this.nom = nom;
    }

    // getter
    public List<Arc> getArcsSortants() {return arcsSortants;}
    public List<Arc> getArcsEntrant() {return arcsEntrant;}
    public Set<String> getRues() {return rues;}
    public String getNom() {return nom;}

    // setter
    public void addArcSortant(Arc arc) {
        if (arc != null)
            arcsSortants.add(arc);
    }
    public void addArcEntrant(Arc arc) {
        if (arc != null)
            arcsEntrant.add(arc);
    }

}
