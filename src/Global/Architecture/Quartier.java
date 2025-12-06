package Global.Architecture;

import java.util.LinkedList;

public class Quartier {

    private String nom;
    private LinkedList<Rue> rues;
    private boolean[][][] occupation = new boolean[12][5][7];
    private LinkedList<Quartier> voisins = new LinkedList<>();
    private int couleur = -1;

    public Quartier (String nom){
        this.nom = nom;
        this.rues = new LinkedList<>();
    }

    public static void colorierQuartiers(LinkedList<Quartier> quartiers){
        for(Quartier q : quartiers){
            boolean[] interdit = new boolean[quartiers.size()];

            for (Quartier v : q.getVoisins()){
                int c = v.getCouleur();
                if(c != -1){
                    interdit[c] = true;
                }
            }

            int couleur = 0;
            while(couleur < interdit.length && interdit[couleur]) couleur++;
            q.setCouleur(couleur);
        }
    }

    public void addVoisin(Quartier q){
        if (q != this && !voisins.contains(q)){
            voisins.add(q);
            q.voisins.add(this);
        }
    }

    public void addRue(Rue r){if (!rues.contains(r)) rues.add(r);}
    public String getNom(){return nom;}
    public LinkedList<Rue> getRues(){return rues;}
    public boolean estLibre(int m, int s, int j){return !occupation[m][s][j];}
    public void occuper(int m, int s, int j){occupation[m][s][j] = true;}
    public LinkedList<Quartier> getVoisins(){return voisins;}
    public int getCouleur(){return couleur;}
    public void setCouleur(int c){this.couleur = c;}


}
