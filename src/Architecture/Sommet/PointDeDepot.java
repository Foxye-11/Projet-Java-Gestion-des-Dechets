package Architecture.Sommet;

import java.util.ArrayList;

public class PointDeDepot {
    private String nom_voie;
    private int nombre_poubelles;
    private ArrayList<String> poubelles;

    public PointDeDepot(String nom_voie, int nombre_poubelles) {
        this.nom_voie = nom_voie;
        this.nombre_poubelles = nombre_poubelles;
    }

}
