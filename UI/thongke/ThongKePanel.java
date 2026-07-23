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

public class ThongKePanel extends JPanel {

    private final login account;
    private final ThongKeDAO dao = new ThongKeDAO();
    private final JTabbedPane tabbedPane = new JTabbedPane();

    public ThongKePanel(login account) {
        this.account = account;
        setLayout(new BorderLayout(10, 10));

        JPanel tongQuanPanel = new JPanel(new BorderLayout());
        tongQuanPanel.add(new JLabel("Đang tải dữ liệu...", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.add("Tổng quan", tongQuanPanel);

        String[] tables = {"SinhVien", "GiangVien", "HocPhan", "DangKyHoc"};
        for (String table : tables) {
            tabbedPane.add(tableNameToLabel(table), new JScrollPane(new JTable())); // bảng rỗng
        }
        if ("admin".equalsIgnoreCase(account.getRole())) {
            tabbedPane.add("Users", new JScrollPane(new JTable()));
        }

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            String title = tabbedPane.getTitleAt(index);
            Component comp = tabbedPane.getComponentAt(index);
            if (comp instanceof JScrollPane scroll) {
                JTable table = (JTable) scroll.getViewport().getView();
                if (table.getModel().getRowCount() == 0 && !"Tổng quan".equals(title)) {
                    SwingWorker<DefaultTableModel, Void> worker = new SwingWorker<>() {
                        @Override
                        protected DefaultTableModel doInBackground() throws Exception {
                            return new DefaultTableModel(
                                    dao.layDuLieuBang(labelToTableName(title)),
                                    dao.layTenCotBang(labelToTableName(title))
                            ) {
                                @Override
                                public boolean isCellEditable(int row, int column) { return false; }
                            };
                        }

                        @Override
                        protected void done() {
                            try {
                                table.setModel(get());
                                table.setAutoCreateRowSorter(true);
                                table.setFillsViewportHeight(true);
                                table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                                table.setRowHeight(24);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                    worker.execute();
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
        loadTongQuanAsync(tongQuanPanel);
    }

    private void loadTongQuanAsync(JPanel tongQuanPanel) {
        SwingWorker<tongquan, Void> worker = new SwingWorker<>() {
            @Override
            protected tongquan doInBackground() throws Exception {
                return dao.layThongKe();
            }

            @Override
            protected void done() {
                try {
                    tongquan tq = get();
                    tongQuanPanel.removeAll();
                    tongQuanPanel.add(taoPanelTongQuan(tq), BorderLayout.CENTER);
                    tongQuanPanel.revalidate();
                    tongQuanPanel.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    tongQuanPanel.removeAll();
                    tongQuanPanel.add(new JLabel("Lỗi khi tải dữ liệu", SwingConstants.CENTER), BorderLayout.CENTER);
                    tongQuanPanel.revalidate();
                    tongQuanPanel.repaint();
                }
            }
        };
        worker.execute();
    }

    private JPanel taoPanelTongQuan(tongquan tq) {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JPanel cards = new JPanel(new GridLayout(1, 5, 15, 0));
        cards.setOpaque(false);
        cards.add(new TheTongQuan("Sinh viên", String.valueOf(tq.getSoSinhVien())));
        cards.add(new TheTongQuan("Giảng viên", String.valueOf(tq.getSoGiangVien())));
        cards.add(new TheTongQuan("Học phần", String.valueOf(tq.getSoHocPhan())));
        cards.add(new TheTongQuan("Đăng ký", String.valueOf(tq.getSoDangKy())));
        if ("admin".equalsIgnoreCase(account.getRole())) {
            cards.add(new TheTongQuan("Tài khoản", String.valueOf(tq.getSoTaiKhoan())));
        }
        panel.add(cards, BorderLayout.NORTH);

        JPanel chartsPanel = new JPanel(new GridBagLayout());
        chartsPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);

        BieuDo chartTron = new BieuDo();
        chartTron.setDataTron(
                new String[]{"Sinh viên", "Giảng viên", "Học phần"},
                new double[]{tq.getSoSinhVien(), tq.getSoGiangVien(), tq.getSoHocPhan()}
        );
        chartTron.setBorder(BorderFactory.createTitledBorder("Tỷ lệ tổng thể"));

        c.gridx = 0; c.gridy = 0; c.weightx = 0.3; c.weighty = 1.0; c.gridheight = 2;
        chartsPanel.add(chartTron, c);

        JPanel rightPanel = new JPanel(new GridLayout(2,1,10,10));
        rightPanel.setOpaque(false);

        Map<String, Integer> svTheoLop = tq.getSinhVienTheoLop();
        if (svTheoLop != null && !svTheoLop.isEmpty()) {
            BieuDo chartCot1 = new BieuDo();
            chartCot1.setDataCot(
                    svTheoLop.keySet().toArray(new String[0]),
                    svTheoLop.values().stream().mapToInt(Integer::intValue).toArray()
            );
            chartCot1.setBorder(BorderFactory.createTitledBorder("Số lượng sinh viên theo lớp"));
            rightPanel.add(chartCot1);
        }

        Map<String, Float> diemTBTheoHP = tq.getDiemTBTheoHP();
        if (diemTBTheoHP != null && !diemTBTheoHP.isEmpty()) {
            BieuDo chartCot2 = new BieuDo();
            chartCot2.setDataCot(
                    diemTBTheoHP.keySet().toArray(new String[0]),
                    diemTBTheoHP.values().stream().mapToInt(f -> Math.round(f)).toArray()
            );
            chartCot2.setBorder(BorderFactory.createTitledBorder("Điểm trung bình theo học phần"));
            rightPanel.add(chartCot2);
        }

        c.gridx = 1; c.gridy = 0; c.weightx = 0.7; c.weighty = 1.0; c.gridheight = 2;
        chartsPanel.add(rightPanel, c);

        panel.add(chartsPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2,1,10,5));
        infoPanel.setOpaque(false);

        JLabel diemTBLabel = new JLabel(String.format("Điểm trung bình toàn hệ thống: %.2f", tq.getDiemTBHeThong()));
        diemTBLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        diemTBLabel.setForeground(new Color(33,150,243));
        infoPanel.add(diemTBLabel);

        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        progressPanel.setOpaque(false);
        progressPanel.add(new JLabel("Tỷ lệ lấp đầy lớp học: "));
        JProgressBar progress = new JProgressBar(0,100);
        progress.setValue((int)(tq.getTyLeLapDay()*100));
        progress.setStringPainted(true);
        progress.setPreferredSize(new Dimension(200,20));
        progressPanel.add(progress);
        infoPanel.add(progressPanel);

        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String labelToTableName(String label) {
        return switch (label) {
            case "Sinh viên" -> "SinhVien";
            case "Giảng viên" -> "GiangVien";
            case "Học phần" -> "HocPhan";
            case "Đăng ký" -> "DangKyHoc";
            case "Người dùng" -> "Users";
            default -> label;
        };
    }

    private String tableNameToLabel(String tableName) {
        return switch (tableName) {
            case "SinhVien" -> "Sinh viên";
            case "GiangVien" -> "Giảng viên";
            case "HocPhan" -> "Học phần";
            case "DangKyHoc" -> "Đăng ký";
            case "Users" -> "Người dùng";
            default -> tableName;
        };
    }
}
