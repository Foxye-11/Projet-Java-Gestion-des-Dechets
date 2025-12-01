package Global.Exploration;

import Global.Architecture.Arc;
import Global.Architecture.Sommet.Sommet;

import java.util.List;

public class Trajet {
    private Sommet depart;
    private Sommet destination;
    private List<Arc> chemin;

    public Trajet(Sommet depart, Sommet destination, List<Arc> chemin) {
        this.depart = depart;
        this.destination = destination;
        this.chemin = chemin;
    }

    // getter
    public Sommet getDepart() {return depart;}
    public Sommet getDestination() {return destination;}
    public List<Arc> getChemin() {return chemin;}


    public void afficher() {
        System.out.println("\n* Itinéraire de " + depart.getNom() + " à " + destination.getNom() + " :");
        for (Arc arc : chemin) {
            System.out.println("  - " + arc.getSommet1().getNom() + " → " + arc.getSommet2().getNom() + " (ligne " + arc.getNomRue() + ")");
        }
        System.out.println("Nombre de stations : " + chemin.size());
    }
}
