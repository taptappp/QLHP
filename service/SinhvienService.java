package service;

import DAO.sinhvienDAO;
import model.DangKyHocPhan;
import model.sinhvien;
import model.login;

import java.util.List;

public class SinhvienService {

    private final sinhvienDAO dao = new sinhvienDAO();

    public enum AddResult {
        SUCCESS,
        DUPLICATE_MASV,
        DUPLICATE_EMAIL,
        DUPLICATE_USERNAME,
        DUPLICATE_SDT,
        INVALID_SDT,
        ERROR
    }

    public AddResult themSinhVien(String username, String password, String role, sinhvien sv) {
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                role == null || role.isEmpty() ||
                sv == null ||
                sv.getMaSV() == null || sv.getMaSV().isEmpty()) {
            return AddResult.ERROR;
        }

        if (dao.timTheoMaSinhVien(sv.getMaSV()) != null) return AddResult.DUPLICATE_MASV;
        if (dao.tonTaiEmail(sv.getEmail())) return AddResult.DUPLICATE_EMAIL;
        if (dao.tonTaiTenDangNhap(username)) return AddResult.DUPLICATE_USERNAME;
        if (!dao.kiemTraSoDienThoaiHopLe(sv.getSdt())) return AddResult.INVALID_SDT;
        if (dao.tonTaiSoDienThoai(sv.getSdt())) return AddResult.DUPLICATE_SDT;

        boolean ok = dao.themSinhVienVaTaiKhoan(username, password, role, sv);
        return ok ? AddResult.SUCCESS : AddResult.ERROR;
    }

    public sinhvien layThongTinSinhVien(login account) {
        if (account == null || account.getMaSV() == null || account.getMaSV().isEmpty()) return null;
        return dao.timTheoMaSinhVien(account.getMaSV());
    }

    public sinhvien layThongTinSinhVienTheoMaSV(String maSV) {
        if (maSV == null || maSV.isEmpty()) return null;
        return dao.timTheoMaSinhVien(maSV);
    }

    public List<sinhvien> layDanhSachSinhVien() {
        return dao.layTatCaSinhVien();
    }

    public boolean capNhatSinhVien(sinhvien sv, String newPassword) {
        if (sv == null) return false;

        sinhvien existing = dao.timTheoMaSinhVien(sv.getMaSV());
        if (existing == null) return false;

        if (!existing.getEmail().equalsIgnoreCase(sv.getEmail())) {
            if (dao.tonTaiEmail(sv.getEmail())) return false;
        }

        if (!dao.kiemTraSoDienThoaiHopLe(sv.getSdt())) return false;
        if (dao.tonTaiSoDienThoaiChoSinhVienKhac(sv.getSdt(), sv.getMaSV())) return false;

        if (newPassword != null && newPassword.isBlank()) newPassword = null;

        return dao.capNhatSinhVien(sv, newPassword);
    }

    public boolean xoaSinhVien(String maSV) {
        if (maSV == null || maSV.isEmpty()) return false;
        sinhvien sv = dao.timTheoMaSinhVien(maSV);
        if (sv == null) return false;
        return dao.xoaSinhVien(maSV);
    }

    public List<DangKyHocPhan> layHocPhanChuaDangKy(String maSV) {
        if (maSV == null || maSV.isEmpty()) return List.of();
        return dao.layHocPhanChuaDangKy(maSV);
    }
}
