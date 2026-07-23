package UI.component;

import javax.swing.*;
import java.awt.*;

public class TheTongQuan extends PanelBoGoc {

    public TheTongQuan(String title, String value) {
        super(Color.WHITE, 25);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(new Color(33, 150, 243));
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(lblTitle);
        add(Box.createVerticalStrut(6));
        add(lblValue);
    }
}
