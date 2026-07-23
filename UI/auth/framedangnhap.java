package UI.auth;

import javax.swing.*;
import java.awt.*;

public class framedangnhap extends JFrame {

    public framedangnhap() {
        setTitle("Đăng nhập");
        setSize(780, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new dangnhap());
    }
}
