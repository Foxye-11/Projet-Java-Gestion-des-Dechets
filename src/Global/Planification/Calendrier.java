package Global.Planification;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;


public class Calendrier extends JFrame {
    private Map<LocalDate, Tournee> planning = new HashMap<>();

    // setter & getter
    public void addTournee(Tournee t) {planning.put(t.getDate(), t);}
    public Tournee getTournee(LocalDate date) {return planning.get(date);}


    // Méthode d’affichage
    public void affichage(int annee) {
        setLayout(new GridLayout(3, 4)); // 12 mois

        String[] mois = {"Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre"};

        for (int m = 1; m <= 12; m++) {
            YearMonth dayPerMonth = YearMonth.of(annee, m); // jour par mois
            JPanel panelMois = new JPanel(new GridLayout(0, 7)); // grille des 7 jours par semaines
            panelMois.setBorder(BorderFactory.createTitledBorder(mois[m-1])); // titre du mois

            // titre par colonne des jours de la semaine
            String[] jours = {"Lun","Mar","Mer","Jeu","Ven","Sam","Dim"};
            for (String j : jours) {
                JLabel daysTitle = new JLabel(j, SwingConstants.CENTER);
                daysTitle.setOpaque(true);
                daysTitle.setBackground(Color.LIGHT_GRAY);
                panelMois.add(daysTitle);
            }

            // Décalage pour aligner le 1er jour du mois
            LocalDate premierJour = dayPerMonth.atDay(1); // recup le 1e jour
            int decalage = (premierJour.getDayOfWeek().getValue() % 7); // decale pour avoir le bon alignement
            for (int i = 0; i < decalage-1; i++) {
                panelMois.add(new JLabel(""));
            }

            // Jours du mois
            for (int jour = 1; jour <= dayPerMonth.lengthOfMonth(); jour++) {
                LocalDate date = dayPerMonth.atDay(jour);
                JLabel caseJour = new JLabel(String.valueOf(jour), SwingConstants.CENTER); // creation d'une case par jour avec son numero
                caseJour.setOpaque(true);

                Tournee t = planning.get(date);
                if (t != null) { // Coloriage de chaque jour selon sa tournée ou non
                    switch (t.getType_dechet()) {
                        case "OM": caseJour.setBackground(Color.GRAY); break;
                        case "Recyclable": caseJour.setBackground(Color.YELLOW); break;
                        case "Verre": caseJour.setBackground(Color.GREEN); break;
                        case "Dechets Organiques": caseJour.setBackground(new Color(139,69,19)); break;
                        default: caseJour.setBackground(Color.WHITE); break;
                    }
                    // Différenciation selon type de collecte
                    switch (t.getType_tournee()) {
                        case "Habitations":
                            caseJour.setText(caseJour.getText() + " H"); // ajoute un H
                            caseJour.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                            break;
                        case "Points de collecte":
                            caseJour.setText(caseJour.getText() + " P"); // ajoute un P
                            caseJour.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                            break;
                        case "Encombrants":
                            caseJour.setBackground(Color.ORANGE); // couleur spéciale
                            caseJour.setText(caseJour.getText() + " E");
                            caseJour.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                            break;
                    }

                } else {
                    caseJour.setBackground(Color.WHITE);
                }
                panelMois.add(caseJour);
            }
            add(panelMois);
        }
        pack();
        setLocationRelativeTo(null);
    }
}
