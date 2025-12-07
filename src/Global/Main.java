package Global;

import Global.Architecture.Fichier;
import Global.Graphique.GraphiqueFenetre;
import Global.Graphique.GraphismeControle;
import Global.Planification.Calendrier;
import Global.Planification.Planifier;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        // Définition des jours autorisés par type
        Map<String, List<DayOfWeek>> joursParType = new HashMap<>();
        joursParType.put("OM", null);
        joursParType.put("Recyclable", null);
        joursParType.put("Verre", null);
        joursParType.put("Dechets Organiques", null);

        // Définition des fréquences
        Map<String, Double> frequence = new HashMap<>();
        frequence.put("OM", 2.0);
        frequence.put("Recyclable", 2.0);
        frequence.put("Verre", 0.5);
        frequence.put("Dechets Organiques", 0.3);

        Planifier planifier = new Planifier(joursParType, frequence);
        Calendrier calendrier = planifier.creerPlanning(2025);

        planifier.addTournee(LocalDate.of(2025, 1, 20), "null", "Encombrants", calendrier, null);
        planifier.addTournee(LocalDate.of(2025, 1, 20), "Verre", "Points de collecte", calendrier, null);
        calendrier.affichage(2025);

        calendrier.setVisible(true);
    }

}
