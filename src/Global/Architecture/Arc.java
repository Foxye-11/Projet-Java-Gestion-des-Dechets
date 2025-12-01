package Global.Architecture;

import Global.Architecture.Sommet.Sommet;

public class Arc {
    private String nomRue;
    private int nbMaisons;
    private float longueur;
    private int sens;
    private Sommet sommet1;
    private Sommet sommet2;

    public Arc(String nom_rue, int nb_habitations, float longueur, int sens, Sommet sommet1, Sommet sommet2) {
        this.nomRue = nom_rue;
        this.nbMaisons = nb_habitations;
        this.longueur = longueur;
        this.sens = sens;
        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
    }

    // getter
    public String getNomRue() {return nomRue;}
    public int getNbMaisons() {return nbMaisons;}
    public float getLongueur() {return longueur;}
    public int getSens() {return sens;}
    public Sommet getSommet1() {return sommet1;}
    public Sommet getSommet2() {return sommet2;}


    public Sommet chgtSommet(Sommet s) { //permet la transition entre deux sommets sur la même rue
        Sommet newSommet = null;

        if ( s.equals(this.sommet1)){
            newSommet = this.sommet2;
        }
        if (s.equals(this.sommet2)){
            newSommet = this.sommet1;
        }

        return newSommet;
    }
}
