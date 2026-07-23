package UI.thongke;

import UI.component.BieuDo;
import UI.component.TheTongQuan;
import DAO.ThongKeDAO;
import model.login;
import model.tongquan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ThongKeSinhVienPanel extends JPanel {

    private final login account;
    private final ThongKeDAO dao = new ThongKeDAO();

    public ThongKeSinhVienPanel(login account) {
        this.account = account;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Tổng quan", taoPanelTongQuan());
        tabbedPane.add("Học phần", createTablePanel("DangKyHoc", account.getUsername()));
        tabbedPane.add("Điểm", createTablePanel("Diem", account.getUsername()));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel taoPanelTongQuan() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));
        cards.setOpaque(false);
        panel.add(cards, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());
        chartPanel.setOpaque(false);
        panel.add(chartPanel, BorderLayout.CENTER);

        new SwingWorker<tongquan, Void>() {
            @Override
            protected tongquan doInBackground() {
                return dao.layThongKeTheoTenDangNhap(account.getUsername());
            }

            @Override
            protected void done() {
                try {
                    tongquan tq = get();
                    cards.removeAll();
                    cards.add(new TheTongQuan("Học phần đăng ký", String.valueOf(tq.getSoDangKy())));
                    cards.add(new TheTongQuan("Tín chỉ đã học", String.valueOf(tq.getSoTinChiDaHoc())));
                    cards.add(new TheTongQuan("Điểm TB tích lũy", String.format("%.2f", tq.getDiemTBTichLuy())));
                    cards.revalidate();
                    cards.repaint();

                    Map<String, Float> diemTBTheoHP = tq.getDiemTBTheoHP();
                    if (diemTBTheoHP != null && !diemTBTheoHP.isEmpty()) {
                        String[] labels = diemTBTheoHP.keySet().toArray(new String[0]);
                        int[] values = diemTBTheoHP.values().stream().mapToInt(f -> Math.round(f)).toArray();
                        BieuDo chartCot = new BieuDo();
                        chartCot.setDataCot(labels, values);
                        chartCot.setBorder(BorderFactory.createTitledBorder("Điểm trung bình theo học phần"));
                        chartCot.setPreferredSize(new Dimension(800, 350));
                        chartPanel.add(chartCot, BorderLayout.CENTER);
                    }

                    chartPanel.revalidate();
                    chartPanel.repaint();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();

        return panel;
    }
    private JScrollPane createTablePanel(String tableName, String username) {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);

        new SwingWorker<Object[][], Void>() {
            @Override
            protected Object[][] doInBackground() {
                return dao.layDuLieuBangTheoSinhVien(tableName, username);
            }

            @Override
            protected void done() {
                try {
                    Object[][] data = get();
                    if (data != null && data.length > 0) {
                        String[] showColumns;
                        if ("DangKyHoc".equalsIgnoreCase(tableName)) {
                            showColumns = new String[]{"Mã HP", "Mã LHP"};
                        } else if ("Diem".equalsIgnoreCase(tableName)) {
                            showColumns = new String[]{"Mã HP", "Điểm QT", "Điểm Thi", "Điểm TB"};
                        } else {
                            showColumns = new String[data[0].length];
                            for (int i = 0; i < showColumns.length; i++) showColumns[i] = "Col" + i;
                        }

                        model.setColumnIdentifiers(showColumns);
                        for (Object[] row : data) {
                            Object[] newRow = new Object[showColumns.length];
                            for (int i = 0; i < showColumns.length; i++) {
                                if (i < row.length) {
                                    Object val = row[i];
                                    if (val instanceof Float || val instanceof Double) {
                                        newRow[i] = String.format("%.2f", val);
                                    } else {
                                        newRow[i] = val;
                                    }
                                }
                            }
                            model.addRow(newRow);
                        }
                    }
                    table.revalidate();
                    table.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();

        return scrollPane;
    }
}
