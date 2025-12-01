package Architecture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReseauVille {
    private Map<String, Rue> ReseauRue = new HashMap<>();
    private Map<String, Arc> ReseauArc = new HashMap<>();
    private Fichier fichier;

    public ReseauVille(String nom_ficher) throws IOException {
        this.fichier = new Fichier(nom_ficher);
    }

   


}
