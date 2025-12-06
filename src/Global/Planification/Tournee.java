package Global.Planification;

import java.time.LocalDate;

public class Tournee {
    private LocalDate date;
    private String type_dechet; // "OM", "Recyclable", "Verre", "Dechets Organiques"
    private String type_tournee; // "Habitations", "Points de collecte", "Encombrants"

    public Tournee(LocalDate date, String type_dechet, String type_tournee) {
        this.date = date;
        this.type_dechet = type_dechet;
        this.type_tournee = type_tournee;
    }

    public LocalDate getDate() { return date; }
    public String getType_dechet() { return type_dechet; }
    public String getType_tournee() { return type_tournee; }
}