package service;

import DAO.DiemDAO;
import model.Diem;

import java.util.List;

public class DiemService {

    private final DiemDAO dao = new DiemDAO();

    public List<Diem> layDiemTheoSinhVien(String maSV) {
        return dao.layDiemTheoSinhVien(maSV);
    }

    public boolean capNhatDiem(Diem d) {
        if (d == null || d.getMaDangKy() == null) return false;

        d.setDiemTB(d.tinhDiemTB());

        if ((d.getDiemQT() != null && (d.getDiemQT() < 0 || d.getDiemQT() > 10)) ||
                (d.getDiemThi() != null && (d.getDiemThi() < 0 || d.getDiemThi() > 10))) {
            return false;
        }

        return dao.capNhatDiem(d);
    }
}
