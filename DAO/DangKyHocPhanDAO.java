package DAO;

import connection.connectBD;
import model.LopHocPhan;

import java.sql.*;
import java.util.*;

public class DangKyHocPhanDAO {

    // ===================== Lấy danh sách lớp =====================
    public List<LopHocPhan> layLopHocPhanChuaDangKy(String maSV) {
        List<LopHocPhan> list = new ArrayList<>();
        String sql = """
            SELECT lhp.*, hp.soTinChi, gv.hoTen AS tenGV
            FROM LopHocPhan lhp
            JOIN HocPhan hp ON lhp.maHP = hp.maHP
            LEFT JOIN GiangVien gv ON lhp.maGV = gv.maGV
            WHERE lhp.maLHP NOT IN (
                SELECT maLHP FROM DangKyHoc WHERE maSV = ?
            )
        """;

        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LopHocPhan l = map(rs);
                l.setSoTinChi(rs.getInt("soTinChi"));
                l.setTenGV(rs.getString("tenGV"));
                list.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<LopHocPhan> layLopHocPhanDaDangKy(String maSV) {
        List<LopHocPhan> list = new ArrayList<>();
        String sql = """
            SELECT lhp.*, hp.soTinChi, gv.hoTen AS tenGV
            FROM DangKyHoc dk
            JOIN LopHocPhan lhp ON dk.maLHP = lhp.maLHP
            JOIN HocPhan hp ON lhp.maHP = hp.maHP
            LEFT JOIN GiangVien gv ON lhp.maGV = gv.maGV
            WHERE dk.maSV = ?
        """;

        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LopHocPhan l = map(rs);
                l.setSoTinChi(rs.getInt("soTinChi"));
                l.setTenGV(rs.getString("tenGV"));
                list.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===================== Kiểm tra đăng ký & lớp đầy =====================
    public boolean daDangKy(String maSV, String maLHP) {
        String sql = "SELECT 1 FROM DangKyHoc WHERE maSV=? AND maLHP=?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ps.setString(2, maLHP);
            return ps.executeQuery().next();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean conChoTrongLop(String maLHP) {
        String sql = """
            SELECT lhp.siSoToiDa - COUNT(dk.maSV) AS conCho
            FROM LopHocPhan lhp
            LEFT JOIN DangKyHoc dk ON lhp.maLHP = dk.maLHP
            WHERE lhp.maLHP = ?
            GROUP BY lhp.siSoToiDa
        """;
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt("conCho") > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // ===================== Lấy sĩ số nhanh cho nhiều lớp =====================
    public Map<String, Integer> laySiSoHienTaiNhieuLop(List<LopHocPhan> ds) {
        Map<String, Integer> map = new HashMap<>();
        if (ds == null || ds.isEmpty()) return map;

        // Tạo chuỗi maLHP cho IN clause
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < ds.size(); i++) {
            inClause.append("?");
            if (i < ds.size() - 1) inClause.append(",");
        }

        String sql = "SELECT maLHP, COUNT(maSV) AS soSV FROM DangKyHoc " +
                "WHERE maLHP IN (" + inClause + ") GROUP BY maLHP";

        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // Gán các tham số
            for (int i = 0; i < ds.size(); i++) {
                ps.setString(i + 1, ds.get(i).getMaLHP());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("maLHP"), rs.getInt("soSV"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Nếu lớp không có sinh viên nào đăng ký, map vẫn chưa có → gán 0
        for (LopHocPhan l : ds) {
            map.putIfAbsent(l.getMaLHP(), 0);
        }

        return map;
    }

    // ===================== Đăng ký / Hủy =====================
    public boolean dangKy(String maSV, String maLHP, String maHP) {
        String sql = "INSERT INTO DangKyHoc(maDangKy, maSV, maHP, maLHP) VALUES (?,?,?,?)";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, maSV);
            ps.setString(3, maHP);
            ps.setString(4, maLHP);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean huyDangKy(String maSV, String maLHP) {
        String sql = "DELETE FROM DangKyHoc WHERE maSV=? AND maLHP=?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maSV);
            ps.setString(2, maLHP);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ===================== Map ResultSet → LopHocPhan =====================
    private LopHocPhan map(ResultSet rs) throws SQLException {
        return new LopHocPhan(
                rs.getString("maLHP"),
                rs.getString("maHP"),
                rs.getString("maGV"),
                rs.getInt("thu"),
                rs.getInt("tietBatDau"),
                rs.getInt("soTiet"),
                rs.getString("phong"),
                rs.getInt("siSoToiDa")
        );
    }
}
