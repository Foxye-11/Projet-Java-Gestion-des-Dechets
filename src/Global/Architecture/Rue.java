package Global.Architecture;

import java.util.LinkedList;

public class Rue {
    private String nom;
    private LinkedList<Arc> ensembleRue;
    private int nbHabitations;
    private float longueur;

    public Rue(String nom, LinkedList<Arc> ensembleRue, int nbHabitations,  float longueur) {
        this.nom = nom;
        this.ensembleRue = ensembleRue;
        this.nbHabitations = nbHabitations;
        this.longueur = longueur;
    }

    // getter
    public String getNom() {return nom;}
    public LinkedList<Arc> getEnsemble_rue() {return ensembleRue;}
        public int getNbHabitations() {return nbHabitations;}
    public float getLongueur() {return longueur;}
    //setter
    public void addArc(Arc arc) {
        if (!ensembleRue.contains(arc))
            ensembleRue.add(arc);
        else System.out.println("Arc deja present");
    }
}
