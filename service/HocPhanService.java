package service;

import DAO.HocPhanDAO;
import model.HocPhan;

import java.util.ArrayList;
import java.util.List;

public class HocPhanService {

    private final HocPhanDAO hocPhanDAO = new HocPhanDAO();

    private List<HocPhan> cacheHocPhan;

    public List<HocPhan> layTatCaHocPhan() {
        if (cacheHocPhan == null) {
            cacheHocPhan = hocPhanDAO.layTatCaHocPhan();
        }
        return cacheHocPhan;
    }

    public List<HocPhan> layTatCaHocPhanKemGiangVien() {
        if (cacheHocPhan == null) {
            cacheHocPhan = hocPhanDAO.layTatCaHocPhanKemGiangVien();
        }
        return cacheHocPhan;
    }

    public HocPhan timTheoMaHocPhan(String maHP) {
        for (HocPhan hp : layTatCaHocPhan()) {
            if (hp.getMaHP().equals(maHP)) {
                return hp;
            }
        }
        return null;
    }

    public boolean themHocPhan(HocPhan hp) {
        if (!hopLe(hp)) return false;
        boolean ok = hocPhanDAO.themHocPhan(hp);
        cacheHocPhan = null;
        return ok;
    }

    public boolean capNhatHocPhan(HocPhan hp) {
        if (!hopLe(hp)) return false;
        boolean ok = hocPhanDAO.capNhatHocPhan(hp);
        cacheHocPhan = null;
        return ok;
    }

    public boolean xoaHocPhan(String maHP) {
        boolean ok = hocPhanDAO.xoaHocPhan(maHP);
        cacheHocPhan = null;
        return ok;
    }

    private boolean hopLe(HocPhan hp) {
        return hp != null
                && hp.getMaHP() != null && !hp.getMaHP().isEmpty()
                && hp.getTenHP() != null && !hp.getTenHP().isEmpty()
                && hp.getSoTinChi() > 0;
    }

    public List<String> layTatCaMaHocPhan() {
        List<String> list = new ArrayList<>();
        for (HocPhan hp : layTatCaHocPhan()) {
            list.add(hp.getMaHP());
        }
        return list;
    }

    public String layTenHocPhan(String maHP) {
        HocPhan hp = timTheoMaHocPhan(maHP);
        return hp != null ? hp.getTenHP() : "";
    }

    public String layMaGiangVien(String maHP) {
        HocPhan hp = timTheoMaHocPhan(maHP);
        return hp != null ? hp.getMaGV() : null;
    }

    public String layTenGiangVien(String maHP) {
        HocPhan hp = timTheoMaHocPhan(maHP);
        return hp != null && hp.getMaGV() != null ? hp.getMaGV() : "—";
    }
}
