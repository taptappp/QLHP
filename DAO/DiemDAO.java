package DAO;

import connection.connectBD;
import model.Diem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiemDAO {
    public List<Diem> layDiemTheoSinhVien(String maSV) {
        List<Diem> list = new ArrayList<>();

        String sql = """
            SELECT 
                dk.maDangKy,
                hp.maHP,
                hp.tenHP,
                d.diemQT,
                d.diemThi,
                d.diemTB
            FROM DangKyHoc dk
            JOIN HocPhan hp ON dk.maHP = hp.maHP
            LEFT JOIN Diem d ON dk.maDangKy = d.maDangKy
            WHERE dk.maSV = ?
        """;

        try (Connection conn = connectBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Float diemQT  = rs.getObject("diemQT")  != null ? rs.getFloat("diemQT")  : null;
                Float diemThi = rs.getObject("diemThi") != null ? rs.getFloat("diemThi") : null;
                Float diemTB  = rs.getObject("diemTB")  != null ? rs.getFloat("diemTB")  : null;

                list.add(new Diem(
                        rs.getString("maDangKy"),
                        rs.getString("maHP"),
                        rs.getString("tenHP"),
                        diemQT,
                        diemThi,
                        diemTB
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean capNhatDiem(Diem d) {
        if (d == null || d.getMaDangKy() == null) return false;

        String sql = """
            MERGE INTO Diem AS target
            USING (SELECT ? AS maDangKy) AS source
            ON target.maDangKy = source.maDangKy
            WHEN MATCHED THEN
                UPDATE SET 
                    diemQT = ?, 
                    diemThi = ?, 
                    diemTB = ?
            WHEN NOT MATCHED THEN
                INSERT (maDiem, maDangKy, maHP, diemQT, diemThi, diemTB)
                VALUES (?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = connectBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getMaDangKy());
            ps.setObject(2, d.getDiemQT());
            ps.setObject(3, d.getDiemThi());
            ps.setObject(4, d.getDiemTB());

            ps.setString(5, d.getMaDangKy());
            ps.setString(6, d.getMaDangKy());
            ps.setString(7, d.getMaHP());
            ps.setObject(8, d.getDiemQT());
            ps.setObject(9, d.getDiemThi());
            ps.setObject(10, d.getDiemTB());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
