package Global.Architecture;

import Global.Architecture.Sommet.Sommet;

public class Arc {
    private String nom_rue;
    private int nb_habitations;
    private float longueur;
    private int sens;
    private Sommet[] sommets =  new Sommet[2];

    public Arc(String nom_rue, int nb_habitations, float longueur, int sens, Sommet sommet1, Sommet sommet2) {
        this.nom_rue = nom_rue;
        this.nb_habitations = nb_habitations;
        this.longueur = longueur;
        this.sens = sens;
        this.sommets[0] = sommet1;
        this.sommets[1] = sommet2;
    }


    public String getNom_rue() {return nom_rue;}
    public int  getNb_habitations() {return nb_habitations;}
    public float getLongueur() {return longueur;}
    public int isSens() {return sens;}
    public Sommet[] getSommets() {return sommets;}

    public Sommet chgtSommet(Sommet s) { //permet la transition entre deux sommets sur la même rue
        Sommet newSommet = null;

        if ( s.equals(this.sommets[0])){
            newSommet = this.sommets[1];
        }
        if (s.equals(this.sommets[1])){
            newSommet = this.sommets[0];
        }

        return newSommet;
    }
}
