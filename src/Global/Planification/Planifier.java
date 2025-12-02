package Global.Planification;

public class Planifier {
    public String[] type_dechet = {"om", "recyclable", "verre", "dechets vert"};
    public int[] NbJourParMois = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public boolean[] type_frequence = new boolean[4];
    public int[] frequence_passage = new int[4];
    private Calendrier calendrier = new Calendrier();

    public Planifier(boolean[] type_frequence, int[]frequence_passage){
        this.type_frequence = type_frequence;
        this.frequence_passage = frequence_passage;
    }

    public void creerPlanning(){
        for(int i=0; i<type_dechet.length; i++){
            if(frequence_passage[i] <= 0) continue; // pas de tournée
            for (int mois = 0; mois < 12; mois++) {
                // par semaine OU par mois
                if(type_frequence[i]) { // HEBDOMADAIRE
                    int interval = 7 / frequence_passage[i];
                    for (int semaine = 0; semaine < 5; semaine++) {
                        for (int jour = 0; jour < 6; jour += interval) {
                            addTournee(mois, semaine, jour, type_dechet[i], "Particulier");
                        }
                    }
                }
                else {
                    int interval = NbJourParMois[mois] / frequence_passage[i];
                    for (int jourDuMois = 0; jourDuMois < NbJourParMois[mois]; jourDuMois += interval) {
                        int semaine = jourDuMois / 7;
                        int jour = jourDuMois % 7;
                        addTournee(mois, semaine, jour, type_dechet[i], "Particulier");
                    }
                }
            }
        }
    }

    public void addTournee (int m, int s, int j, String type_dechet, String type_tournee){
        // Décaler d’un jour si déjà occupé
        while (calendrier.getObject(m, s, j) != null){
            j++;
            if (j > 5){
                j = 0;
                s++;
                if (s*7 >= NbJourParMois[m]) {
                    s = 0;
                    m++;
                    if (m >= 12) {
                        System.out.println("A prevoir pour l'année prochaine");
                        return;
                    }
                }
            }
        }
        Tournee t = new Tournee(m, s, j, type_dechet, type_tournee);
        calendrier.addObject(m, s, j, t);
    }
}
