package UI.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NutTuyChinh extends JButton {

    private Color defaultBg = new Color(70, 130, 180);
    private Color hoverBg = new Color(100, 149, 237);

    public NutTuyChinh(String text) {
        super(text);
        setFont(QuanLyFont.customFont17);
        setForeground(Color.WHITE);
        setBackground(defaultBg);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBg);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBg);
                repaint();
            }
        });
    }

    public void setDefaultBackground(Color c) {
        this.defaultBg = c;
        setBackground(c);
        repaint();
    }

    public void setHoverBackground(Color c) {
        this.hoverBg = c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();
        g2.setColor(getForeground());
        g2.drawString(
                getText(),
                (getWidth() - textWidth) / 2,
                (getHeight() + textHeight) / 2 - 3
        );

        g2.dispose();
    }
}
