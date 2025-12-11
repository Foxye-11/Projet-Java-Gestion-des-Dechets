package Global.Graphique;

import Global.Architecture.Sommet.Sommets;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

public class GraphiqueFenetre extends JFrame {

    public GraphiqueFenetre(Map<String, Set<GraphismeControle.Arc>> adjacency, Map<String, Sommets> sommets) {

        setTitle("Graphe des Sommets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphismeControle panel = new GraphismeControle(adjacency, sommets);
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
