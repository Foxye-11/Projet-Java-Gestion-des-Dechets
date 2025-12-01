package Global.Entite;

public class Poubelle {
    //ordures ménagères, recyclables, encombrants, verres
    private String type;

    public  Poubelle(String type) {
        this.type = type;
    }

    public String getType() {return type;}
}
