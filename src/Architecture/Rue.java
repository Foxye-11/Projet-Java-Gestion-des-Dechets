package Architecture;

import java.util.LinkedList;

public class Rue {
    private String nom;
    private LinkedList<Arc> ensemble_rue;

    public Rue(String nom, LinkedList<Arc> ensemble_rue) {
        this.nom = nom;
        this.ensemble_rue = ensemble_rue;
    }
}
