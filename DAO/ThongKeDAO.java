package DAO;

import connection.connectBD;
import model.tongquan;

import java.sql.*;
import java.util.*;

public class ThongKeDAO {

    public tongquan layThongKe() {
        return layThongKeTheoSinhVien(null);
    }

    public tongquan layThongKeTheoSinhVien(String maSV) {
        tongquan tq = new tongquan(0, 0, 0, 0, 0);

        try (Connection con = connectBD.getConnection()) {

            int sv, gv, hp, dk, tk;

            if (maSV == null) {
                sv = demBanGhi(con, "SinhVien");
                gv = demBanGhi(con, "GiangVien");
                hp = demBanGhi(con, "HocPhan");
                dk = demBanGhi(con, "DangKyHoc");
                tk = demBanGhi(con, "Users");
            } else {
                sv = 1;
                gv = demBanGhi(con, "GiangVien");
                hp = demBanGhi(con, "HocPhan");
                dk = demTheoMaSinhVien(con, "DangKyHoc", maSV);
                tk = 0;
            }

            tq = new tongquan(sv, gv, hp, dk, tk);

            tq.setSinhVienTheoLop(thongKeSinhVienTheoLop(con, maSV));
            tq.setDiemTBTheoHP(thongKeDiemTrungBinhTheoHocPhan(con, maSV));
            tq.setDiemTBHeThong(layDiemTrungBinhHeThong(con, maSV));
            tq.setTyLeLapDay(layTyLeLapDayLopHoc(con, maSV));

            if (maSV != null) {
                tq.setSoTinChiDaHoc(laySoTinChiDaHoc(con, maSV));
                tq.setDiemTBTichLuy(layDiemTrungBinhTichLuy(con, maSV));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tq;
    }


    private int demBanGhi(Connection con, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int demTheoMaSinhVien(Connection con, String tableName, String maSV) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE maSV = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public Object[][] layDuLieuBang(String tableName) {
        List<Object[]> dataList = new ArrayList<>();
        try (Connection con = connectBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + tableName)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++)
                    row[i] = rs.getObject(i + 1);
                dataList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][]);
    }

    public Object[][] layDuLieuBangTheoSinhVien(String tableName, String username) {
        List<Object[]> dataList = new ArrayList<>();
        try (Connection con = connectBD.getConnection()) {

            String maSV = null;
            String sqlUser = "SELECT MaSV FROM Users WHERE username = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlUser)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) maSV = rs.getString("MaSV");
            }
            if (maSV == null) return new Object[0][0];

            String sql;
            PreparedStatement ps;

            switch (tableName) {
                case "DangKyHoc":
                    sql = "SELECT maHP, maLHP FROM DangKyHoc WHERE maSV = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, maSV);
                    break;
                case "Diem":
                    sql = "SELECT d.maHP, d.diemQT, d.diemThi, d.diemTB " +
                            "FROM Diem d JOIN DangKyHoc dk ON d.maDangKy = dk.maDangKy " +
                            "WHERE dk.maSV = ?";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, maSV);
                    break;
                default:
                    sql = "SELECT * FROM " + tableName;
                    ps = con.prepareStatement(sql);
                    break;
            }

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++)
                    row[i] = rs.getObject(i + 1);
                dataList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList.toArray(new Object[0][]);
    }

    public String[] layTenCotBang(String tableName) {
        try (Connection con = connectBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " WHERE 1=0")) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 0; i < columnCount; i++)
                columns[i] = meta.getColumnName(i + 1);
            return columns;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    private Map<String, Integer> thongKeSinhVienTheoLop(Connection con, String maSV) throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql;
        if (maSV == null) {
            sql = "SELECT lop, COUNT(*) AS SoSV FROM SinhVien GROUP BY lop";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) map.put(rs.getString("lop"), rs.getInt("SoSV"));
            }
        } else {
            sql = "SELECT lop, COUNT(*) AS SoSV FROM SinhVien WHERE maSV = ? GROUP BY lop";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maSV);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) map.put(rs.getString("lop"), rs.getInt("SoSV"));
            }
        }
        return map;
    }

    private Map<String, Float> thongKeDiemTrungBinhTheoHocPhan(Connection con, String maSV) throws SQLException {
        Map<String, Float> map = new LinkedHashMap<>();
        if (maSV == null) {
            String sql = "SELECT maHP, AVG(diemTB) AS DiemTB FROM Diem GROUP BY maHP";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) map.put(rs.getString("maHP"), rs.getFloat("DiemTB"));
            }
        } else {
            String sql = "SELECT d.maHP, d.diemTB FROM Diem d " +
                    "JOIN DangKyHoc dk ON d.maDangKy = dk.maDangKy WHERE dk.maSV = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maSV);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) map.put(rs.getString("maHP"), rs.getFloat("diemTB"));
            }
        }
        return map;
    }

    private float layDiemTrungBinhHeThong(Connection con, String maSV) throws SQLException {
        if (maSV == null) {
            String sql = "SELECT AVG(diemTB) FROM Diem";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) return rs.getFloat(1);
            }
        } else {
            String sql = "SELECT AVG(diemTB) FROM Diem d " +
                    "JOIN DangKyHoc dk ON d.maDangKy = dk.maDangKy WHERE dk.maSV = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maSV);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) return rs.getFloat(1);
            }
        }
        return 0f;
    }

    private float layTyLeLapDayLopHoc(Connection con, String maSV) throws SQLException {
        if (maSV != null) return 0f; // sinh viên không quan tâm

        String sql = """
            SELECT SUM(CAST(dk.CountSV AS FLOAT)) / SUM(CAST(lh.siSoToiDa AS FLOAT)) AS TyLe
            FROM LopHocPhan lh
            LEFT JOIN (
                SELECT maLHP, COUNT(*) AS CountSV
                FROM DangKyHoc
                GROUP BY maLHP
            ) dk ON lh.maLHP = dk.maLHP
        """;

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getFloat("TyLe");
        }
        return 0f;
    }

    private int laySoTinChiDaHoc(Connection con, String maSV) throws SQLException {
        String sql = "SELECT SUM(hp.soTinChi) FROM DangKyHoc dk JOIN HocPhan hp ON dk.maHP = hp.maHP WHERE dk.maSV = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private float layDiemTrungBinhTichLuy(Connection con, String maSV) throws SQLException {
        String sql = "SELECT AVG(diemTB) FROM Diem d JOIN DangKyHoc dk ON d.maDangKy = dk.maDangKy WHERE dk.maSV = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getFloat(1);
        }
        return 0f;
    }

    public tongquan layThongKeTheoTenDangNhap(String username) {
        String maSV = null;
        String sql = "SELECT MaSV FROM Users WHERE username = ?";
        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) maSV = rs.getString("MaSV");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return layThongKeTheoSinhVien(maSV);
    }
}
