package Entite;

import Architecture.Arc;
import Architecture.Rue;

public class Camion {
    private int id;
    private int charge = 100;
    private Rue rue_actuelle;
    private Arc arc_actuel;
    private String depot;

    public Camion(int id, String depot){
        this.id = id;
        this.depot = depot;
    }
}
