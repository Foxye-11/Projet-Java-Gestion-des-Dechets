package Graphique;

import javax.swing.*;
import java.util.Map;
import java.util.Set;

public class GraphiqueFenetre extends JFrame {

    public GraphiqueFenetre(Map<String, Set<String>> adjacency) {

        setTitle("Graphe sommets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphismeControle panel = new GraphismeControle(adjacency);
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
