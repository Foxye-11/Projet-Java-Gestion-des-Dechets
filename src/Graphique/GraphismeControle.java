package Graphique;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphismeControle extends JPanel {

    public static class Arc {
        public String dest;
        public int type; // 0 = sortante, 1 = entrante, 2 = bidirectionnelle

        public Arc(String dest, int type) {
            this.dest = dest;
            this.type = type;
        }
    }

    private final Map<String, Set<Arc>> adjacencyList;
    private final Map<String, Point> positions = new LinkedHashMap<>();

    public GraphismeControle(Map<String, Set<Arc>> adjacencyList) {
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        int arrowSize = 10; // taille de la flèche
        Set<String> drawn = new HashSet<>();

        for (String src : adjacencyList.keySet()) {
            Point p1 = positions.get(src);

            for (Arc arc : adjacencyList.get(src)) {
                String dest = arc.dest;
                int type = arc.type;

                Point p2 = positions.get(dest);
                if (p2 == null) continue;

                // couleur et dessin selon le type
                switch (type) {
                    case 0 -> g2.setColor(Color.RED);        // sortante
                    case 1 -> g2.setColor(Color.GREEN);      // entrante
                    case 2 -> g2.setColor(Color.DARK_GRAY);  // bidirectionnelle
                }

                // tracer la ligne entre les sommets
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                // tracer flèches
                if (type == 0) {
                    drawArrow(g2, p1, p2, arrowSize); // sortante
                } else if (type == 1) {
                    drawArrow(g2, p1, p2, arrowSize); // entrante
                } else {
                    drawArrow(g2, p1, p2, arrowSize); // bidirectionnelle
                    drawArrow(g2, p2, p1, arrowSize);
                }
            }
        }

        // Dessin des sommets
        for (String node : adjacencyList.keySet()) {
            Point p = positions.get(node);

            // Cercle du sommet
            g2.setColor(new Color(80, 120, 255));
            g2.fillOval(p.x - 15, p.y - 15, 30, 30);

            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 15, p.y - 15, 30, 30);

            // Lettre centrée dans le cercle
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(node);
            int textHeight = fm.getAscent();
            g2.drawString(node, p.x - textWidth / 2, p.y + textHeight / 4);
        }
    }

    /**
     * Dessine une flèche de p1 vers p2
     */
    private void drawArrow(Graphics2D g2, Point p1, Point p2, int arrowSize) {
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);

        int xArrow = p2.x - (int)(Math.cos(angle) * 15);
        int yArrow = p2.y - (int)(Math.sin(angle) * 15);

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(xArrow, yArrow);
        arrowHead.addPoint(xArrow - (int)(arrowSize * Math.cos(angle - Math.PI / 6)),
                yArrow - (int)(arrowSize * Math.sin(angle - Math.PI / 6)));
        arrowHead.addPoint(xArrow - (int)(arrowSize * Math.cos(angle + Math.PI / 6)),
                yArrow - (int)(arrowSize * Math.sin(angle + Math.PI / 6)));
        g2.fill(arrowHead);
    }

}
