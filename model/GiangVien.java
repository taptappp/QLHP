package model;

public class GiangVien {
    private String maGV;
    private String hoTen;
    private String hocVi;
    private String email;
    private String SDT;

    public GiangVien() {}

    public GiangVien(String maGV, String hoTen, String hocVi, String email) {
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.hocVi = hocVi;
        this.email = email;
        this.SDT = ""; // mặc định rỗng
    }

    public GiangVien(String maGV, String hoTen, String hocVi, String email, String SDT) {
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.hocVi = hocVi;
        this.email = email;
        this.SDT = SDT;
    }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getHocVi() { return hocVi; }
    public void setHocVi(String hocVi) { this.hocVi = hocVi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSDT() { return SDT; } // getter SDT
    public void setSDT(String SDT) { this.SDT = SDT; } // setter SDT
}
