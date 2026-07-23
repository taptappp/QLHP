package service;

import DAO.DangKyHocPhanDAO;
import model.LopHocPhan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DangKyHocPhanService {

    private final DangKyHocPhanDAO dao = new DangKyHocPhanDAO();

    // ===================== Lấy dữ liệu nhanh =====================

    public List<LopHocPhan> layLopHocPhanChuaDangKy(String maSV){
        return dao.layLopHocPhanChuaDangKy(maSV);
    }

    public List<LopHocPhan> layLopHocPhanDaDangKy(String maSV){
        return dao.layLopHocPhanDaDangKy(maSV);
    }

    // ===================== Đăng ký / Hủy =====================

    public boolean dangKyHocPhan(String maSV, LopHocPhan lopMoi){
        if (dao.daDangKy(maSV, lopMoi.getMaLHP())) return false;
        if (!dao.conChoTrongLop(lopMoi.getMaLHP())) return false;
        if (kiemTraTrungLich(maSV, lopMoi)) return false;
        return dao.dangKy(maSV, lopMoi.getMaLHP(), lopMoi.getMaHP());
    }

    public boolean huyDangKyHocPhan(String maSV, String maLHP){
        return dao.huyDangKy(maSV, maLHP);
    }

    // ===================== Tính tổng tín chỉ =====================

    public int tinhTongTinChi(String maSV){
        int tong = 0;
        List<LopHocPhan> daDK = layLopHocPhanDaDangKy(maSV);
        if(daDK != null){
            for (LopHocPhan l : daDK){
                tong += l.getSoTinChi();
            }
        }
        return tong;
    }

    // ===================== Trùng lịch =====================

    public boolean kiemTraTrungLich(String maSV, LopHocPhan moi){
        List<LopHocPhan> daDK = layLopHocPhanDaDangKy(maSV);
        if(daDK != null){
            for (LopHocPhan l : daDK){
                if (l.getThu() == moi.getThu()){
                    int bd1 = l.getTietBatDau();
                    int kt1 = bd1 + l.getSoTiet() - 1;

                    int bd2 = moi.getTietBatDau();
                    int kt2 = bd2 + moi.getSoTiet() - 1;

                    if (bd1 <= kt2 && bd2 <= kt1){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ===================== Tối ưu hóa sĩ số & trùng lịch =====================

    /**
     * Cập nhật sĩ số hiện tại cho tất cả lớp
     */
    public void capNhatSiSo(List<LopHocPhan> ds){
        if(ds == null || ds.isEmpty()) return;

        // Lấy sĩ số tất cả lớp 1 lần
        Map<String,Integer> siSoMap = dao.laySiSoHienTaiNhieuLop(ds);

        for(LopHocPhan l : ds){
            Integer ss = siSoMap.get(l.getMaLHP());
            l.setSiSoHienTai(ss != null ? ss : 0);
        }
    }

    /**
     * Cập nhật sĩ số & kiểm tra trùng lịch cho danh sách lớp chưa đăng ký
     */
    public void capNhatSiSoVaTrungLich(String maSV, List<LopHocPhan> ds){
        if(ds == null || ds.isEmpty()) return;

        // Lấy sĩ số tất cả lớp 1 lần
        Map<String,Integer> siSoMap = dao.laySiSoHienTaiNhieuLop(ds);

        // Lấy tất cả lớp đã đăng ký của sinh viên 1 lần
        List<LopHocPhan> daDK = layLopHocPhanDaDangKy(maSV);

        for(LopHocPhan l : ds){
            Integer ss = siSoMap.get(l.getMaLHP());
            l.setSiSoHienTai(ss != null ? ss : 0);

            // Kiểm tra trùng lịch
            l.setBiTrungLich(false);
            for(LopHocPhan d : daDK){
                if(l.getThu() == d.getThu()){
                    int bd1 = l.getTietBatDau();
                    int kt1 = bd1 + l.getSoTiet() - 1;

                    int bd2 = d.getTietBatDau();
                    int kt2 = bd2 + d.getSoTiet() - 1;

                    if(bd1 <= kt2 && bd2 <= kt1){
                        l.setBiTrungLich(true);
                        break;
                    }
                }
            }
        }
    }
}
