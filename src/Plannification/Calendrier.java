package Plannification;

public class Calendrier {
    private Tournee[][][] planning = new Tournee[12][5][7];

    public Calendrier(){}

    public void addObject(int m, int s,int j, Tournee objet){planning[m][s][j] = objet;}
    public Tournee getObject(int m, int s,int j, Tournee objet){return planning[m][s][j];}

    public void affichage_planning(){

    }
}
