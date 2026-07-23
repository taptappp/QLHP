package model;

public class sinhvien {
    private String maSV;       // Khóa chính, dùng để liên kết với Users
    private String hoTen;
    private String lop;
    private String nganh;
    private String khoaHoc;
    private String email;
    private String sdt;
    private String username;   // từ bảng Users
    private String password;   // từ bảng Users

    public sinhvien() {}

    // Constructor chính dùng trong DAO
    public sinhvien(String maSV, String hoTen, String lop, String nganh,
                    String khoaHoc, String email, String sdt) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.lop = lop;
        this.nganh = nganh;
        this.khoaHoc = khoaHoc;
        this.email = email;
        this.sdt = sdt;
    }

    // ===== Getters & Setters =====
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }

    public String getNganh() { return nganh; }
    public void setNganh(String nganh) { this.nganh = nganh; }

    public String getKhoaHoc() { return khoaHoc; }
    public void setKhoaHoc(String khoaHoc) { this.khoaHoc = khoaHoc; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return hoTen + " (" + maSV + ")";
    }
}
