package Global.Architecture.Sommet;

import Global.Architecture.Arc;
import Global.Entite.Poubelle;

import java.util.ArrayList;


public class PointDeCollecte {
    private String rue;
    private Arc localisation;
    private String nom;
    private int capacite;
    private ArrayList<Poubelle> poubelles;

    public PointDeCollecte(String rue, String nom, int capacite, Arc location) {
        this.rue = rue;
        this.nom = nom;
        this.capacite = capacite;
        this.localisation = location;
    }
    public PointDeCollecte(Arc localisation, String nom, int capacite) {
        this.localisation = localisation;
        this.nom = nom;
        this.capacite = capacite;
    }

    // getter
    public String getRue() { return rue;}
    public String getNom() { return nom;}
    public int getCapacite() { return capacite;}
    public int getNombre_poubelles() {return capacite;}
    public ArrayList<Poubelle> getPoubelles() {return poubelles;}
    public Arc getLocalisation() {return localisation;}
}
