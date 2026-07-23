package UI.frame;

import UI.diem.XemDiemPanel;
import UI.hocphan.DangKyHocPhanPanel;
import UI.sinhvien.ThongTinSinhVienPanel;
import model.login;
import model.sinhvien;
import service.SinhvienService;

import javax.swing.*;

public class SinhVienFrame extends giaodienchinh {

    private final SinhvienService svService = new SinhvienService();

    public SinhVienFrame(login account) {
        super(account);
    }

    @Override
    protected JPanel createMenu() {

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnHome = createMenuButton("🏠 Tổng quan");
        JButton btnSV   = createMenuButton("👨‍🎓 Thông tin");
        JButton btnDiem = createMenuButton("📊 Điểm");
        JButton btnDKHP = createMenuButton("📚 Đăng ký học phần");

        btnHome.addActionListener(e -> {
            showDashboard();
        });

        btnSV.addActionListener(e -> {
            showPanel(new ThongTinSinhVienPanel(account));
        });

        btnDiem.addActionListener(e -> {
            sinhvien sv = svService.layThongTinSinhVien(account);
            if (sv != null) {
                showPanel(new XemDiemPanel(sv));
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin sinh viên",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDKHP.addActionListener(e -> {
            sinhvien sv = svService.layThongTinSinhVien(account);
            if (sv != null) {
                showPanel(new DangKyHocPhanPanel(sv.getMaSV()));
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin sinh viên",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        menu.add(btnHome);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnSV);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnDiem);
        menu.add(Box.createVerticalStrut(10));
        menu.add(btnDKHP);
        menu.add(Box.createVerticalGlue());

        JButton btnLogout = createLogoutButton();
        menu.add(btnLogout);

        return menu;
    }
}
