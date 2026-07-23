package UI.hocphan;

import model.LopHocPhan;
import service.DangKyHocPhanService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DangKyHocPhanPanel extends JPanel {

    private static final int MAX_TC = 25;

    private final DangKyHocPhanService service = new DangKyHocPhanService();
    private final String maSV;

    private JTable tblChuaDK, tblDaDK;
    private DefaultTableModel modelChuaDK, modelDaDK;

    private JLabel lblTongTC;

    private List<LopHocPhan> listChuaDK, listDaDK;

    public DangKyHocPhanPanel(String maSV) {
        this.maSV = maSV;

        setLayout(new BorderLayout(10,10));
        setBackground(Color.WHITE);

        initTop();
        initTables();
        initButtons();
        taiDuLieu();
    }

    private void initTop() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        JLabel title = new JLabel("ĐĂNG KÝ HỌC PHẦN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        lblTongTC = new JLabel("Tổng tín chỉ: 0 / " + MAX_TC);
        lblTongTC.setFont(new Font("Segoe UI", Font.BOLD, 14));

        p.add(title, BorderLayout.CENTER);
        p.add(lblTongTC, BorderLayout.EAST);

        add(p, BorderLayout.NORTH);
    }

    private void initTables() {
        modelChuaDK = new DefaultTableModel(
                new String[]{"Mã LHP","HP","GV","Thứ","Tiết","Phòng","Sĩ số"},0);
        tblChuaDK = new JTable(modelChuaDK);

        modelDaDK = new DefaultTableModel(
                new String[]{"Mã LHP","HP","GV","Thứ","Tiết","Phòng","Sĩ số"},0);
        tblDaDK = new JTable(modelDaDK);

        tblChuaDK.setRowHeight(28);
        tblDaDK.setRowHeight(28);

        tblChuaDK.setDefaultRenderer(Object.class, new ColorRenderer());

        JPanel center = new JPanel(new GridLayout(1,2,10,10));
        center.add(wrapTable("CHƯA ĐĂNG KÝ", tblChuaDK));
        center.add(wrapTable("ĐÃ ĐĂNG KÝ", tblDaDK));

        add(center, BorderLayout.CENTER);
    }

    private JPanel wrapTable(String title, JTable table) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void initButtons() {
        JButton btnDK = createButton("Đăng ký →");
        JButton btnHuy = createButton("← Hủy đăng ký");

        btnDK.addActionListener(e -> dangKy());
        btnHuy.addActionListener(e -> huyDangKy());

        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.add(btnDK);
        p.add(btnHuy);

        add(p, BorderLayout.SOUTH);
    }

    private JButton createButton(String t){
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setBackground(new Color(33,150,243));
        b.setForeground(Color.WHITE);
        return b;
    }

    private void taiDuLieu() {
        listChuaDK = service.layLopHocPhanChuaDangKy(maSV);
        listDaDK   = service.layLopHocPhanDaDangKy(maSV);

        loadTableBasic(modelChuaDK, listChuaDK);
        loadTableBasic(modelDaDK, listDaDK);
        capNhatTongTinChi();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                service.capNhatSiSoVaTrungLich(maSV, listChuaDK);
                service.capNhatSiSo(listDaDK);
                return null;
            }

            @Override
            protected void done() {
                loadTable(modelChuaDK, listChuaDK);
                loadTable(modelDaDK, listDaDK);
                capNhatTongTinChi();
            }
        }.execute();
    }

    private void loadTableBasic(DefaultTableModel model, List<LopHocPhan> list) {
        model.setRowCount(0);
        for (LopHocPhan l : list) {
            model.addRow(new Object[]{
                    l.getMaLHP(),
                    l.getMaHP(),
                    l.getMaGV(),
                    l.getThu(),
                    l.getTietBatDau()+" → "+(l.getTietBatDau()+l.getSoTiet()-1),
                    l.getPhong(),
                    "..."
            });
        }
    }

    private void loadTable(DefaultTableModel model, List<LopHocPhan> list) {
        model.setRowCount(0);
        for (LopHocPhan l : list) {
            model.addRow(taoDongDuLieu(l));
        }
    }

    private Object[] taoDongDuLieu(LopHocPhan l){
        return new Object[]{
                l.getMaLHP(),
                l.getMaHP(),
                l.getMaGV(),
                l.getThu(),
                l.getTietBatDau()+" → "+(l.getTietBatDau()+l.getSoTiet()-1),
                l.getPhong(),
                l.getSiSoHienTai() + "/" + l.getSiSoToiDa()
        };
    }

    private void dangKy() {
        int r = tblChuaDK.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this,"Vui lòng chọn lớp để đăng ký!");
            return;
        }

        LopHocPhan l = listChuaDK.get(r);
        int tongTC = listDaDK.stream().mapToInt(LopHocPhan::getSoTinChi).sum();
        if(tongTC + l.getSoTinChi() > MAX_TC){
            JOptionPane.showMessageDialog(this,"Vượt quá số tín chỉ tối đa!");
            return;
        }

        if (l.isBiTrungLich()) {
            JOptionPane.showMessageDialog(this,"Trùng lịch học!");
            return;
        }

        if (l.getSiSoHienTai() >= l.getSiSoToiDa()) {
            JOptionPane.showMessageDialog(this,"Lớp đã đầy!");
            return;
        }

        if (!service.dangKyHocPhan(maSV, l)) {
            JOptionPane.showMessageDialog(this,"Không thể đăng ký!");
            return;
        }
        modelDaDK.addRow(taoDongDuLieu(l));
        modelChuaDK.removeRow(r);
        listDaDK.add(l);
        listChuaDK.remove(r);

        capNhatTongTinChi();
        JOptionPane.showMessageDialog(this,"Đăng ký thành công!");
    }

    private void huyDangKy() {
        int r = tblDaDK.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this,"Vui lòng chọn lớp để hủy đăng ký!");
            return;
        }

        LopHocPhan l = listDaDK.get(r);
        service.huyDangKyHocPhan(maSV, l.getMaLHP());

        modelChuaDK.addRow(taoDongDuLieu(l));
        modelDaDK.removeRow(r);
        listChuaDK.add(l);
        listDaDK.remove(r);

        capNhatTongTinChi();
        JOptionPane.showMessageDialog(this,"Hủy đăng ký thành công!");
    }

    private void capNhatTongTinChi() {
        int tc = listDaDK.stream().mapToInt(LopHocPhan::getSoTinChi).sum();
        lblTongTC.setText("Tổng tín chỉ: " + tc + " / " + MAX_TC);
        lblTongTC.setForeground(tc > MAX_TC ? Color.RED : Color.BLACK);
    }

    class ColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col) {

            Component c = super.getTableCellRendererComponent(
                    table,value,isSelected,hasFocus,row,col);

            LopHocPhan l;
            if(table == tblChuaDK){
                l = listChuaDK.get(row);
                if (l.isBiTrungLich())
                    c.setBackground(new Color(255,200,200));
                else if (l.getSiSoHienTai() >= l.getSiSoToiDa())
                    c.setBackground(new Color(255,235,150));
                else
                    c.setBackground(Color.WHITE);
            } else {
                c.setBackground(Color.WHITE);
            }

            return c;
        }
    }
}
