package model;

import java.util.Map;

public class tongquan {
    private int soSinhVien;
    private int soGiangVien;
    private int soHocPhan;
    private int soDangKy;
    private int soTaiKhoan;

    private Map<String, Integer> sinhVienTheoLop;
    private Map<String, Float> diemTBTheoHP;
    private float diemTBHeThong;
    private float tyLeLapDay;

    private int soTinChiDaHoc;
    private float diemTBTichLuy;


    public tongquan() {
    }

    public tongquan(int soSinhVien, int soGiangVien, int soHocPhan, int soDangKy, int soTaiKhoan) {
        this.soSinhVien = soSinhVien;
        this.soGiangVien = soGiangVien;
        this.soHocPhan = soHocPhan;
        this.soDangKy = soDangKy;
        this.soTaiKhoan = soTaiKhoan;
    }

    public int getSoSinhVien() { return soSinhVien; }
    public void setSoSinhVien(int soSinhVien) { this.soSinhVien = soSinhVien; }

    public int getSoGiangVien() { return soGiangVien; }
    public void setSoGiangVien(int soGiangVien) { this.soGiangVien = soGiangVien; }

    public int getSoHocPhan() { return soHocPhan; }
    public void setSoHocPhan(int soHocPhan) { this.soHocPhan = soHocPhan; }

    public int getSoDangKy() { return soDangKy; }
    public void setSoDangKy(int soDangKy) { this.soDangKy = soDangKy; }

    public int getSoTaiKhoan() { return soTaiKhoan; }
    public void setSoTaiKhoan(int soTaiKhoan) { this.soTaiKhoan = soTaiKhoan; }

    public Map<String, Integer> getSinhVienTheoLop() { return sinhVienTheoLop; }
    public void setSinhVienTheoLop(Map<String, Integer> sinhVienTheoLop) { this.sinhVienTheoLop = sinhVienTheoLop; }

    public Map<String, Float> getDiemTBTheoHP() { return diemTBTheoHP; }
    public void setDiemTBTheoHP(Map<String, Float> diemTBTheoHP) { this.diemTBTheoHP = diemTBTheoHP; }

    public float getDiemTBHeThong() { return diemTBHeThong; }
    public void setDiemTBHeThong(float diemTBHeThong) { this.diemTBHeThong = diemTBHeThong; }

    public float getTyLeLapDay() { return tyLeLapDay; }
    public void setTyLeLapDay(float tyLeLapDay) { this.tyLeLapDay = tyLeLapDay; }

    public int getSoTinChiDaHoc() { return soTinChiDaHoc; }
    public void setSoTinChiDaHoc(int soTinChiDaHoc) { this.soTinChiDaHoc = soTinChiDaHoc; }

    public float getDiemTBTichLuy() { return diemTBTichLuy; }
    public void setDiemTBTichLuy(float diemTBTichLuy) { this.diemTBTichLuy = diemTBTichLuy; }

    public double[] getTongHopSoLieu() {
        return new double[]{soSinhVien, soGiangVien, soHocPhan};
    }
}
