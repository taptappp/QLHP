package DAO;

import connection.connectBD;
import model.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangnhapDAO {
    public login kiemTraDangNhap(String user, String pass) {
        String sql = "SELECT MaSV, username, role FROM Users WHERE username = ? AND password = ?";

        try (Connection con = connectBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new login(
                        rs.getString("MaSV"),
                        rs.getString("username"),
                        rs.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
