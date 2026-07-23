package DAO;

import connection.connectBD;
import model.LopHocPhan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LopHocPhanDAO {

    public boolean tonTaiMaLopHocPhan(String maLHP) throws SQLException {
        String sql = "SELECT 1 FROM LopHocPhan WHERE maLHP = ?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            return ps.executeQuery().next();
        }
    }

    public List<LopHocPhan> layTatCaLopHocPhanKemGiangVien() throws SQLException {
        List<LopHocPhan> list = new ArrayList<>();
        String sql = """
            SELECT l.maLHP, l.maHP, l.maGV, l.thu,
                   l.tietBatDau, l.soTiet, l.phong, l.siSoToiDa,
                   g.hoTen AS tenGV
            FROM LopHocPhan l
            LEFT JOIN GiangVien g ON l.maGV = g.maGV
            ORDER BY l.maLHP
        """;

        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapLopHocPhan(rs));
            }
        }
        return list;
    }

    public void themLopHocPhan(LopHocPhan l) throws SQLException {
        String sql = """
            INSERT INTO LopHocPhan
            (maLHP, maHP, maGV, thu, tietBatDau, soTiet, phong, siSoToiDa)
            VALUES (?,?,?,?,?,?,?,?)
        """;

        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ganThamSoThem(ps, l);
            ps.executeUpdate();
        }
    }

    public boolean capNhatLopHocPhan(LopHocPhan l) throws SQLException {
        String sql = """
            UPDATE LopHocPhan SET
                maHP = ?, maGV = ?, thu = ?, tietBatDau = ?,
                soTiet = ?, phong = ?, siSoToiDa = ?
            WHERE maLHP = ?
        """;

        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getMaHP());
            ps.setString(2, l.getMaGV());
            ps.setInt(3, l.getThu());
            ps.setInt(4, l.getTietBatDau());
            ps.setInt(5, l.getSoTiet());
            ps.setString(6, l.getPhong());
            ps.setInt(7, l.getSiSoToiDa());
            ps.setString(8, l.getMaLHP());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean xoaLopHocPhan(String maLHP) throws SQLException {
        String sql = "DELETE FROM LopHocPhan WHERE maLHP = ?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maLHP);
            return ps.executeUpdate() > 0;
        }
    }

    public LopHocPhan timTheoMaLopHocPhan(String maLHP) throws SQLException {
        String sql = """
            SELECT l.maLHP, l.maHP, l.maGV, l.thu,
                   l.tietBatDau, l.soTiet, l.phong, l.siSoToiDa,
                   h.soTinChi, g.hoTen AS tenGV
            FROM LopHocPhan l
            JOIN HocPhan h ON l.maHP = h.maHP
            LEFT JOIN GiangVien g ON l.maGV = g.maGV
            WHERE l.maLHP = ?
        """;

        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maLHP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LopHocPhan l = mapLopHocPhan(rs);
                    l.setSoTinChi(rs.getInt("soTinChi"));
                    return l;
                }
                return null;
            }
        }
    }

    private LopHocPhan mapLopHocPhan(ResultSet rs) throws SQLException {
        LopHocPhan l = new LopHocPhan();
        l.setMaLHP(rs.getString("maLHP"));
        l.setMaHP(rs.getString("maHP"));
        l.setMaGV(rs.getString("maGV"));
        l.setThu(rs.getInt("thu"));
        l.setTietBatDau(rs.getInt("tietBatDau"));
        l.setSoTiet(rs.getInt("soTiet"));
        l.setPhong(rs.getString("phong"));
        l.setSiSoToiDa(rs.getInt("siSoToiDa"));
        l.setTenGV(rs.getString("tenGV"));
        return l;
    }

    private void ganThamSoThem(PreparedStatement ps, LopHocPhan l) throws SQLException {
        ps.setString(1, l.getMaLHP());
        ps.setString(2, l.getMaHP());
        ps.setString(3, l.getMaGV());
        ps.setInt(4, l.getThu());
        ps.setInt(5, l.getTietBatDau());
        ps.setInt(6, l.getSoTiet());
        ps.setString(7, l.getPhong());
        ps.setInt(8, l.getSiSoToiDa());
    }
}
