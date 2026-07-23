package service;

import DAO.ThongKeDAO;
import model.tongquan;

public class tongquanbe {

    private final ThongKeDAO dao = new ThongKeDAO();

    public tongquan layThongKeTongQuan() {
        return dao.layThongKe();
    }

    public tongquan layThongKeTheoSinhVien(String maSV) {
        return dao.layThongKeTheoSinhVien(maSV);
    }

    public Object[][] layDuLieuBang(String tableName) {
        return dao.layDuLieuBang(tableName);
    }

    public Object[][] layDuLieuBangTheoSinhVien(String tableName, String username) {
        return dao.layDuLieuBangTheoSinhVien(tableName, username);
    }

    public String[] layTenCotBang(String tableName) {
        return dao.layTenCotBang(tableName);
    }
}
