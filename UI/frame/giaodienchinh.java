package UI.frame;

import UI.component.PanelBoGoc;
import UI.thongke.ThongKePanel;
import UI.thongke.ThongKeSinhVienPanel;
import model.login;

import javax.swing.*;
import java.awt.*;

public abstract class giaodienchinh extends JFrame {

    protected login account;

    protected PanelBoGoc sidebar;
    protected PanelBoGoc contentPanel;

    protected JButton btnMenu;
    protected boolean menuVisible = false;

    protected final int SIDEBAR_WIDTH = 200;
    protected final int MENU_TOP_GAP = 60;

    public giaodienchinh(login account) {
        this.account = account;

        setTitle("Hệ thống quản lý sinh viên");
        setSize(780, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        PanelBoGoc root = new PanelBoGoc(Color.WHITE, 40);
        root.setLayout(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        header.setOpaque(false);

        btnMenu = new JButton("≡ MENU");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 19));
        btnMenu.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnMenu.setFocusPainted(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnMenu.addActionListener(e -> toggleMenu());

        header.add(btnMenu);
        root.add(header, BorderLayout.NORTH);


        sidebar = new PanelBoGoc(new Color(30, 30, 45), 30);
        sidebar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setVisible(false);
        sidebar.add(createMenu(), BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);


        contentPanel = new PanelBoGoc(Color.WHITE, 30);
        contentPanel.setLayout(new BorderLayout());
        root.add(contentPanel, BorderLayout.CENTER);

        showDashboard();
    }


    protected void toggleMenu() {
        menuVisible = !menuVisible;
        sidebar.setVisible(menuVisible);
        sidebar.getParent().revalidate();
        sidebar.getParent().repaint();
    }

    protected void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    protected void showDashboard() {
        if (account.getRole().equalsIgnoreCase("sinhvien")) {
            showPanel(new ThongKeSinhVienPanel(account));
        } else {
            showPanel(new ThongKePanel(account));
        }
    }

    protected abstract JPanel createMenu();
    protected JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(45, 45, 60));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    protected JButton createLogoutButton() {
        JButton btn = new JButton("Đăng xuất");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(50, 57, 200));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> logout());
        return btn;
    }

    protected void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Đăng xuất",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                new UI.auth.framedangnhap().setVisible(true);
            });
        }
    }

}
