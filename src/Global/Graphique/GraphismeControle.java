package Global.Graphique;

import Global.Architecture.Sommet.Sommets;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;

public class GraphismeControle extends JPanel {

    public static class Arc {
        public String dest;
        public int type; // 0 sortante, 1 entrante, 2 bidirectionnelle

        public Arc(String dest, int type) {
            this.dest = dest;
            this.type = type;
        }
    }

    private final Map<String, Set<Arc>> adjacencyList;
    private final Map<String, Sommets> sommets;   // <-- quartier disponible ici
    private final Map<String, Point> positions = new LinkedHashMap<>();

    public GraphismeControle(Map<String, Set<Arc>> adjacencyList,
                             Map<String, Sommets> sommets) {
        this.adjacencyList = adjacencyList;
        this.sommets = sommets;
        setPreferredSize(new Dimension(900, 700));
        genererPositionsCirculaires();
    }

    private void genererPositionsCirculaires() {
        int n = adjacencyList.size();
        int radius = 250;
        int cx = 450, cy = 350;

        int i = 0;
        for (String node : adjacencyList.keySet()) {
            double angle = 2 * Math.PI * i / n;
            int x = cx + (int)(Math.cos(angle) * radius);
            int y = cy + (int)(Math.sin(angle) * radius);
            positions.put(node, new Point(x, y));
            i++;
        }
    }

    private Color getCouleurQuartier(int q) {
        return switch (q) {
            case 1 -> new Color(80, 120, 255);
            case 2 -> new Color(80, 200, 120);
            case 3 -> new Color(255, 180, 80);
            case 4 -> new Color(255, 90, 90);
            default -> Color.GRAY;
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        int arrowSize = 10;

        // ============= DESSIN DES ARCS =====================
        for (String src : adjacencyList.keySet()) {
            Point p1 = positions.get(src);

            for (Arc arc : adjacencyList.get(src)) {
                Point p2 = positions.get(arc.dest);
                if (p2 == null) continue;

                // ---- couleur selon le type ----
                switch (arc.type) {
                    case 0 -> g2.setColor(Color.RED);        // sortante
                    case 1 -> g2.setColor(Color.GREEN);      // entrante
                    case 2 -> g2.setColor(Color.DARK_GRAY);  // bidirectionnelle
                }

                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                // ---- flèches ----
                if (arc.type == 0) {
                    drawArrow(g2, p1, p2, arrowSize);
                } else if (arc.type == 1) {
                    drawArrow(g2, p1, p2, arrowSize);
                } else {
                    drawArrow(g2, p1, p2, arrowSize);
                    drawArrow(g2, p2, p1, arrowSize);
                }
            }
        }

        // ============= DESSIN DES SOMMETS =====================
        for (String node : adjacencyList.keySet()) {

            Point p = positions.get(node);

            int q = sommets.get(node).getQuartier();     // <-- QUARTIER ICI
            g2.setColor(getCouleurQuartier(q));
            g2.fillOval(p.x - 15, p.y - 15, 30, 30);

            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 15, p.y - 15, 30, 30);

            g2.drawString(node, p.x - 4, p.y + 4);
        }
    }

    private void drawArrow(Graphics2D g2, Point p1, Point p2, int size) {
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        int x = p2.x - (int)(Math.cos(angle) * 15);
        int y = p2.y - (int)(Math.sin(angle) * 15);

        Polygon arrow = new Polygon();
        arrow.addPoint(x, y);
        arrow.addPoint(x - (int)(size * Math.cos(angle - Math.PI / 6)),
                y - (int)(size * Math.sin(angle - Math.PI / 6)));
        arrow.addPoint(x - (int)(size * Math.cos(angle + Math.PI / 6)),
                y - (int)(size * Math.sin(angle + Math.PI / 6)));

        g2.fill(arrow);
    }
}
