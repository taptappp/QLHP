package main;
import javax.swing.SwingUtilities;

import UI.auth.framedangnhap;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new framedangnhap().setVisible(true);
        });
    }
}

