package Architecture.Sommet;

import Entite.Camion;

import java.util.ArrayList;

public class PointDeDepot {
    private String nom_voie;
    private int nombre_poubelles;
    private ArrayList<String> poubelles;

    public PointDeDepot(String nom_voie, int nombre_poubelles) {
        this.nom_voie = nom_voie;
        this.nombre_poubelles = nombre_poubelles;
    }

    public void chargerCamion(Camion camion, int quantite_dechets){
        camion.charger(quantite_dechets);
    }

    public void dechargerCamion(Camion camion){
        camion.decharger_camion();
    }


    public String getNom_voie() {return nom_voie;}
    public int getNombre_poubelles() {return nombre_poubelles;}
    public ArrayList<String> getPoubelles() {return poubelles;}
}
