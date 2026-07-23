package DAO;

import connection.connectBD;
import model.sinhvien;
import model.DangKyHocPhan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class sinhvienDAO {

    public List<sinhvien> layTatCaSinhVien() {
        List<sinhvien> list = new ArrayList<>();
        String sql = """
            SELECT sv.*, u.username, u.password
            FROM SinhVien sv
            JOIN Users u ON sv.maSV = u.MaSV
        """;

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapSinhVien(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public sinhvien timTheoMaSinhVien(String maSV) {
        String sql = """
            SELECT sv.*, u.username, u.password
            FROM SinhVien sv
            JOIN Users u ON sv.maSV = u.MaSV
            WHERE sv.maSV = ?
        """;

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapSinhVien(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean tonTaiTenDangNhap(String username) {
        String sql = "SELECT MaSV FROM Users WHERE username = ?";
        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean tonTaiEmail(String email) {
        String sql = "SELECT maSV FROM SinhVien WHERE email = ?";
        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean kiemTraSoDienThoaiHopLe(String sdt) {
        return sdt != null && sdt.matches("0\\d{9}");
    }

    public boolean tonTaiSoDienThoai(String sdt) {
        String sql = "SELECT maSV FROM SinhVien WHERE SDT = ?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sdt);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean tonTaiSoDienThoaiChoSinhVienKhac(String sdt, String maSV) {
        String sql = "SELECT maSV FROM SinhVien WHERE SDT = ? AND maSV <> ?";
        try (Connection c = connectBD.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, sdt);
            ps.setString(2, maSV);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themSinhVienVaTaiKhoan(String username, String password, String role, sinhvien sv) {
        if (tonTaiTenDangNhap(username)) return false;
        if (tonTaiEmail(sv.getEmail())) return false;
        if (tonTaiSoDienThoai(sv.getSdt())) return false;

        String sqlUser = "INSERT INTO Users(MaSV, username, password, role) VALUES (?, ?, ?, ?)";
        String sqlSV = """
            INSERT INTO SinhVien(maSV, hoTen, lop, nganh, khoaHoc, email, SDT)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = connectBD.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psUser = con.prepareStatement(sqlUser)) {
                psUser.setString(1, sv.getMaSV());
                psUser.setString(2, username);
                psUser.setString(3, password);
                psUser.setString(4, role);
                psUser.executeUpdate();
            }

            try (PreparedStatement psSV = con.prepareStatement(sqlSV)) {
                psSV.setString(1, sv.getMaSV());
                psSV.setString(2, sv.getHoTen());
                psSV.setString(3, sv.getLop());
                psSV.setString(4, sv.getNganh());
                psSV.setString(5, sv.getKhoaHoc());
                psSV.setString(6, sv.getEmail());
                psSV.setString(7, sv.getSdt());
                psSV.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatSinhVien(sinhvien sv, String newPassword) {
        if (tonTaiSoDienThoaiChoSinhVienKhac(sv.getSdt(), sv.getMaSV())) return false;

        String sqlUpdateSV = """
            UPDATE SinhVien
            SET hoTen=?, lop=?, nganh=?, khoaHoc=?, email=?, SDT=?
            WHERE maSV=?
        """;

        String sqlUpdatePass = "UPDATE Users SET password=? WHERE MaSV=?";

        try (Connection con = connectBD.getConnection()) {
            con.setAutoCommit(false);

            if (newPassword != null && !newPassword.isEmpty()) {
                try (PreparedStatement psPass = con.prepareStatement(sqlUpdatePass)) {
                    psPass.setString(1, newPassword);
                    psPass.setString(2, sv.getMaSV());
                    psPass.executeUpdate();
                }
            }

            try (PreparedStatement psSV = con.prepareStatement(sqlUpdateSV)) {
                psSV.setString(1, sv.getHoTen());
                psSV.setString(2, sv.getLop());
                psSV.setString(3, sv.getNganh());
                psSV.setString(4, sv.getKhoaHoc());
                psSV.setString(5, sv.getEmail());
                psSV.setString(6, sv.getSdt());
                psSV.setString(7, sv.getMaSV());
                psSV.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaSinhVien(String maSV) {
        String sqlDeleteSV = "DELETE FROM SinhVien WHERE maSV=?";
        String sqlDeleteUser = "DELETE FROM Users WHERE MaSV=?";

        try (Connection con = connectBD.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement psSV = con.prepareStatement(sqlDeleteSV)) {
                psSV.setString(1, maSV);
                psSV.executeUpdate();
            }

            try (PreparedStatement psUser = con.prepareStatement(sqlDeleteUser)) {
                psUser.setString(1, maSV);
                psUser.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<DangKyHocPhan> layHocPhanChuaDangKy(String maSV) {
        List<DangKyHocPhan> list = new ArrayList<>();
        String sql = """
            SELECT hp.maHP, hp.tenHP, hp.soTinChi
            FROM HocPhan hp
            WHERE hp.maHP NOT IN (
                SELECT maHP FROM DangKyHoc WHERE maSV=?
            )
        """;

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DangKyHocPhan hp = new DangKyHocPhan();
                hp.setMaHP(rs.getString("maHP"));
                hp.setTenHP(rs.getString("tenHP"));
                hp.setSoTinChi(rs.getInt("soTinChi"));
                list.add(hp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private sinhvien mapSinhVien(ResultSet rs) throws SQLException {
        sinhvien sv = new sinhvien(
                rs.getString("maSV"),
                rs.getString("hoTen"),
                rs.getString("lop"),
                rs.getString("nganh"),
                rs.getString("khoaHoc"),
                rs.getString("email"),
                rs.getString("SDT")
        );
        sv.setUsername(rs.getString("username"));
        sv.setPassword(rs.getString("password"));
        return sv;
    }
}
