package model;

public class Diem {

    private String maDangKy;
    private String maSV;
    private String maHP;
    private String tenHP;

    private Float diemQT;
    private Float diemThi;
    private Float diemTB;

    public Diem() {}

    public Diem(String maDangKy, String maHP, String tenHP,
                Float diemQT, Float diemThi, Float diemTB) {
        this.maDangKy = maDangKy;
        this.maHP = maHP;
        this.tenHP = tenHP;
        this.diemQT = diemQT;
        this.diemThi = diemThi;
        this.diemTB = diemTB;
    }

    public Diem(String maDangKy, String maSV, String maHP, String tenHP,
                Float diemQT, Float diemThi, Float diemTB) {
        this.maDangKy = maDangKy;
        this.maSV = maSV;
        this.maHP = maHP;
        this.tenHP = tenHP;
        this.diemQT = diemQT;
        this.diemThi = diemThi;
        this.diemTB = diemTB;
    }

    public String getMaDangKy() { return maDangKy; }
    public void setMaDangKy(String maDangKy) { this.maDangKy = maDangKy; }

    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getMaHP() { return maHP; }
    public void setMaHP(String maHP) { this.maHP = maHP; }

    public String getTenHP() { return tenHP; }
    public void setTenHP(String tenHP) { this.tenHP = tenHP; }

    public Float getDiemQT() { return diemQT; }
    public void setDiemQT(Float diemQT) { this.diemQT = diemQT; }

    public Float getDiemThi() { return diemThi; }
    public void setDiemThi(Float diemThi) { this.diemThi = diemThi; }

    public Float getDiemTB() {
        return diemTB;
    }

    public void setDiemTB(Float diemTB) { this.diemTB = diemTB; }

    public Float tinhDiemTB() {
        if (diemQT == null && diemThi == null) {
            return null;
        }
        float qt = (diemQT != null) ? diemQT : 0f;
        float thi = (diemThi != null) ? diemThi : 0f;
        float diem = qt * 0.4f + thi * 0.6f;
        return Math.round(diem * 100f) / 100f;
    }
}
