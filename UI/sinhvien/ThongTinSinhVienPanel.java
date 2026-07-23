package UI.sinhvien;

import model.login;
import model.sinhvien;
import DAO.sinhvienDAO;

import javax.swing.*;
import java.awt.*;

public class ThongTinSinhVienPanel extends JPanel {

    private JTextField txtEmail;
    private JTextField txtSDT;
    private JButton btnEdit, btnSave;
    private sinhvien sv;
    private sinhvienDAO dao = new sinhvienDAO();

    public ThongTinSinhVienPanel(login account) {

        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));

        sv = dao.timTheoMaSinhVien(account.getMaSV());
        JLabel title = new JLabel("SƠ YẾU LÝ LỊCH SINH VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(255, 255, 255));
        content.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;

        addLabel(content, gbc, 0, 0, "Mã sinh viên :");
        addValue(content, gbc, 1, 0, sv.getMaSV());

        addLabel(content, gbc, 2, 0, "Lớp :");
        addValue(content, gbc, 3, 0, sv.getLop());

        addLabel(content, gbc, 0, 1, "Họ và tên :");
        addValue(content, gbc, 1, 1, sv.getHoTen());

        addLabel(content, gbc, 2, 1, "Ngành :");
        addValue(content, gbc, 3, 1, sv.getNganh());

        addLabel(content, gbc, 0, 2, "Email :");
        txtEmail = new JTextField(safe(sv.getEmail()), 22);
        txtEmail.setEditable(false);
        txtEmail.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        txtEmail.setBackground(Color.WHITE);
        txtEmail.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        gbc.gridx = 1;
        gbc.gridy = 2;
        content.add(txtEmail, gbc);

        addLabel(content, gbc, 2, 2, "Khóa :");
        addValue(content, gbc, 3, 2, sv.getKhoaHoc());

        addLabel(content, gbc, 0, 3, "Số điện thoại :");
        txtSDT = new JTextField(safe(sv.getSdt()), 22);
        txtSDT.setEditable(false);
        txtSDT.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        txtSDT.setBackground(Color.WHITE);
        txtSDT.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        gbc.gridx = 1;
        gbc.gridy = 3;
        content.add(txtSDT, gbc);

        add(content, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(255, 255, 255));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 50));

        btnEdit = createButton("Cập nhật");
        btnSave = createButton("Lưu");

        btnSave.setEnabled(false);

        btnPanel.add(btnEdit);
        btnPanel.add(btnSave);
        add(btnPanel, BorderLayout.SOUTH);

        btnEdit.addActionListener(e -> {
            txtEmail.setEditable(true);
            txtSDT.setEditable(true);
            txtEmail.requestFocus();
            btnSave.setEnabled(true);
            btnEdit.setEnabled(false);
        });

        btnSave.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String sdt = txtSDT.getText().trim();

            if (email.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Email hoặc số điện thoại không được để trống!");
                return;
            }

            if (!email.matches("^[\\w.-]+@gmail\\.com$")) {
                JOptionPane.showMessageDialog(this,
                        "Email không hợp lệ! Phải là Gmail (@gmail.com)");
                return;
            }

            if (!sdt.matches("^0\\d{9}$")) {
                JOptionPane.showMessageDialog(this,
                        "Số điện thoại không hợp lệ (10 số, bắt đầu 0)!");
                return;
            }

            sv.setEmail(email);
            sv.setSdt(sdt);

            dao.capNhatSinhVien(sv, sv.getPassword());

            txtEmail.setEditable(false);
            txtSDT.setEditable(false);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(true);

            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
        });

    }

    private void addLabel(JPanel p, GridBagConstraints gbc,
                          int x, int y, String text) {
        gbc.gridx = x;
        gbc.gridy = y;
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 17)); // IN ĐẬM
        p.add(lbl, gbc);
    }

    private void addValue(JPanel p, GridBagConstraints gbc,
                          int x, int y, String text) {
        gbc.gridx = x;
        gbc.gridy = y;
        JLabel lbl = new JLabel(safe(text));
        lbl.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        p.add(lbl, gbc);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
