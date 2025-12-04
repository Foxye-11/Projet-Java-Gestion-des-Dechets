package Global.Architecture.Sommet;

import Global.Entite.Poubelle;

import java.util.ArrayList;


public class PointDeCollecte {
    private String rue;
    private String nom;
    private int capacite;
    private ArrayList<Poubelle> poubelles;

    public PointDeCollecte(String rue, String nom, int capacite) {
        this.rue = rue;
        this.nom = nom;
        this.capacite = capacite;
    }

    // getter
    public String getRue() { return rue;}
    public String getNom() { return nom;}
    public int getCapacite() { return capacite;}
    public int getNombre_poubelles() {return capacite;}
    public ArrayList<Poubelle> getPoubelles() {return poubelles;}
}
