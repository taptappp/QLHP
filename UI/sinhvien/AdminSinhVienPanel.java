package UI.sinhvien;

import model.sinhvien;
import service.SinhvienService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminSinhVienPanel extends JPanel {

    private final SinhvienService service = new SinhvienService();

    private JTable table;
    private DefaultTableModel model;
    private List<sinhvien> allStudents;

    private JTextField txtMaSV, txtHoTen, txtLop, txtNganh, txtKhoa, txtEmail, txtSDT;
    private JTextField txtUsername, txtPassword;
    private JTextField txtTimSV;

    public AdminSinhVienPanel() {
        setLayout(new BorderLayout(8, 8));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("QUẢN LÝ THÔNG TIN SINH VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        topPanel.add(lblTitle, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("🔍 Tìm sinh viên"));
        txtTimSV = new JTextField(25);
        searchPanel.add(txtTimSV);
        topPanel.add(searchPanel);
        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Mã SV", "Họ tên", "Lớp", "Ngành", "Khóa học", "Email", "SDT", "Username"}, 0
        );
        table = new JTable(model);
        beautifyTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaSV = new JTextField();
        txtHoTen = new JTextField();
        txtLop = new JTextField();
        txtNganh = new JTextField();
        txtKhoa = new JTextField();
        txtEmail = new JTextField();
        txtSDT = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();

        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setBackground(Color.WHITE);
        emailPanel.add(txtEmail, BorderLayout.CENTER);
        emailPanel.add(new JLabel("@gmail.com"), BorderLayout.EAST);

        String[] labels = {"Mã SV", "Họ tên", "Lớp", "Ngành", "Khóa học", "Email", "SDT", "Username", "Password"};
        Component[] fields = {txtMaSV, txtHoTen, txtLop, txtNganh, txtKhoa, emailPanel, txtSDT, txtUsername, txtPassword};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            formPanel.add(fields[i], gbc);
        }

        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnAdd = createButton("Thêm");
        JButton btnEdit = createButton("Sửa");
        JButton btnDelete = createButton("Xóa");
        JButton btnReset = createButton("Hiển thị DS");
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReset);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();

        btnAdd.addActionListener(e -> themSinhVien());
        btnEdit.addActionListener(e -> capNhatSinhVien());
        btnDelete.addActionListener(e -> xoaSinhVien());
        btnReset.addActionListener(e -> {
            clearForm();
            loadData();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        txtTimSV.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterSinhVien(); }
            public void removeUpdate(DocumentEvent e) { filterSinhVien(); }
            public void changedUpdate(DocumentEvent e) { filterSinhVien(); }
        });
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(33, 150, 243));
        return btn;
    }

    private void beautifyTable(JTable table) {
        table.setRowHeight(30);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private void loadData() {
        model.setRowCount(0);
        allStudents = service.layDanhSachSinhVien();
        for (sinhvien sv : allStudents) {
            model.addRow(new Object[]{
                    sv.getMaSV(), sv.getHoTen(), sv.getLop(),
                    sv.getNganh(), sv.getKhoaHoc(),
                    sv.getEmail(), sv.getSdt(), sv.getUsername()
            });
        }
    }

    private void filterSinhVien() {
        String key = txtTimSV.getText().toLowerCase().trim();
        model.setRowCount(0);
        for (sinhvien sv : allStudents) {
            if (sv.getMaSV().toLowerCase().contains(key)
                    || sv.getHoTen().toLowerCase().contains(key)
                    || (sv.getSdt() != null && sv.getSdt().contains(key))) {
                model.addRow(new Object[]{
                        sv.getMaSV(), sv.getHoTen(), sv.getLop(),
                        sv.getNganh(), sv.getKhoaHoc(),
                        sv.getEmail(), sv.getSdt(), sv.getUsername()
                });
            }
        }
    }

    private void themSinhVien() {
        String ma = txtMaSV.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String lop = txtLop.getText().trim();
        String nganh = txtNganh.getText().trim();
        String khoa = txtKhoa.getText().trim();
        String email = txtEmail.getText().trim();
        String sdt = txtSDT.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (ma.isEmpty() || hoTen.isEmpty() || lop.isEmpty() || nganh.isEmpty()
                || khoa.isEmpty() || email.isEmpty()
                || sdt.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (phải đủ 10 chữ số)!");
            return;
        }

        for (sinhvien sv : allStudents) {
            if (sv.getSdt() != null && sv.getSdt().equals(sdt)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại!");
                return;
            }
        }

        sinhvien sv = new sinhvien(
                ma, hoTen, lop, nganh, khoa,
                email + "@gmail.com", sdt
        );

        boolean ok = service.themSinhVien(username, password, "sinhvien", sv)
                == SinhvienService.AddResult.SUCCESS;

        if (ok) {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thất bại!");
        }
    }

    private void capNhatSinhVien() {
        sinhvien sv = service.layThongTinSinhVienTheoMaSV(txtMaSV.getText());
        if (sv == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên!");
            return;
        }

        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty() || !sdt.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (10 chữ số)!");
            return;
        }

        for (sinhvien svOther : allStudents) {
            if (!svOther.getMaSV().equals(sv.getMaSV())
                    && svOther.getSdt() != null
                    && svOther.getSdt().equals(sdt)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại!");
                return;
            }
        }

        sv.setHoTen(txtHoTen.getText());
        sv.setLop(txtLop.getText());
        sv.setNganh(txtNganh.getText());
        sv.setKhoaHoc(txtKhoa.getText());
        sv.setEmail(txtEmail.getText() + "@gmail.com");
        sv.setSdt(sdt);

        String newPass = txtPassword.getText().trim();
        if (newPass.isEmpty()) newPass = null;

        boolean ok = service.capNhatSinhVien(sv, newPass);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thất bại!");
        }
    }

    private void xoaSinhVien() {
        if (JOptionPane.showConfirmDialog(this, "Xóa sinh viên này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            boolean ok = service.xoaSinhVien(txtMaSV.getText());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String maSV = model.getValueAt(row, 0).toString();
        sinhvien sv = service.layThongTinSinhVienTheoMaSV(maSV);
        if (sv == null) return;

        txtMaSV.setText(sv.getMaSV());
        txtHoTen.setText(sv.getHoTen());
        txtLop.setText(sv.getLop());
        txtNganh.setText(sv.getNganh());
        txtKhoa.setText(sv.getKhoaHoc());
        txtEmail.setText(sv.getEmail().split("@")[0]);
        txtSDT.setText(sv.getSdt());
        txtUsername.setText(sv.getUsername());
        txtPassword.setText(sv.getPassword());

        txtMaSV.setEditable(false);
        txtUsername.setEditable(false);
    }

    private void clearForm() {
        txtMaSV.setText("");
        txtHoTen.setText("");
        txtLop.setText("");
        txtNganh.setText("");
        txtKhoa.setText("");
        txtEmail.setText("");
        txtSDT.setText("");
        txtUsername.setText("");
        txtPassword.setText("");

        txtMaSV.setEditable(true);
        txtUsername.setEditable(true);
    }
}
