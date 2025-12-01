package Global.Architecture;

import java.util.LinkedList;

public class Rue {
    private String nom;
    private LinkedList<Arc> ensemble_rue;
    private int nbHabitations;
    private float longueur;

    public Rue(String nom, LinkedList<Arc> ensemble_rue, int nbHabitations,  float longueur) {
        this.nom = nom;
        this.ensemble_rue = ensemble_rue;
        this.nbHabitations = nbHabitations;
        this.longueur = longueur;
    }

    // getter
    public String getNom() {return nom;}
    public LinkedList<Arc> getEnsemble_rue() {return ensemble_rue;}

    //setter
    public void addArc(Arc arc) {
        if (!ensemble_rue.contains(arc))
            ensemble_rue.add(arc);
        else System.out.println("Arc deja present");
    }
}
