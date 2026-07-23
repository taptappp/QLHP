package UI.component;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class NutmatKhau extends JPanel {

    private JLabel lblTitle;
    private JPasswordField passwordField;
    private JLabel emojiLabel;
    private int yLocate = 25;
    private Timer timer;

    public NutmatKhau(String title, String emoji) {
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
        lblTitle.setBounds(35, yLocate, 250, 30);
        add(lblTitle);

        passwordField = new JPasswordField();
        passwordField.setOpaque(false);
        passwordField.setBorder(null);
        passwordField.setFont(QuanLyFont.customFont17);
        passwordField.setBounds(35, 25, 245, 30); // căn chỉnh bên phải emoji
        add(passwordField);

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
                if (passwordField.getPassword().length == 0) {
                    animateUp();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
                if (passwordField.getPassword().length == 0) {
                    animateDown();
                }
            }
        });
    }

    public String getText() {
        return new String(passwordField.getPassword());
    }

    public void setEchoChar(char c) {
        passwordField.setEchoChar(c);
    }

    private void animateUp() {
        stopTimer();
        timer = new Timer(10, e -> {
            if (yLocate > 5) {
                yLocate -= 2;
                lblTitle.setLocation(35, yLocate);
            } else {
                stopTimer();
                lblTitle.setFont(QuanLyFont.customFont17.deriveFont(Font.PLAIN, 13f));
                lblTitle.setForeground(Color.BLACK);
            }
        });
        timer.start();
    }

    private void animateDown() {
        stopTimer();
        timer = new Timer(10, e -> {
            if (yLocate < 25) {
                yLocate += 2;
                lblTitle.setLocation(35, yLocate);
            } else {
                stopTimer();
                lblTitle.setFont(QuanLyFont.customFont17);
                lblTitle.setForeground(Color.GRAY);
            }
        });
        timer.start();
    }

    private void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }
}
