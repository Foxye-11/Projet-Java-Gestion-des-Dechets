package Global.Architecture;

import Global.Architecture.Sommet.Sommets;

public class Arc {
    private String nom_rue;
    private int nb_habitations;
    private int longueur;
    private boolean sensUnique;
    private Sommets[] sommets =  new Sommets[2];

    public Arc(String nom_rue, int nb_habitations, int longueur, boolean sensUnique, Sommets sommet1, Sommets sommet2) {
        this.nom_rue = nom_rue;
        this.nb_habitations = nb_habitations;
        this.longueur = longueur;
        this.sensUnique = sensUnique;
        this.sommets[0] = sommet1;
        this.sommets[1] = sommet2;
    }


    public String getNom_rue() {return nom_rue;}
    public int  getNb_habitations() {return nb_habitations;}
    public int getLongueur() {return longueur;}
    public boolean isSensUnique() {return sensUnique;}
    public Sommets[] getSommets() {return sommets;}

    public Sommets chgtSommet(Sommets s) { //permet la transition entre deux sommets sur la même rue
        Sommets newSommet = null;

        if ( s.equals(this.sommets[0])){
            newSommet = this.sommets[1];
        }
        if (s.equals(this.sommets[1])){
            newSommet = this.sommets[0];
        }

        return newSommet;
    }
}
