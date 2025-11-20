package Architecture.Sommet;

import Architecture.Arc;

import java.util.ArrayList;
import java.util.HashSet;

public class Sommets {
    private ArrayList<Arc> arcs_sortants;
    private ArrayList<Arc> arcs_entrant;
    private HashSet<String> nom_rue;

    public Sommets(ArrayList<Arc> arcs_sortants, ArrayList<Arc> arcs_entrant, HashSet<String> nom_rue) {
        this.arcs_sortants = arcs_sortants;
        this.arcs_entrant = arcs_entrant;
        this.nom_rue = nom_rue;
    }


}
