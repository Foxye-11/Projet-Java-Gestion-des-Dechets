package Graphique;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GraphismeControle extends JPanel {
    private final Map<String, Set<String>> adjacencyList;
    private final Map<String, Point> positions = new LinkedHashMap<>();

    public GraphismeControle(Map<String, Set<String>> adjacencyList) {
        this.adjacencyList = adjacencyList;
        setPreferredSize(new Dimension(900, 700));
        genererPositionsCirculaires();
    }

    private void genererPositionsCirculaires() {
        int n = adjacencyList.size();
        int radius = 250;      // rayon du cercle
        int centerX = 450;     // centre de la fenêtre
        int centerY = 350;

        int i = 0;
        for (String node : adjacencyList.keySet()) {
            double angle = 2 * Math.PI * i / n;
            int x = centerX + (int) (Math.cos(angle) * radius);
            int y = centerY + (int) (Math.sin(angle) * radius);
            positions.put(node, new Point(x, y));
            i++;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        for (String src : adjacencyList.keySet()) {
            Point p1 = positions.get(src);
            for (String dest : adjacencyList.get(src)) {
                Point p2 = positions.get(dest);
                if (p2 != null) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        for (String node : adjacencyList.keySet()) {
            Point p = positions.get(node);
            g.setColor(new Color(80, 120, 255));
            g.fillOval(p.x - 15, p.y - 15, 30, 30);

            g.setColor(Color.BLACK);
            g.drawOval(p.x - 15, p.y - 15, 30, 30);

            g.drawString(node, p.x + 20, p.y);
        }
    }
}
