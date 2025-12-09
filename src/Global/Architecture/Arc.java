package Global.Architecture;

import Global.Architecture.Sommet.Sommets;

public class Arc {
    private String nomRue;
    private int nbMaisons;
    private float longueur;
    private int sens;
    private Sommets sommets1;
    private Sommets sommets2;
    private int quartier;

    public Arc(String nom_rue, int nb_habitations, float longueur, int sens, Sommets sommets1, Sommets sommets2, int quartier) {
        this.nomRue = nom_rue;
        this.nbMaisons = nb_habitations;
        this.longueur = longueur;
        this.sens = sens;
        this.sommets1 = sommets1;
        this.sommets2 = sommets2;
        this.quartier = quartier;
    }

    // getter
    public String getNomRue() {return nomRue;}
    public int getNbMaisons() {return nbMaisons;}
    public float getLongueur() {return longueur;}
    public int getSens() {return sens;}
    public Sommets getSommet1() {return sommets1;}
    public Sommets getSommet2() {return sommets2;}
    public int getQuartier() {return quartier;}


    public Sommets chgtSommet(Sommets s) { //permet la transition entre deux sommets sur la même rue
        Sommets newSommets = null;

        if ( s.equals(this.sommets1)){
            newSommets = this.sommets2;
        }
        if (s.equals(this.sommets2)){
            newSommets = this.sommets1;
        }

        return newSommets;
    }
}
