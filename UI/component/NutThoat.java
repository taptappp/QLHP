package UI.component;

import javax.swing.*;
import java.awt.*;

public class NutThoat extends JButton {
    public NutThoat() {
        setText("X");
        setFont(QuanLyFont.customFont17);
        setForeground(Color.RED);
        setFocusPainted(false);
        setBorder(null);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(NutThoat.this);
            if (win != null) {
                win.dispose();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
