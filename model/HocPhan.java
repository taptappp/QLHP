package model;

public class HocPhan {

    private String maHP;
    private String tenHP;
    private int soTinChi;
    private String maGV;
    private String tenGV;

    public HocPhan() {}

    public HocPhan(String maHP, String tenHP, int soTinChi, String maGV) {
        this.maHP = maHP;
        this.tenHP = tenHP;
        this.soTinChi = soTinChi;
        this.maGV = maGV;
    }

    public HocPhan(String maHP, String tenHP, int soTinChi, String maGV, String tenGV) {
        this.maHP = maHP;
        this.tenHP = tenHP;
        this.soTinChi = soTinChi;
        this.maGV = maGV;
        this.tenGV = tenGV;
    }

    public String getMaHP() {
        return maHP;
    }

    public void setMaHP(String maHP) {
        this.maHP = maHP;
    }

    public String getTenHP() {
        return tenHP;
    }

    public void setTenHP(String tenHP) {
        this.tenHP = tenHP;
    }

    public int getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getTenGV() {
        return tenGV;
    }

    public void setTenGV(String tenGV) {
        this.tenGV = tenGV;
    }
}
