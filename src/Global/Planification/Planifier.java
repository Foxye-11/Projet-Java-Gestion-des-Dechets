package Global.Planification;
import java.time.*;
import java.util.*;


public class Planifier {
    public String[] type_dechet = {"OM", "Recyclable", "Verre", "Dechets Organiques"};
    private Map<String, List<DayOfWeek>> joursParType = new HashMap<>();
    private Map<String, Double> frequence = new HashMap<>();

    public Planifier(Map<String, List<DayOfWeek>> joursParType, Map<String, Double> frequence) {
        this.joursParType = joursParType;
        this.frequence = frequence;
    }

    public Calendrier creerPlanning(int annee) {
        Calendrier calendrier = new Calendrier();
        LocalDate debut = LocalDate.of(annee, 1, 1);
        LocalDate fin = LocalDate.of(annee, 12, 31);

        for (String type : type_dechet) {
            if (frequence.get(type) == null || frequence.get(type) <= 0) continue;

            LocalDate date = debut;
            int interval = (int)(7/frequence.get(type));
            List<DayOfWeek> joursAutorises = joursParType.get(type);

            while(date.isBefore(fin)){
                addTournee(date, type, "collecte", calendrier, joursAutorises);
                date = date.plusDays(interval);
            }

        }
        return calendrier;
    }

    public void addTournee(LocalDate d, String type_dechet, String type_tournee, Calendrier calendrier, List<DayOfWeek> joursAutorises) {
        if (joursAutorises == null) joursAutorises = Arrays.asList(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        while (!joursAutorises.contains(d.getDayOfWeek()) || calendrier.getTournee(d) != null) {
            d = d.plusDays(1);
        }
        if (joursAutorises.contains(d.getDayOfWeek()) && calendrier.getTournee(d) == null) {
            calendrier.addTournee(new Tournee(d, type_dechet, type_tournee));
            System.out.println("Tournee de " + type_dechet + " ajoutee à " + d);
        } else {
            System.out.println("Erreur d'ajout de la tournée pour " + type_dechet + " à " + d);
        }
    }
}
