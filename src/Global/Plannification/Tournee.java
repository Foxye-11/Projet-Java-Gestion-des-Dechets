package Global.Plannification;

public class Tournee {
    public int[] date = new int[3];
    public String type_dechet;
    public String type_tournee;

    public Tournee(int m, int s, int j, String type_dechet, String type_tournee){
        this.date[0] = m;
        this.date[1] = s;
        this.date[2] = j;
        this.type_dechet = type_dechet;
        this.type_tournee = type_tournee;
    }
}
