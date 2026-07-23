package DAO;

import connection.connectBD;
import model.HocPhan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HocPhanDAO {

    public List<HocPhan> layTatCaHocPhanKemGiangVien() {
        List<HocPhan> list = new ArrayList<>();
        String sql = """
                SELECT h.maHP, h.tenHP, h.soTinChi, h.maGV, g.hoTen AS tenGV
                FROM HocPhan h
                LEFT JOIN GiangVien g ON h.maGV = g.maGV
                ORDER BY h.maHP
                """;

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HocPhan hp = new HocPhan(
                        rs.getString("maHP"),
                        rs.getString("tenHP"),
                        rs.getInt("soTinChi"),
                        rs.getString("maGV")
                );
                hp.setTenGV(rs.getString("tenGV")); // set tên GV
                list.add(hp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<HocPhan> layTatCaHocPhan() {
        List<HocPhan> list = new ArrayList<>();
        String sql = "SELECT maHP, tenHP, soTinChi, maGV FROM HocPhan ORDER BY maHP";

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new HocPhan(
                        rs.getString("maHP"),
                        rs.getString("tenHP"),
                        rs.getInt("soTinChi"),
                        rs.getString("maGV")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public HocPhan timTheoMaHocPhan(String maHP) {
        String sql = """
                SELECT h.maHP, h.tenHP, h.soTinChi, h.maGV, g.hoTen AS tenGV
                FROM HocPhan h
                LEFT JOIN GiangVien g ON h.maGV = g.maGV
                WHERE h.maHP = ?
                """;

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HocPhan hp = new HocPhan(
                            rs.getString("maHP"),
                            rs.getString("tenHP"),
                            rs.getInt("soTinChi"),
                            rs.getString("maGV")
                    );
                    hp.setTenGV(rs.getString("tenGV"));
                    return hp;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themHocPhan(HocPhan hp) {
        String sql = "INSERT INTO HocPhan(maHP, tenHP, soTinChi, maGV) VALUES (?,?,?,?)";

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hp.getMaHP());
            ps.setString(2, hp.getTenHP());
            ps.setInt(3, hp.getSoTinChi());
            if (hp.getMaGV() != null) {
                ps.setString(4, hp.getMaGV());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatHocPhan(HocPhan hp) {
        String sql = "UPDATE HocPhan SET tenHP = ?, soTinChi = ?, maGV = ? WHERE maHP = ?";

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hp.getTenHP());
            ps.setInt(2, hp.getSoTinChi());
            if (hp.getMaGV() != null) {
                ps.setString(3, hp.getMaGV());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            ps.setString(4, hp.getMaHP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHocPhan(String maHP) {
        String sql = "DELETE FROM HocPhan WHERE maHP = ?";

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHP);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
