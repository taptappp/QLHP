package UI.hocphan;

import model.HocPhan;
import model.LopHocPhan;
import service.HocPhanService;
import service.LopHocPhanService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LopHocPhanPanel extends JPanel {

    private final HocPhanService hpService = new HocPhanService();
    private final LopHocPhanService lhpService = new LopHocPhanService();

    private JTable tblHP, tblLHP;
    private DefaultTableModel modelHP, modelLHP;

    private JTextField txtMaLHP, txtTenHP, txtThu,
            txtTietBD, txtSoTiet, txtPhong, txtSiSo, txtTim;
    private JLabel lblGV;

    private List<HocPhan> allHP;
    private List<LopHocPhan> allLHP;

    public LopHocPhanPanel() {
        setLayout(new BorderLayout(8,8));
        setBackground(Color.WHITE);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(Color.WHITE);

        JLabel title = new JLabel("QUẢN LÝ LỚP HỌC PHẦN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));
        top.add(title);

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.setBackground(Color.WHITE);
        search.add(new JLabel("🔍 Tìm LHP"));
        txtTim = new JTextField(25);
        search.add(txtTim);
        top.add(search);

        add(top, BorderLayout.NORTH);

        modelHP = new DefaultTableModel(
                new String[]{"Mã HP","Tên HP","Mã GV","Tên GV"},0
        );
        tblHP = new JTable(modelHP);

        modelLHP = new DefaultTableModel(
                new String[]{"Mã LHP","Mã HP","Mã GV","Tên GV","Thứ","Tiết","Phòng","Sĩ số"},0
        );
        tblLHP = new JTable(modelLHP);

        JPanel tablePanel = new JPanel(new GridLayout(1,2,10,10));
        tablePanel.add(new JScrollPane(tblHP));
        tablePanel.add(new JScrollPane(tblLHP));
        add(tablePanel, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        form.setBackground(Color.WHITE);

        txtMaLHP = new JTextField();
        txtTenHP = new JTextField(); txtTenHP.setEditable(false);
        lblGV = new JLabel("—");
        txtThu = new JTextField();
        txtTietBD = new JTextField();
        txtSoTiet = new JTextField();
        txtPhong = new JTextField();
        txtSiSo = new JTextField();

        form.add(new JLabel("Mã LHP")); form.add(txtMaLHP);
        form.add(new JLabel("Tên HP")); form.add(txtTenHP);
        form.add(new JLabel("Giảng viên")); form.add(lblGV);
        form.add(new JLabel("Thứ")); form.add(txtThu);
        form.add(new JLabel("Tiết bắt đầu")); form.add(txtTietBD);
        form.add(new JLabel("Số tiết")); form.add(txtSoTiet);
        form.add(new JLabel("Phòng")); form.add(txtPhong);
        form.add(new JLabel("Sĩ số")); form.add(txtSiSo);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBorder(BorderFactory.createTitledBorder("Thông tin lớp học phần"));
        wrap.add(form);

        JPanel btnPanel = new JPanel(new GridLayout(4,1,0,10));
        btnPanel.setBackground(Color.WHITE);

        JButton btnAdd = createButton("Mở lớp");
        JButton btnEdit = createButton("Sửa");
        JButton btnDelete = createButton("Hủy lớp");
        JButton btnReset = createButton("Hiển thị DS");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReset);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(wrap, BorderLayout.CENTER);
        bottom.add(btnPanel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        loadHocPhan();
        loadLHP();

        tblHP.getSelectionModel().addListSelectionListener(e -> fillHP());
        tblLHP.getSelectionModel().addListSelectionListener(e -> fillLHP());

        btnAdd.addActionListener(e -> themLHP());
        btnEdit.addActionListener(e -> capnhatLHP());
        btnDelete.addActionListener(e -> xoaLHP());
        btnReset.addActionListener(e -> {
            clearForm();
            refreshTableLHP(allLHP);
        });

        txtTim.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e){ filter(); }
            public void removeUpdate(DocumentEvent e){ filter(); }
            public void changedUpdate(DocumentEvent e){ filter(); }
        });
    }

    private void loadHocPhan(){
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                allHP = hpService.layTatCaHocPhanKemGiangVien();
                return null;
            }
            protected void done() {
                modelHP.setRowCount(0);
                if(allHP != null) {
                    for(HocPhan hp: allHP){
                        modelHP.addRow(new Object[]{
                                hp.getMaHP(),
                                hp.getTenHP(),
                                hp.getMaGV() != null ? hp.getMaGV() : "—",
                                hp.getTenGV() != null ? hp.getTenGV() : "—"
                        });
                    }
                }
            }
        }.execute();
    }

    private void loadLHP(){
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                allLHP = lhpService.layTatCaLopHocPhan();
                return null;
            }
            protected void done() {
                refreshTableLHP(allLHP);
            }
        }.execute();
    }

    private void refreshTableLHP(List<LopHocPhan> list){
        modelLHP.setRowCount(0);
        if(list != null){
            for(LopHocPhan l: list){
                modelLHP.addRow(new Object[]{
                        l.getMaLHP(),
                        l.getMaHP(),
                        l.getMaGV(),
                        l.getTenGV() != null ? l.getTenGV() : "—",
                        l.getThu(),
                        l.getTietBatDau() + " → " + (l.getTietBatDau()+l.getSoTiet()-1),
                        l.getPhong(),
                        l.getSiSoToiDa()
                });
            }
        }
    }

    private void themLHP() {
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                try {
                    int r = tblHP.getSelectedRow();
                    if (r < 0) return null;

                    String maHP = modelHP.getValueAt(r,0).toString();

                    HocPhan hp = allHP.stream()
                            .filter(h -> h.getMaHP().equals(maHP))
                            .findFirst().orElse(null);

                    String maGV = hp != null ? hp.getMaGV() : "";
                    String tenGV = hp != null ? hp.getTenGV() : "—";

                    LopHocPhan l = new LopHocPhan(
                            txtMaLHP.getText(),
                            maHP,
                            maGV,
                            Integer.parseInt(txtThu.getText()),
                            Integer.parseInt(txtTietBD.getText()),
                            Integer.parseInt(txtSoTiet.getText()),
                            txtPhong.getText(),
                            Integer.parseInt(txtSiSo.getText())
                    );
                    l.setTenGV(tenGV);

                    lhpService.themLopHocPhan(l);

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(LopHocPhanPanel.this,
                                    "Lỗi khi thêm lớp: " + ex.getMessage())
                    );
                }
                return null;
            }
            protected void done() {
                loadLHP();
                clearForm();
            }
        }.execute();
    }

    private void capnhatLHP() {
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                try {
                    LopHocPhan l = lhpService.timTheoMaLopHocPhan(txtMaLHP.getText());
                    if (l == null) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(LopHocPhanPanel.this,
                                        "Không tìm thấy lớp học phần để cập nhật"));
                        return null;
                    }

                    l.setThu(Integer.parseInt(txtThu.getText()));
                    l.setTietBatDau(Integer.parseInt(txtTietBD.getText()));
                    l.setSoTiet(Integer.parseInt(txtSoTiet.getText()));
                    l.setPhong(txtPhong.getText());
                    l.setSiSoToiDa(Integer.parseInt(txtSiSo.getText()));

                    lhpService.capNhatLopHocPhan(l);

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(LopHocPhanPanel.this,
                                    "Lỗi khi cập nhật lớp: " + ex.getMessage())
                    );
                }
                return null;
            }
            protected void done() {
                loadLHP();
            }
        }.execute();
    }

    private void xoaLHP() {
        if (JOptionPane.showConfirmDialog(this,"Hủy lớp này?","Xác nhận",
                JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;

        new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                try {
                    lhpService.xoaLopHocPhan(txtMaLHP.getText());
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(LopHocPhanPanel.this,
                                    "Lỗi khi hủy lớp: " + ex.getMessage())
                    );
                }
                return null;
            }
            protected void done() {
                loadLHP();
                clearForm();
            }
        }.execute();
    }

    private void fillHP(){
        int r = tblHP.getSelectedRow();
        if(r < 0) return;

        txtTenHP.setText(modelHP.getValueAt(r,1).toString());
        lblGV.setText(modelHP.getValueAt(r,3).toString());
    }

    private void fillLHP(){
        int r = tblLHP.getSelectedRow();
        if(r < 0) return;

        txtMaLHP.setText(tblLHP.getValueAt(r,0).toString());
        txtThu.setText(tblLHP.getValueAt(r,4).toString().split("→")[0].trim());
        txtPhong.setText(tblLHP.getValueAt(r,6).toString());
        txtSiSo.setText(tblLHP.getValueAt(r,7).toString());

        txtMaLHP.setEditable(false);
    }

    private void filter(){
        String k = txtTim.getText().toLowerCase();
        modelLHP.setRowCount(0);
        if(allLHP != null){
            for(LopHocPhan l: allLHP){
                if((l.getMaLHP() != null && l.getMaLHP().toLowerCase().contains(k))
                        || (l.getMaHP() != null && l.getMaHP().toLowerCase().contains(k))
                        || (l.getPhong() != null && l.getPhong().toLowerCase().contains(k))){
                    modelLHP.addRow(new Object[]{
                            l.getMaLHP(),
                            l.getMaHP(),
                            l.getMaGV(),
                            l.getTenGV() != null ? l.getTenGV() : "—",
                            l.getThu(),
                            l.getTietBatDau()+" → "+(l.getTietBatDau()+l.getSoTiet()-1),
                            l.getPhong(),
                            l.getSiSoToiDa()
                    });
                }
            }
        }
    }

    private void clearForm(){
        txtMaLHP.setText("");
        txtTenHP.setText("");
        lblGV.setText("—");
        txtThu.setText("");
        txtTietBD.setText("");
        txtSoTiet.setText("");
        txtPhong.setText("");
        txtSiSo.setText("");
        txtMaLHP.setEditable(true);
    }

    private JButton createButton(String t){
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setBackground(new Color(33,150,243));
        b.setForeground(Color.WHITE);
        return b;
    }
}
