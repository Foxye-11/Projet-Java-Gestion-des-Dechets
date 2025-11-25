package Entite;

import Architecture.Arc;
import Architecture.Rue;

public class Camion {
    private int id;
    private int charge_max;
    private int charge_actuelle;
    private Rue rue_actuelle;
    private Arc arc_actuel;
    private String depot;

    public Camion(int id, String depot){
        this.id = id;
        this.depot = depot;
        this.charge_max = 100;
        this.charge_actuelle = 0;
    }

    public void charger(int quantite_D){
        if (charge_actuelle + quantite_D > charge_max){
            System.out.println("Impossible de charger le camion, celui-ci est déjà plein");
            return;
        }
        charge_actuelle += quantite_D;
    }

    public int decharger_camion(){
        charge_actuelle = 0;
        return charge_actuelle;
    }

    public int getId() {return id;}
    public int getCharge_max() {return charge_max;}
    public int getCharge_actuelle() {return charge_actuelle;}
    public Rue getRue_actuelle() {return rue_actuelle;}
    public Arc getArc_actuelle() {return arc_actuel;}
    public String getDepot() {return depot;}
    public void setArc_actuelle(Arc arc_actuelle) {this.arc_actuel = arc_actuelle;}
}
