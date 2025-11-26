package Graphique;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

public class GraphiqueFenetre extends JFrame {

    public GraphiqueFenetre(Map<String, Set<GraphismeControle.Arc>> adjacency) {

        setTitle("Graphe des Sommets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphismeControle panel = new GraphismeControle(adjacency);
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
