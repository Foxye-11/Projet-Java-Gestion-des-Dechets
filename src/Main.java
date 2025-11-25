import Architecture.Fichier;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        try {
            Fichier fichier = new Fichier();

            fichier.afficherRues();
            fichier.afficherIntersections();
            fichier.afficherPointsCollectes();
            fichier.afficherPointsDepots();


        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier " + e.getMessage());
        }
    }
}
