package service;

import DAO.GiangVienDAO;
import model.GiangVien;

import java.util.List;

public class GiangVienService {

    private final GiangVienDAO dao = new GiangVienDAO();

    public List<GiangVien> layTatCaGiangVien() {
        return dao.layTatCaGiangVien();
    }
    public GiangVien timTheoSoDienThoai(String sdt) {
        if (sdt == null || sdt.isEmpty()) return null;
        return dao.timTheoSoDienThoai(sdt);
    }
    public boolean themGiangVien(GiangVien gv) {
        if (gv == null || gv.getMaGV() == null || gv.getMaGV().isEmpty()
                || gv.getHoTen() == null || gv.getHoTen().isEmpty()) {
            return false;
        }

        if (gv.getSDT() != null && !gv.getSDT().isEmpty()) {
            if (tonTaiSoDienThoai(gv.getSDT())) return false;
        }

        return dao.themGiangVien(gv);
    }

    public boolean capNhatGiangVien(GiangVien gv) {
        if (gv == null || gv.getMaGV() == null || gv.getMaGV().isEmpty()) {
            return false;
        }

        if (gv.getSDT() != null && !gv.getSDT().isEmpty()) {
            if (tonTaiSoDienThoaiKhacMa(gv.getSDT(), gv.getMaGV())) return false;
        }

        return dao.capNhatGiangVien(gv);
    }

    public boolean xoaGiangVien(String maGV) {
        if (maGV == null || maGV.isEmpty()) {
            return false;
        }
        return dao.xoaGiangVien(maGV);
    }

    public GiangVien timTheoMaGiangVien(String maGV) {
        if (maGV == null || maGV.isEmpty()) return null;
        return dao.timTheoMaGiangVien(maGV);
    }

    public boolean tonTaiSoDienThoai(String sdt) {
        return dao.tonTaiSoDienThoai(sdt);
    }

    public boolean tonTaiSoDienThoaiKhacMa(String sdt, String maGV) {
        GiangVien gv = dao.timTheoSoDienThoai(sdt);
        return gv != null && !gv.getMaGV().equals(maGV);
    }

}
