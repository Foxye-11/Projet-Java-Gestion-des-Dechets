package Global.Planification;

import Global.Architecture.Arc;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Tournee {
    private LocalDate date;
    private String type_dechet; // "OM", "Recyclable", "Verre", "Dechets Organiques"
    private String type_tournee; // "Habitations", "Points de collecte", "Encombrants"
    private List<Arc> liste = new LinkedList<>();

    public Tournee(LocalDate date, String type_dechet, String type_tournee) {
        this.date = date;
        this.type_dechet = type_dechet;
        this.type_tournee = type_tournee;
    }

    public void setListe(List<Arc> liste) {this.liste = liste;}

    public LocalDate getDate() { return date; }
    public String getType_dechet() { return type_dechet; }
    public String getType_tournee() { return type_tournee; }
    public List<Arc> getListe() { return liste; }
}