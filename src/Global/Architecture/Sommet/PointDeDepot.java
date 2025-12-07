package Global.Architecture.Sommet;

import Global.Architecture.Arc;
import Global.Entite.Camion;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PointDeDepot {
    private String nom;
    private int capacite;
    private ArrayList<String> poubelles;
    private Arc localisation;

    public PointDeDepot(String nom, Arc localisation) {
        this.nom = nom;
        this.localisation = localisation;
    }

    public void chargerCamion(Camion camion, int quantite_dechets){
        camion.charger(quantite_dechets);
    }

    public void dechargerCamion(Camion camion){
        camion.decharger_camion();
    }

    public String getNom() {return nom;}
    public int getCapacite() {return capacite;}
    public ArrayList<String> getPoubelles() {return poubelles;}
    public Arc getLocalisation() {return localisation;}
}
