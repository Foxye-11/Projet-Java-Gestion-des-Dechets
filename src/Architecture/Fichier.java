package Architecture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Fichier {
    private String nomFichier = "MontBrunlesBains.txt";
    public Map<String, Set<String>> reseau = new LinkedHashMap<>();

    public Fichier() throws IOException {
        chargerAgglo();
    }

    private void chargerAgglo() throws IOException{
        String motCle1 = "MAISONS PAR RUE:";
        String motCle2 = "INTERSECTIONS ENTRE RUES:";
        String motCle3 = "POINTS DE COLLECTE:";
        String motCle4 = "DEPOT:";
        boolean lecture = false;

        BufferedReader br = new BufferedReader(new FileReader(nomFichier));


    }

    public void afficherAgglo(){

    }

    public Map<String, Set<String>> reseauAgglo(){
        return reseau;
    }


}
