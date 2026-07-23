package UI.component;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class OvanBan extends JPanel {

    private JLabel lblTitle;
    private JTextField textField;
    private JLabel emojiLabel;
    private int yLocate = 25;
    private Timer timer;

    public OvanBan(String title, String emoji) {
        setLayout(null);
        setOpaque(false);
        setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

        emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        emojiLabel.setBounds(0, 25, 30, 30);
        add(emojiLabel);

        lblTitle = new JLabel(title);
        lblTitle.setFont(QuanLyFont.customFont17);
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setBounds(35, yLocate, 250, 30); // cách emoji 5px
        add(lblTitle);

        textField = new JTextField();
        textField.setOpaque(false);
        textField.setBorder(null);
        textField.setFont(QuanLyFont.customFont17);
        textField.setBounds(35, 25, 245, 30); // căn chỉnh bên phải emoji
        add(textField);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
                if (textField.getText().isEmpty()) {
                    animateUp();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
                if (textField.getText().isEmpty()) {
                    animateDown();
                }
            }
        });
    }

    private void animateUp() {
        if (timer != null && timer.isRunning()) timer.stop();
        timer = new Timer(10, e -> {
            if (yLocate > 0) {
                yLocate -= 2;
                lblTitle.setBounds(35, yLocate, 250, 30);
            } else {
                timer.stop();
                lblTitle.setFont(QuanLyFont.customFont17.deriveFont(Font.PLAIN, 13f));
                lblTitle.setForeground(Color.BLACK);
            }
        });
        timer.start();
    }

    private void animateDown() {
        if (timer != null && timer.isRunning()) timer.stop();
        timer = new Timer(10, e -> {
            if (yLocate < 25) {
                yLocate += 2;
                lblTitle.setBounds(35, yLocate, 250, 30);
            } else {
                timer.stop();
                lblTitle.setFont(QuanLyFont.customFont17);
                lblTitle.setForeground(Color.GRAY);
            }
        });
        timer.start();
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
        if (!text.isEmpty()) {
            animateUp();
        }
    }
}
