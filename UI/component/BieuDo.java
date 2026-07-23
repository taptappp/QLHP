package UI.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BieuDo extends JPanel {

    private String[] labelsCot;
    private int[] valuesCot;
    private String[] labelsTron;
    private double[] valuesTron;
    private boolean isCot = false;
    private boolean isTron = false;
    private Color colorStart = new Color(33, 150, 243);
    private Color colorEnd = new Color(144, 202, 249);
    private Color colorBorderAndText = new Color(33, 150, 243);
    private int hoverIndex = -1;
    private int pieHoverIndex = -1;
    private String tooltipText = "";

    public BieuDo() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 300));
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (isCot) handleHoverBar(e.getX(), e.getY());
                if (isTron) handleHoverPie(e.getX(), e.getY());
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoverIndex = -1;
                pieHoverIndex = -1;
                tooltipText = "";
                repaint();
            }
        });
    }
    public void setDataCot(String[] labels, int[] values) {
        this.labelsCot = labels;
        this.valuesCot = values;
        this.isCot = true;
        this.isTron = false;
        repaint();
    }
    public void setDataTron(String[] labels, double[] values) {
        this.labelsTron = labels;
        this.valuesTron = values;
        this.isTron = true;
        this.isCot = false;
        repaint();
    }
    private void handleHoverBar(int mx, int my) {
        hoverIndex = -1;
        tooltipText = "";
        if (valuesCot == null) return;

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int chartHeight = height - padding * 2;
        int barWidth = Math.max(20, (width - padding * 2) / Math.max(valuesCot.length, 1) - 10);
        int max = 0;
        for (int v : valuesCot) max = Math.max(max, v);
        if (max == 0) max = 1;

        for (int i = 0; i < valuesCot.length; i++) {
            int barHeight = (int) ((valuesCot[i] * 1.0 / max) * chartHeight);
            int x = padding + i * (barWidth + 10);
            int y = height - padding - barHeight;
            if (mx >= x && mx <= x + barWidth && my >= y && my <= height - padding) {
                hoverIndex = i;
                tooltipText = labelsCot[i] + ": " + valuesCot[i];
                break;
            }
        }
    }
    private void handleHoverPie(int mx, int my) {
        pieHoverIndex = -1;
        tooltipText = "";
        if (valuesTron == null) return;

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) * 3 / 5;
        int cx = width / 2;
        int cy = height / 2;

        double sum = 0;
        for (double v : valuesTron) sum += v;

        double startAngle = 0;
        double mxRel = mx - cx;
        double myRel = cy - my;
        double dist = Math.sqrt(mxRel*mxRel + myRel*myRel);
        if (dist > diameter/2) return;

        double angleMouse = Math.toDegrees(Math.atan2(myRel, mxRel));
        if (angleMouse < 0) angleMouse += 360;

        for (int i = 0; i < valuesTron.length; i++) {
            double angle = valuesTron[i] / sum * 360;
            if (angleMouse >= startAngle && angleMouse <= startAngle + angle) {
                pieHoverIndex = i;
                tooltipText = labelsTron[i] + ": " + String.format("%.1f", valuesTron[i]);
                break;
            }
            startAngle += angle;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float scale = Math.min(getWidth() / 400f, getHeight() / 300f);

        if (isCot) drawCot(g2, scale);
        if (isTron) drawTron(g2, scale);

        if (!tooltipText.isEmpty()) {
            g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(10, Math.round(12*scale))));
            int tw = g2.getFontMetrics().stringWidth(tooltipText) + 10;
            int th = g2.getFontMetrics().getHeight();
            Point mp = getMousePosition();
            if (mp != null) {
                g2.setColor(new Color(0,0,0,200));
                g2.fillRoundRect(mp.x, mp.y - th, tw, th, 5, 5);
                g2.setColor(Color.WHITE);
                g2.drawString(tooltipText, mp.x + 5, mp.y - 5);
            }
        }
    }

    private void drawCot(Graphics2D g2, float scale) {
        if (valuesCot == null || valuesCot.length == 0) return;

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int chartHeight = height - padding * 2;
        int max = 0;
        for (int v : valuesCot) max = Math.max(max, v);
        if (max == 0) max = 1;

        int barWidth = Math.max(20, (width - padding * 2) / Math.max(valuesCot.length,1) - 10);

        g2.setColor(new Color(colorBorderAndText.getRed(), colorBorderAndText.getGreen(),
                colorBorderAndText.getBlue(), 50));
        for (int i = 0; i <= 5; i++) {
            int y = padding + i * chartHeight / 5;
            g2.drawLine(padding, y, width - padding, y);
        }

        for (int i = 0; i < valuesCot.length; i++) {
            int barHeight = (int) ((valuesCot[i] * 1.0 / max) * chartHeight);
            int x = padding + i * (barWidth + 10);
            int y = height - padding - barHeight;

            g2.setColor(new Color(0,0,0,50));
            g2.fillRoundRect(x+2, y+2, barWidth, barHeight, 15, 15);

            GradientPaint gp = new GradientPaint(x, y, colorStart, x, y + barHeight, colorEnd);
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, barWidth, barHeight, 15, 15);

            g2.setColor(colorBorderAndText);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(x, y, barWidth, barHeight, 15, 15);

            g2.setFont(new Font("Segoe UI", Font.BOLD, Math.round(14*scale)));
            g2.setColor(colorBorderAndText);
            String val = String.valueOf(valuesCot[i]);
            int valWidth = g2.getFontMetrics().stringWidth(val);
            g2.drawString(val, x + barWidth/2 - valWidth/2, y - 8);

            String label = labelsCot[i];
            int availableWidth = barWidth + 10;
            Font font = new Font("Segoe UI", Font.PLAIN, Math.round(13*scale));
            g2.setFont(font);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            if(labelWidth > availableWidth) {
                float newSize = Math.max(8, font.getSize2D() * availableWidth / labelWidth);
                g2.setFont(font.deriveFont(newSize));
                labelWidth = g2.getFontMetrics().stringWidth(label);
            }
            g2.drawString(label, x + barWidth/2 - labelWidth/2, height - padding + 20);
            if(i < valuesCot.length - 1) {
                int sepX = x + barWidth + 5;
                g2.setColor(new Color(0,0,0,50));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(sepX, padding, sepX, height - padding);
            }
        }
        g2.setColor(new Color(0,0,0,50));
        g2.setStroke(new BasicStroke(1f));
        for(int i=0;i<valuesCot.length-1;i++){
            int sepX = padding + i*(barWidth+10) + barWidth + 5;
            g2.drawLine(sepX, height - padding +5, sepX, height - padding +30);
        }
        g2.setColor(colorBorderAndText);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(padding, height - padding, width - padding, height - padding);
    }
    private void drawTron(Graphics2D g2, float scale) {
        if (valuesTron == null || valuesTron.length == 0) return;

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height) * 3 / 5;
        int cx = width / 2;
        int cy = height / 2;
        double sum = 0;
        for (double v : valuesTron) sum += v;
        double startAngle = 0;
        Color[] colors = {colorStart, new Color(76,175,80), new Color(255,152,0),
                new Color(156,39,176), new Color(244,67,54), new Color(255,235,59)};
        for (int i = 0; i < valuesTron.length; i++) {
            double angle = valuesTron[i] / sum * 360;
            int drawDiameter = diameter;
            if (pieHoverIndex == i) drawDiameter += 15;
            g2.setColor(colors[i % colors.length]);
            g2.fillArc(cx - drawDiameter/2, cy - drawDiameter/2, drawDiameter, drawDiameter,
                    (int)startAngle, (int)angle);
            g2.setColor(colorBorderAndText);
            g2.setStroke(new BasicStroke(2f));
            g2.drawArc(cx - drawDiameter/2, cy - drawDiameter/2, drawDiameter, drawDiameter,
                    (int)startAngle, (int)angle);
            double midAngle = startAngle + angle/2;
            double rad = Math.toRadians(midAngle);
            int textRadius = drawDiameter / 2 + 20;
            int x = (int)(cx + Math.cos(rad) * textRadius);
            int y = (int)(cy - Math.sin(rad) * textRadius);
            g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(10, Math.round(12*scale))));
            String percent = String.format("%.1f%%", valuesTron[i]/sum*100);
            int textWidth = g2.getFontMetrics().stringWidth(percent);
            g2.setColor(colorBorderAndText);
            g2.drawString(percent, x - textWidth/2, y + g2.getFontMetrics().getAscent()/2);
            startAngle += angle;
        }
    }
}
