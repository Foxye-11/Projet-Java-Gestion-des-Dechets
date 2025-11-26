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
        int radius = 250;
        int centerX = 450;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Anti-aliasing pour un rendu plus propre
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessin des arêtes
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.DARK_GRAY);

        Set<String> drawn = new HashSet<>();

        for (String src : adjacencyList.keySet()) {
            Point p1 = positions.get(src);
            for (String dest : adjacencyList.get(src)) {

                // éviter les doublons (a-b == b-a)
                String id1 = src + ":" + dest;
                String id2 = dest + ":" + src;
                if (drawn.contains(id1) || drawn.contains(id2)) continue;

                Point p2 = positions.get(dest);
                if (p2 != null) {
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    drawn.add(id1);
                }
            }
        }

        // Dessin des sommets
        for (String node : adjacencyList.keySet()) {
            Point p = positions.get(node);

            // Cercle du noeud
            g2.setColor(new Color(80, 120, 255));
            g2.fillOval(p.x - 15, p.y - 15, 30, 30);

            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 15, p.y - 15, 30, 30);

            // Nom du noeud
            g2.drawString(node, p.x + 20, p.y);
        }
    }
}
