package model;

public class LopHocPhan {

    private String maLHP;
    private String maHP;
    private String maGV;
    private int thu;
    private int tietBatDau;
    private int soTiet;
    private String phong;
    private int siSoToiDa;
    private String tenGV;
    private int siSoHienTai;
    private boolean biTrungLich;


    public int getSiSoHienTai() { return siSoHienTai; }
    public void setSiSoHienTai(int siSoHienTai) { this.siSoHienTai = siSoHienTai; }

    public boolean isBiTrungLich() { return biTrungLich; }
    public void setBiTrungLich(boolean biTrungLich) { this.biTrungLich = biTrungLich; }

    private int soTinChi;

    public LopHocPhan() {
    }

    public LopHocPhan(String maLHP, String maHP, String maGV,
                      int thu, int tietBatDau, int soTiet,
                      String phong, int siSoToiDa) {
        this.maLHP = maLHP;
        this.maHP = maHP;
        this.maGV = maGV;
        this.thu = thu;
        this.tietBatDau = tietBatDau;
        this.soTiet = soTiet;
        this.phong = phong;
        this.siSoToiDa = siSoToiDa;
    }

    public String getTenGV() { return tenGV; }
    public String getMaLHP() { return maLHP; }
    public String getMaHP() { return maHP; }
    public String getMaGV() { return maGV; }
    public int getThu() { return thu; }
    public int getTietBatDau() { return tietBatDau; }
    public int getSoTiet() { return soTiet; }
    public String getPhong() { return phong; }
    public int getSiSoToiDa() { return siSoToiDa; }
    public int getSoTinChi() { return soTinChi; }


    public void setMaLHP(String maLHP) { this.maLHP = maLHP; }
    public void setMaHP(String maHP) { this.maHP = maHP; }
    public void setMaGV(String maGV) { this.maGV = maGV; }
    public void setThu(int thu) { this.thu = thu; }
    public void setTietBatDau(int tietBatDau) { this.tietBatDau = tietBatDau; }
    public void setSoTiet(int soTiet) { this.soTiet = soTiet; }
    public void setPhong(String phong) { this.phong = phong; }
    public void setSiSoToiDa(int siSoToiDa) { this.siSoToiDa = siSoToiDa; }
    public void setTenGV(String tenGV) { this.tenGV = tenGV; }

    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

}
