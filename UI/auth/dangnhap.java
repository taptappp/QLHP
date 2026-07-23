package UI.auth;

import UI.component.*;
import UI.frame.AdminFrame;
import UI.frame.SinhVienFrame;
import service.loginbe;
import model.login;

import javax.swing.*;
import java.awt.*;

public class dangnhap extends JPanel {

    private OvanBan txtUsername;
    private NutmatKhau txtPassword;
    private NutTuyChinh btnLogin;
    private JCheckBox cbShowPassword;
    private JComboBox<String> cbRole;

    private loginbe service = new loginbe();

    public dangnhap() {
        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 780, 580);

        PanelBoGoc mainPanel = new PanelBoGoc(Color.WHITE, 40);
        mainPanel.setLayout(null);
        mainPanel.setBounds(10, 10, 760, 560);
        add(mainPanel);

        PanelBoGoc leftPanel = new PanelBoGoc(new Color(70, 130, 180), 30);
        leftPanel.setLayout(null);
        leftPanel.setBounds(20, 20, 340, 520);
        mainPanel.add(leftPanel);

        JLabel lblWelcome = new JLabel("Have A Nice Day!", JLabel.CENTER);
        lblWelcome.setFont(new Font("Courier New", Font.BOLD, 28));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(0, 170, 340, 40);
        leftPanel.add(lblWelcome);

        JLabel lblNoAccount1 = new JLabel("Well come to our systerm", JLabel.CENTER);
        lblNoAccount1.setFont(new Font("Courier New", Font.ITALIC, 20));
        lblNoAccount1.setForeground(Color.WHITE);
        lblNoAccount1.setBounds(0, 230, 320, 30);
        leftPanel.add(lblNoAccount1);

        PanelBoGoc rightPanel = new PanelBoGoc(Color.WHITE, 30);
        rightPanel.setLayout(null);
        rightPanel.setBounds(400, 20, 340, 520);
        mainPanel.add(rightPanel);

        NutThoat btnExit = new NutThoat();
        btnExit.setBounds(290, 10, 35, 35);
        rightPanel.add(btnExit);

        JLabel lblTitle = new JLabel("Đăng nhập", JLabel.CENTER);
        lblTitle.setFont(QuanLyFont.customFont23);
        lblTitle.setBounds(0, 50, 340, 40);
        rightPanel.add(lblTitle);

        txtUsername = new OvanBan("Username", "👨‍💻");
        txtUsername.setBounds(30, 120, 280, 50);
        rightPanel.add(txtUsername);

        txtPassword = new NutmatKhau("Password", "🔒");
        txtPassword.setBounds(30, 190, 280, 50);
        rightPanel.add(txtPassword);

        JLabel lblRole = new JLabel("Role");
        lblRole.setFont(QuanLyFont.customFont17);
        lblRole.setBounds(30, 245, 100, 25);
        rightPanel.add(lblRole);

        cbRole = new JComboBox<>(new String[]{"Sinh viên", "Admin"});
        cbRole.setFont(QuanLyFont.customFont17);
        cbRole.setBounds(30, 270, 280, 30);
        rightPanel.add(cbRole);

        cbShowPassword = new JCheckBox("Hiển thị mật khẩu");
        cbShowPassword.setBounds(30, 315, 200, 25);
        cbShowPassword.setOpaque(false);
        cbShowPassword.setForeground(Color.GRAY);
        cbShowPassword.addActionListener(e ->
                txtPassword.setEchoChar(cbShowPassword.isSelected() ? (char) 0 : '•')
        );
        rightPanel.add(cbShowPassword);

        btnLogin = new NutTuyChinh("Đăng nhập");
        btnLogin.setBounds(30, 350, 280, 50);
        rightPanel.add(btnLogin);
        btnLogin.addActionListener(e -> xuLyDangNhap());
    }

    private void xuLyDangNhap() {
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String selectedRole = cbRole.getSelectedItem().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ thông tin!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        login account = service.dangNhap(user, pass);

        if (account == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Sai tên đăng nhập hoặc mật khẩu!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (selectedRole.equalsIgnoreCase("Sinh viên")) {
            selectedRole = "sinhvien";
        } else if (selectedRole.equalsIgnoreCase("Admin")) {
            selectedRole = "admin";
        }

        if (!account.getRole().equalsIgnoreCase(selectedRole)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng kiểm tra lại thông tin đăng nhập!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (loginFrame != null) {
            loginFrame.dispose();
        }

        SwingUtilities.invokeLater(() -> {
            if (account.getRole().equalsIgnoreCase("admin")) {
                new AdminFrame(account).setVisible(true);
            } else {
                new SinhVienFrame(account).setVisible(true);
            }
        });
    }
}
