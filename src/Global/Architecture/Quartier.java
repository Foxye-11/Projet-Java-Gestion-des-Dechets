package Global.Architecture;
import Global.Architecture.Sommet.Sommets;

import java.util.*;

public class Quartier {
    private int numQuartier;
    private Map<String, Sommets> sommets;
    private Map<String, Arc> arcs;
    private int couleur;

    public Quartier(int idQuartier) {
        this.numQuartier = idQuartier;
        this.sommets = new HashMap<>();
        this.arcs = new HashMap<>();
        this.couleur = -1; // -1 = pas encore colorié
    }


    // getter
    public int getIdQuartier() {return numQuartier;}
    public Map<String, Sommets> getSommets() {return sommets;}
    public Map<String, Arc> getArcs() {return arcs;}
    public int getCouleur() {return couleur;}

    // setter avec verification
    public void addSommet(Sommets s) {
        if (s.getQuartier() == numQuartier) {
            sommets.put(s.getNom(), s);
        }
    }
    public void addArc(String key, Arc a) {
        if (a.getQuartier() == numQuartier) {
            arcs.put(key, a);
        }
    }
    public void setCouleur(int couleur) {this.couleur = couleur;}


    // Vérifier si le quartier est connexe (BFS sur les sommets)
    public boolean estConnexe() {
        if (sommets.isEmpty()) return true;

        Set<Sommets> visites = new HashSet<>();
        Queue<Sommets> file = new LinkedList<>();

        Sommets start = sommets.values().iterator().next();
        file.add(start);
        visites.add(start);

        while (!file.isEmpty()) {
            Sommets courant = file.poll();
            for (Arc arc : courant.getArcsSortants()) {
                Sommets voisin = arc.getSommet2();
                if (voisin.getQuartier() == numQuartier && !visites.contains(voisin)) {
                    visites.add(voisin);
                    file.add(voisin);
                }
            }
        }

        return visites.size() == sommets.size();
    }

    public void afficherQuartier() {
        System.out.println("\n=== Quartier " + numQuartier + " ===");
        System.out.println("Sommets:");
        for (Sommets s : sommets.values()) {
            System.out.println(" - " + s.getNom() + " (Q" + s.getQuartier() + ")");
        }
        System.out.println("Arcs:");
        for (Arc a : arcs.values()) {
            System.out.println(" - " + a.getNomRue() + " (" + a.getSommet1().getNom() + " -> " + a.getSommet2().getNom() + ")");
        }
        System.out.println("Connexe ? " + estConnexe() + "\n");
    }
}