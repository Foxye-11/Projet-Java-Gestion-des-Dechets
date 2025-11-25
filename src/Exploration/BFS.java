package Exploration;

import Architecture.Arc;
import Architecture.Sommet.Sommets;

import java.util.*;

public class BFS {
    // Classe interne pour représenter un état dans la recherche
    private static class Etat implements Comparable<Etat> {
        Sommets sommet;
        String nom_rue;
        List<String> chemin;
        int changements;
        int distance;

        Etat(Sommets sommet, String nom_rue, List<String> chemin, int changements, int distance) {
            this.sommet = sommet;
            this.nom_rue = nom_rue;
            this.chemin = chemin;
            this.changements = changements;
            this.distance = distance;
        }

        public int compareTo(Etat other) {
            // MOINS de correspondances possibles, puis MOINS de distance possible
            if (this.changements != other.changements)
                return Integer.compare(this.changements, other.changements);
            return Integer.compare(this.distance, other.distance);
        }
    }
    public void bfs(Sommets depart, Sommets arrivee){
        //Fais le BFS et l'affichage du chemin trouvé

            if (depart == null || arrivee == null) {
                System.out.println(" Trajet invalide !");
                return;
            }
            //file de priorité pour le BFS
            PriorityQueue<Etat> file = new PriorityQueue<>();
            Set<String> visites = new HashSet<>();

            for (String ligne : depart.getLignes()) { //On a besoin de la map totale de la ville!!!
                file.add(new Etat(depart, ligne, new ArrayList<>(List.of(depart.getNom_rue())), 0, 0));
            }

            Etat resultat = null;
            //BFS
            while (!file.isEmpty()) {
                Etat courant = file.poll();
                String cle = courant.sommet.getNom_rue() + "_" + courant.nom_rue;
                if (visites.contains(cle)) continue;
                visites.add(cle);

                if (courant.sommet.equals(arrivee)) {
                    resultat = courant;
                    break;
                }
                //Parcours du graphe avec les arcs
                for (Arc arc : courant.sommet.getArcs_sortants().values()) {
                    Sommets voisin = arc.chgtSommet(courant.sommet);
                    String ligneArc = arc.getNom_rue();
                    int nouveauxChangements = courant.changements;

                    if (!ligneArc.equals(courant.nom_rue)) {
                        nouveauxChangements++;
                    }
                    //Ajout du chemin dans la file
                    List<String> nouveauChemin = new ArrayList<>(courant.chemin);
                    nouveauChemin.add(voisin.getNom_rue());

                    file.add(new Etat(voisin, ligneArc, nouveauChemin, nouveauxChangements, courant.distance + 1));
                }
            }

            if (resultat == null) {
                System.out.println(" Aucun chemin trouvé !");
                return;
            }
            //Affichage du résumé du chemin trouvé
            System.out.println("\n Chemin trouvé (" + resultat.distance + " stations, " + resultat.changements + " changements)");
            System.out.println(" Itinéraire détaillé :");

            String derniereLigne = null;

        }

}
