package UI.diem;

import model.Diem;
import model.sinhvien;
import service.DiemService;
import service.SinhvienService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyDiemPanel extends JPanel {

    private final SinhvienService svService = new SinhvienService();
    private final DiemService diemService = new DiemService();

    private JTable tblSinhVien, tblDiem;
    private DefaultTableModel modelSV, modelDiem;
    private List<sinhvien> allStudents;

    private JTextField txtMaSV, txtHoTen, txtLop, txtNganh, txtKhoa, txtEmail, txtTimSV;
    private JLabel lblThongBao;
    private JButton btnLuuDiem, btnLoadDuLieu;

    public QuanLyDiemPanel() {
        setLayout(new BorderLayout(8,8));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("QUẢN LÝ ĐIỂM SINH VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10,15,0,0));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        searchPanel.add(new JLabel("Tìm sinh viên:"));
        txtTimSV = new JTextField(20);
        searchPanel.add(txtTimSV);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.4);

        modelSV = new DefaultTableModel(
                new String[]{"Mã SV","Họ tên","Lớp","Ngành","Khóa","Email"},0);
        tblSinhVien = new JTable(modelSV);
        tblSinhVien.setRowHeight(28);
        split.setLeftComponent(new JScrollPane(tblSinhVien));

        JPanel rightPanel = new JPanel(new BorderLayout(5,5));
        rightPanel.setBackground(Color.WHITE);

        JPanel info = new JPanel(new GridLayout(3,4,8,8));
        info.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        txtMaSV = taoTruongThongTin();
        txtHoTen = taoTruongThongTin();
        txtLop = taoTruongThongTin();
        txtNganh = taoTruongThongTin();
        txtKhoa = taoTruongThongTin();
        txtEmail = taoTruongThongTin();

        info.add(new JLabel("Mã SV")); info.add(txtMaSV);
        info.add(new JLabel("Họ tên")); info.add(txtHoTen);
        info.add(new JLabel("Lớp")); info.add(txtLop);
        info.add(new JLabel("Ngành")); info.add(txtNganh);
        info.add(new JLabel("Khóa")); info.add(txtKhoa);
        info.add(new JLabel("Email")); info.add(txtEmail);

        rightPanel.add(info, BorderLayout.NORTH);

        modelDiem = new DefaultTableModel(
                new String[]{"Mã HP","Tên học phần","Điểm QT","Điểm Thi","Điểm TB"},0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2 || col == 3; // chỉ điểm QT và Thi được sửa
            }
        };
        tblDiem = new JTable(modelDiem);
        tblDiem.setRowHeight(28);
        rightPanel.add(new JScrollPane(tblDiem), BorderLayout.CENTER);

        btnLuuDiem = createButtonBlue("Lưu điểm");
        btnLoadDuLieu = createButtonBlue("Hiển thị tất cả");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.add(btnLuuDiem);
        btnPanel.add(btnLoadDuLieu);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);

        split.setRightComponent(rightPanel);
        add(split, BorderLayout.CENTER);

        lblThongBao = new JLabel(" ", JLabel.CENTER);
        lblThongBao.setForeground(Color.RED);
        add(lblThongBao, BorderLayout.SOUTH);

        taiDanhSachSinhVien();

        tblSinhVien.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) taiThongTinVaDiem();
        });

        btnLuuDiem.addActionListener(e -> luuDiem());

        btnLoadDuLieu.addActionListener(e -> {
            txtTimSV.setText("");
            taiDanhSachSinhVien();
        });

        txtTimSV.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { locDanhSachSinhVien(); }
            @Override public void removeUpdate(DocumentEvent e) { locDanhSachSinhVien(); }
            @Override public void changedUpdate(DocumentEvent e) { locDanhSachSinhVien(); }
        });
    }

    private JTextField taoTruongThongTin() {
        JTextField txt = new JTextField();
        txt.setEditable(false);
        return txt;
    }

    private JButton createButtonBlue(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(33, 150, 243));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 34));
        return btn;
    }

    private void taiDanhSachSinhVien() {
        modelSV.setRowCount(0);
        allStudents = svService.layDanhSachSinhVien();
        for(sinhvien sv : allStudents){
            modelSV.addRow(new Object[]{
                    sv.getMaSV(), sv.getHoTen(), sv.getLop(),
                    sv.getNganh(), sv.getKhoaHoc(), sv.getEmail()
            });
        }
    }

    private void taiThongTinVaDiem() {
        modelDiem.setRowCount(0);
        int row = tblSinhVien.getSelectedRow();
        if(row < 0) return;

        String maSV = modelSV.getValueAt(row,0).toString();
        sinhvien sv = svService.layThongTinSinhVienTheoMaSV(maSV);
        if(sv == null) return;

        txtMaSV.setText(sv.getMaSV());
        txtHoTen.setText(sv.getHoTen());
        txtLop.setText(sv.getLop());
        txtNganh.setText(sv.getNganh());
        txtKhoa.setText(sv.getKhoaHoc());
        txtEmail.setText(sv.getEmail());

        for(Diem d : diemService.layDiemTheoSinhVien(maSV)) {
            modelDiem.addRow(new Object[]{
                    d.getMaHP(), d.getTenHP(),
                    d.getDiemQT(), d.getDiemThi(), d.getDiemTB()
            });
        }
    }

    private void luuDiem() {
        if (tblDiem.isEditing()) tblDiem.getCellEditor().stopCellEditing();

        boolean allOk = true;
        String maSV = txtMaSV.getText();
        List<Diem> listDangKy = diemService.layDiemTheoSinhVien(maSV);

        for (int i = 0; i < modelDiem.getRowCount(); i++) {
            String maHP = modelDiem.getValueAt(i, 0).toString();
            String tenHP = modelDiem.getValueAt(i, 1).toString();

            Float qt = chuyenSangSoThuc(modelDiem.getValueAt(i, 2));
            Float thi = chuyenSangSoThuc(modelDiem.getValueAt(i, 3));

            String maDangKy = null;
            for (Diem d : listDangKy) {
                if (d.getMaHP().equals(maHP)) {
                    maDangKy = d.getMaDangKy();
                    break;
                }
            }

            if (maDangKy == null) continue;

            Float tb = (qt != null && thi != null)
                    ? Math.round((qt * 0.4f + thi * 0.6f) * 10) / 10f
                    : null;

            modelDiem.setValueAt(tb, i, 4);

            Diem d = new Diem(maDangKy, maHP, tenHP, qt, thi, tb);
            if (!diemService.capNhatDiem(d)) allOk = false;
        }

        JOptionPane.showMessageDialog(this,
                allOk ? "Cập nhật điểm thành công!" : "Cập nhật điểm thất bại!");
    }

    private Float chuyenSangSoThuc(Object o) {
        if (o == null || o.toString().isEmpty()) return null;
        try {
            return Float.parseFloat(o.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private void locDanhSachSinhVien() {
        String tuKhoa = txtTimSV.getText().trim().toLowerCase();

        int selectedRow = tblSinhVien.getSelectedRow();
        String maSVHienTai = (selectedRow >= 0) ? modelSV.getValueAt(selectedRow, 0).toString() : null;

        modelSV.setRowCount(0);
        int rowToSelect = -1;
        int index = 0;

        for (sinhvien sv : allStudents) {
            if (sv.getMaSV().toLowerCase().contains(tuKhoa)
                    || sv.getHoTen().toLowerCase().contains(tuKhoa)
                    || sv.getLop().toLowerCase().contains(tuKhoa)
                    || sv.getNganh().toLowerCase().contains(tuKhoa)
                    || sv.getKhoaHoc().toLowerCase().contains(tuKhoa)
                    || sv.getEmail().toLowerCase().contains(tuKhoa)) {

                modelSV.addRow(new Object[]{
                        sv.getMaSV(), sv.getHoTen(), sv.getLop(),
                        sv.getNganh(), sv.getKhoaHoc(), sv.getEmail()
                });

                if (maSVHienTai != null && sv.getMaSV().equals(maSVHienTai)) {
                    rowToSelect = index;
                }
                index++;
            }
        }

        if (rowToSelect >= 0) {
            tblSinhVien.setRowSelectionInterval(rowToSelect, rowToSelect);
        } else {
            modelDiem.setRowCount(0);
        }
    }
}
