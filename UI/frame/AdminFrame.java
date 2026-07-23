package UI.frame;

import UI.hocphan.LopHocPhanPanel;
import model.login;
import UI.sinhvien.AdminSinhVienPanel;
import UI.hocphan.QuanLyHocPhanPanel;
import UI.giangvien.QuanLyGiangVienPanel;
import UI.diem.QuanLyDiemPanel;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends giaodienchinh {

    public AdminFrame(login account) {
        super(account);
    }

    @Override
    protected JPanel createMenu() {

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        menu.add(Box.createVerticalStrut(MENU_TOP_GAP));

        JButton btnHome = createMenuButton("🏠 Tổng quan");
        btnHome.addActionListener(e -> showDashboard());
        menu.add(btnHome);
        menu.add(Box.createVerticalStrut(20));

        JLabel lblQuanLy = new JLabel("QUẢN LÝ");
        lblQuanLy.setForeground(new Color(180, 180, 180));
        lblQuanLy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblQuanLy.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(lblQuanLy);
        menu.add(Box.createVerticalStrut(10));

        JButton btnSV   = createMenuButton("👨‍🎓 Sinh viên");
        JButton btnDiem = createMenuButton("📊 Điểm");
        JButton btnHP   = createMenuButton("📘 Học phần");
        JButton btnGV   = createMenuButton("👨‍🏫 Giảng viên");
        JButton btnLHP  = createMenuButton("🗓️ Lớp học phần");

        btnSV.addActionListener(e -> showPanel(new AdminSinhVienPanel()));
        btnDiem.addActionListener(e -> showPanel(new QuanLyDiemPanel()));
        btnHP.addActionListener(e -> showPanel(new QuanLyHocPhanPanel()));
        btnGV.addActionListener(e -> showPanel(new QuanLyGiangVienPanel()));
        btnLHP.addActionListener(e -> showPanel(new LopHocPhanPanel()));

        menu.add(btnSV);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnDiem);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnHP);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnGV);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnLHP);

        menu.add(Box.createVerticalGlue());
        JButton btnLogout = createLogoutButton();
        menu.add(btnLogout);

        return menu;
    }
}
