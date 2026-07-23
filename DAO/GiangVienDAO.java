package DAO;

import connection.connectBD;
import model.GiangVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiangVienDAO {

    public List<GiangVien> layTatCaGiangVien() {
        List<GiangVien> list = new ArrayList<>();
        String sql = "SELECT * FROM GiangVien";

        try (Connection c = connectBD.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapGiangVien(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public GiangVien timTheoSoDienThoai(String sdt) {
        String sql = "SELECT * FROM GiangVien WHERE SDT=?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapGiangVien(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GiangVien timTheoMaGiangVien(String maGV) {
        String sql = "SELECT * FROM GiangVien WHERE maGV=?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maGV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapGiangVien(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean kiemTraSoDienThoaiHopLe(String sdt) {
        return sdt != null && sdt.matches("0\\d{9}");
    }

    public boolean tonTaiSoDienThoai(String sdt) {
        String sql = "SELECT maGV FROM GiangVien WHERE SDT=?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themGiangVien(GiangVien gv) {
        if (!kiemTraSoDienThoaiHopLe(gv.getSDT())) return false;
        if (tonTaiSoDienThoai(gv.getSDT())) return false;

        String sql = "INSERT INTO GiangVien(maGV, hoTen, hocVi, email, SDT) VALUES (?,?,?,?,?)";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, gv.getMaGV());
            ps.setString(2, gv.getHoTen());
            ps.setString(3, gv.getHocVi());
            ps.setString(4, gv.getEmail());
            ps.setString(5, gv.getSDT());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatGiangVien(GiangVien gv) {
        if (!kiemTraSoDienThoaiHopLe(gv.getSDT())) return false;
        String sqlCheck = "SELECT maGV FROM GiangVien WHERE SDT=? AND maGV<>?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sqlCheck)) {

            ps.setString(1, gv.getSDT());
            ps.setString(2, gv.getMaGV());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "UPDATE GiangVien SET hoTen=?, hocVi=?, email=?, SDT=? WHERE maGV=?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, gv.getHoTen());
            ps.setString(2, gv.getHocVi());
            ps.setString(3, gv.getEmail());
            ps.setString(4, gv.getSDT());
            ps.setString(5, gv.getMaGV());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaGiangVien(String maGV) {
        String sql = "DELETE FROM GiangVien WHERE maGV=?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, maGV);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private GiangVien mapGiangVien(ResultSet rs) throws SQLException {
        return new GiangVien(
                rs.getString("maGV"),
                rs.getString("hoTen"),
                rs.getString("hocVi"),
                rs.getString("email"),
                rs.getString("SDT")
        );
    }
}
