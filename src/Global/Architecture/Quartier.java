package Global.Architecture;

import java.util.LinkedList;

public class Quartier {
    private String nom;
    private LinkedList<Rue> rues;
    private boolean[][][] occupation = new boolean[12][5][7];

    public Quartier (String nom){
        this.nom = nom;
        this.rues = new LinkedList<>();
    }

    public void addRue(Rue r){if (!rues.contains(r)) rues.add(r);}
    public String getNom(){return nom;}
    public LinkedList<Rue> getRues(){return rues;}
    public boolean estLibre(int m, int s, int j){return !occupation[m][s][j];}
    public void occuper(int m, int s, int j){occupation[m][s][j] = true;}

}
