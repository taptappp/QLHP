package service;

import DAO.LopHocPhanDAO;
import model.LopHocPhan;

import java.sql.SQLException;
import java.util.List;

public class LopHocPhanService {

    private final LopHocPhanDAO dao = new LopHocPhanDAO();

    public boolean tonTaiMaLopHocPhan(String maLHP) {
        try {
            return dao.tonTaiMaLopHocPhan(maLHP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LopHocPhan> layTatCaLopHocPhan() {
        try {
            return dao.layTatCaLopHocPhanKemGiangVien();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tải danh sách LHP", e);
        }
    }

    public LopHocPhan timTheoMaLopHocPhan(String maLHP) {
        try {
            return dao.timTheoMaLopHocPhan(maLHP);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tìm lớp học phần", e);
        }
    }

    public void themLopHocPhan(LopHocPhan l) throws Exception {
        kiemTraHopLe(l);

        if (tonTaiMaLopHocPhan(l.getMaLHP())) {
            throw new Exception("Mã lớp học phần đã tồn tại");
        }
        dao.themLopHocPhan(l);
    }

    public void capNhatLopHocPhan(LopHocPhan l) throws Exception {
        kiemTraHopLe(l);

        if (!tonTaiMaLopHocPhan(l.getMaLHP())) {
            throw new Exception("Không tìm thấy lớp học phần");
        }
        dao.capNhatLopHocPhan(l);
    }

    public void xoaLopHocPhan(String maLHP) {
        try {
            if (!tonTaiMaLopHocPhan(maLHP)) {
                throw new RuntimeException("Lớp học phần không tồn tại");
            }

            dao.xoaLopHocPhan(maLHP);
        } catch (SQLException e) {
            throw new RuntimeException("Không thể hủy lớp học phần (có thể có sinh viên đăng ký)", e);
        }
    }
    private void kiemTraHopLe(LopHocPhan l) throws Exception {

        if (l.getMaLHP() == null || l.getMaLHP().trim().isEmpty())
            throw new Exception("Mã LHP không được rỗng");

        if (l.getMaHP() == null || l.getMaHP().trim().isEmpty())
            throw new Exception("Chưa chọn học phần");

        if (l.getMaGV() == null || l.getMaGV().trim().isEmpty())
            throw new Exception("Thiếu giảng viên");

        if (l.getThu() < 2 || l.getThu() > 7)
            throw new Exception("Thứ phải từ 2 đến 7");

        if (l.getTietBatDau() < 1)
            throw new Exception("Tiết bắt đầu không hợp lệ");

        if (l.getSoTiet() <= 0)
            throw new Exception("Số tiết không hợp lệ");

        if (l.getSiSoToiDa() <= 0)
            throw new Exception("Sĩ số phải lớn hơn 0");

        if (l.getPhong() == null || l.getPhong().trim().isEmpty())
            throw new Exception("Phòng học không được rỗng");
    }

    public String layTenGiangVien(String maHP) {
        try {
            List<LopHocPhan> list = dao.layTatCaLopHocPhanKemGiangVien();
            for (LopHocPhan l : list) {
                if (maHP.equals(l.getMaHP())) {
                    return l.getTenGV() != null ? l.getTenGV() : l.getMaGV();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy tên giảng viên", e);
        }
        return "—";
    }
}
