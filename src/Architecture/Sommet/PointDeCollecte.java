package Architecture.Sommet;

import Entite.Poubelle;

import java.util.ArrayList;

public class PointDeCollecte {
    private String nom_voie;
    private int nombre_poubelles;
    private ArrayList<Poubelle> poubelles;

    public PointDeCollecte(String nom_voie, int nombre_poubelles,ArrayList<Poubelle> poubelles) {
        this.nom_voie = nom_voie;
        this.nombre_poubelles = nombre_poubelles;
        this.poubelles = poubelles;
    }


    public String getNom_voie() {return nom_voie;}
    public int getNombre_poubelles() {return nombre_poubelles;}
    public ArrayList<Poubelle> getPoubelles() {return poubelles;}
}
