package UI.giangvien;

import model.GiangVien;
import service.GiangVienService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyGiangVienPanel extends JPanel {

    private final GiangVienService service = new GiangVienService();

    private JTable table;
    private DefaultTableModel model;
    private List<GiangVien> allGiangVien;

    private JTextField txtMaGV, txtHoTen, txtHocVi, txtEmail, txtSDT;
    private JTextField txtTim;

    public QuanLyGiangVienPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("QUẢN LÝ GIẢNG VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Tìm giảng viên:"));
        txtTim = new JTextField(20);
        searchPanel.add(txtTim);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);


        model = new DefaultTableModel(
                new String[]{"Mã GV", "Họ tên", "Học vị", "Email", "SĐT"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionBackground(new Color(220, 235, 250));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách giảng viên"));
        add(scroll, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin giảng viên"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaGV = createField();
        txtHoTen = createField();
        txtHocVi = createField();

        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setBackground(Color.WHITE);
        txtEmail = createField();
        JLabel lblDomain = new JLabel("@gmail.com");
        lblDomain.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        emailPanel.add(txtEmail, BorderLayout.CENTER);
        emailPanel.add(lblDomain, BorderLayout.EAST);

        txtSDT = createField();

        String[] labels = {"Mã GV", "Họ tên", "Học vị", "Email", "SĐT"};
        Component[] fields = {txtMaGV, txtHoTen, txtHocVi, emailPanel, txtSDT};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            formPanel.add(fields[i], gbc);
        }


        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 0, 12));
        btnPanel.setBackground(Color.WHITE);

        JButton btnAdd = createButton("Thêm");
        JButton btnEdit = createButton("Sửa");
        JButton btnDelete = createButton("Xóa");
        JButton btnReset = createButton("Làm mới");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReset);

        JPanel bottom = new JPanel(new BorderLayout(15, 0));
        bottom.setBackground(Color.WHITE);
        bottom.add(formPanel, BorderLayout.CENTER);
        bottom.add(btnPanel, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        loadData();

        btnAdd.addActionListener(e -> themGiangVien());
        btnEdit.addActionListener(e -> capNhatGiangVien());
        btnDelete.addActionListener(e -> xoaGiangVien());
        btnReset.addActionListener(e -> {
            lamMoi();
            loadData();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) doDuLieuLen();
        });

        txtTim.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { locGiangVien(); }
            public void removeUpdate(DocumentEvent e) { locGiangVien(); }
            public void changedUpdate(DocumentEvent e) { locGiangVien(); }
        });
    }

    private JTextField createField() {
        JTextField txt = new JTextField(20);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return txt;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 34));
        return btn;
    }

    private boolean kiemTraSDTHopLe(String sdt) {
        return sdt.matches("0\\d{9}");
    }

    private void loadData() {
        model.setRowCount(0);
        allGiangVien = service.layTatCaGiangVien();

        for (GiangVien gv : allGiangVien) {
            model.addRow(new Object[]{
                    gv.getMaGV(),
                    gv.getHoTen(),
                    gv.getHocVi(),
                    gv.getEmail(),
                    gv.getSDT()
            });
        }
        txtMaGV.setEditable(true);
    }

    private void locGiangVien() {
        String key = txtTim.getText().toLowerCase().trim();
        model.setRowCount(0);

        for (GiangVien gv : allGiangVien) {
            if (gv.getMaGV().toLowerCase().contains(key)
                    || gv.getHoTen().toLowerCase().contains(key)
                    || gv.getHocVi().toLowerCase().contains(key)
                    || gv.getEmail().toLowerCase().contains(key)
                    || gv.getSDT().toLowerCase().contains(key)) {

                model.addRow(new Object[]{
                        gv.getMaGV(),
                        gv.getHoTen(),
                        gv.getHocVi(),
                        gv.getEmail(),
                        gv.getSDT()
                });
            }
        }
    }

    private void themGiangVien() {
        if (txtMaGV.getText().isEmpty()
                || txtHoTen.getText().isEmpty()
                || txtHocVi.getText().isEmpty()
                || txtEmail.getText().isEmpty()
                || txtSDT.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Không được để trống!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GiangVien gv = new GiangVien(
                txtMaGV.getText(),
                txtHoTen.getText(),
                txtHocVi.getText(),
                txtEmail.getText() + "@gmail.com",
                txtSDT.getText()
        );

        if (service.themGiangVien(gv)) {
            loadData();
            lamMoi();
            JOptionPane.showMessageDialog(this, "Thêm giảng viên thành công!");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm thất bại! SDT không hợp lệ hoặc đã tồn tại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatGiangVien() {
        GiangVien gv = service.timTheoMaGiangVien(txtMaGV.getText());
        if (gv == null) return;

        gv.setHoTen(txtHoTen.getText());
        gv.setHocVi(txtHocVi.getText());
        gv.setEmail(txtEmail.getText() + "@gmail.com");
        gv.setSDT(txtSDT.getText());

        if (service.capNhatGiangVien(gv)) {
            loadData();
            lamMoi();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật thất bại! SDT không hợp lệ hoặc đã tồn tại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void xoaGiangVien() {
        if (JOptionPane.showConfirmDialog(
                this,
                "Xóa giảng viên này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            service.xoaGiangVien(txtMaGV.getText());
            loadData();
            lamMoi();
        }
    }
    private void doDuLieuLen() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        String maGV = model.getValueAt(row, 0).toString();
        GiangVien gv = service.timTheoMaGiangVien(maGV);
        if (gv == null) return;

        txtMaGV.setText(gv.getMaGV());
        txtHoTen.setText(gv.getHoTen());
        txtHocVi.setText(gv.getHocVi());
        txtEmail.setText(gv.getEmail().split("@")[0]);
        txtSDT.setText(gv.getSDT());
        txtMaGV.setEditable(false);
    }
    private void lamMoi() {
        txtMaGV.setText("");
        txtHoTen.setText("");
        txtHocVi.setText("");
        txtEmail.setText("");
        txtSDT.setText("");
        txtMaGV.setEditable(true);
    }
}
