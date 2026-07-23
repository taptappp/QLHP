package UI.diem;

import model.Diem;
import model.sinhvien;
import service.DiemService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class XemDiemPanel extends JPanel {

    private final DiemService service = new DiemService();
    private final sinhvien currentSV;

    private JTable table;
    private DefaultTableModel model;

    public XemDiemPanel(sinhvien sv) {
        this.currentSV = sv;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("BẢNG ĐIỂM SINH VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel lblSub = new JLabel(
                "Sinh viên: " + sv.getHoTen() + "  |  Mã SV: " + sv.getMaSV(),
                SwingConstants.CENTER
        );
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        add(titlePanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Mã HP", "Tên học phần", "Điểm QT", "Điểm Thi", "Điểm TB"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220, 235, 250));
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(240, 240, 240));

        add(new JScrollPane(table), BorderLayout.CENTER);

        taiDiem();
    }

    private void taiDiem() {
        model.setRowCount(0);

        List<Diem> list = service.layDiemTheoSinhVien(currentSV.getMaSV());

        if (list.isEmpty()) {
            model.addRow(new Object[]{"", "Chưa có dữ liệu điểm", "", "", ""});
            return;
        }

        for (Diem d : list) {
            Float diemTB = d.getDiemTB();
            if (diemTB == null && (d.getDiemQT() != null || d.getDiemThi() != null)) {
                float qt = d.getDiemQT() != null ? d.getDiemQT() : 0;
                float thi = d.getDiemThi() != null ? d.getDiemThi() : 0;
                diemTB = (qt + thi) / 2;
            }

            model.addRow(new Object[]{
                    d.getMaHP(),
                    d.getTenHP(),
                    d.getDiemQT() != null ? d.getDiemQT() : "-",
                    d.getDiemThi() != null ? d.getDiemThi() : "-",
                    diemTB != null ? String.format("%.2f", diemTB) : "-"
            });
        }
    }
}
