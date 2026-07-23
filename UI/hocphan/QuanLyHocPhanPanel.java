package UI.hocphan;

import model.HocPhan;
import service.HocPhanService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuanLyHocPhanPanel extends JPanel {

    private final HocPhanService service = new HocPhanService();

    private JTable table;
    private DefaultTableModel model;
    private List<HocPhan> allHP;

    private JTextField txtMaHP, txtTenHP, txtSoTinChi, txtMaGV;
    private JTextField txtTimHP;

    public QuanLyHocPhanPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("QUẢN LÝ HỌC PHẦN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Tìm học phần:"));

        txtTimHP = new JTextField(20);
        searchPanel.add(txtTimHP);

        header.add(title, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Mã HP", "Tên học phần", "Số tín chỉ", "Mã GV"}, 0
        );
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionBackground(new Color(220, 235, 250));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách học phần"));
        add(scroll, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin học phần"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaHP = createField();
        txtTenHP = createField();
        txtSoTinChi = createField();
        txtMaGV = createField();

        String[] labels = {"Mã học phần", "Tên học phần", "Số tín chỉ", "Mã GV"};
        JTextField[] fields = {txtMaHP, txtTenHP, txtSoTinChi, txtMaGV};

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

        btnAdd.addActionListener(e -> themHocPhan());
        btnEdit.addActionListener(e -> capNhatHocPhan());
        btnDelete.addActionListener(e -> xoaHocPhan());
        btnReset.addActionListener(e -> {
            clearForm();
            loadData();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillForm();
        });

        txtTimHP.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterHocPhan(); }
            public void removeUpdate(DocumentEvent e) { filterHocPhan(); }
            public void changedUpdate(DocumentEvent e) { filterHocPhan(); }
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

    private void loadData() {
        model.setRowCount(0);
        allHP = service.layTatCaHocPhan();

        for (HocPhan hp : allHP) {
            model.addRow(new Object[]{
                    hp.getMaHP(),
                    hp.getTenHP(),
                    hp.getSoTinChi(),
                    hp.getMaGV()
            });
        }
        txtMaHP.setEditable(true);
    }

    private void filterHocPhan() {
        String key = txtTimHP.getText().toLowerCase().trim();
        model.setRowCount(0);

        for (HocPhan hp : allHP) {
            if (hp.getMaHP().toLowerCase().contains(key)
                    || hp.getTenHP().toLowerCase().contains(key)
                    || String.valueOf(hp.getSoTinChi()).contains(key)
                    || hp.getMaGV().toLowerCase().contains(key)) {

                model.addRow(new Object[]{
                        hp.getMaHP(),
                        hp.getTenHP(),
                        hp.getSoTinChi(),
                        hp.getMaGV()
                });
            }
        }
    }

    private void themHocPhan() {
        try {
            HocPhan hp = new HocPhan(
                    txtMaHP.getText().trim(),
                    txtTenHP.getText().trim(),
                    Integer.parseInt(txtSoTinChi.getText().trim()),
                    txtMaGV.getText().trim()
            );

            if (service.themHocPhan(hp)) {
                JOptionPane.showMessageDialog(this, "Thêm học phần thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Mã học phần đã tồn tại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void capNhatHocPhan() {
        HocPhan hp = service.timTheoMaHocPhan(txtMaHP.getText());
        if (hp == null) return;

        try {
            hp.setTenHP(txtTenHP.getText());
            hp.setSoTinChi(Integer.parseInt(txtSoTinChi.getText()));
            hp.setMaGV(txtMaGV.getText());

            service.capNhatHocPhan(hp);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật!");
        }
    }

    private void xoaHocPhan() {
        if (JOptionPane.showConfirmDialog(
                this,
                "Xóa học phần này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            service.xoaHocPhan(txtMaHP.getText());
            loadData();
            clearForm();
        }
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtMaHP.setText(model.getValueAt(row, 0).toString());
        txtTenHP.setText(model.getValueAt(row, 1).toString());
        txtSoTinChi.setText(model.getValueAt(row, 2).toString());
        txtMaGV.setText(model.getValueAt(row, 3).toString());
        txtMaHP.setEditable(false);
    }

    private void clearForm() {
        txtMaHP.setText("");
        txtTenHP.setText("");
        txtSoTinChi.setText("");
        txtMaGV.setText("");
        txtMaHP.setEditable(true);
    }
}
