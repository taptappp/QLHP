package model;

public class login {

    private String maSV;
    private String username;
    private String role;

    public login(String maSV, String username, String role) {
        this.maSV = maSV;
        this.username = username;
        this.role = role;
    }

    public String getMaSV() { return maSV; }
    public String getUsername() { return username; }
    public String getRole() { return role; }

    public void setMaSV(String maSV) { this.maSV = maSV; }
    public void setUsername(String username) { this.username = username; }
    public void setRole(String role) { this.role = role; }

    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }
    public boolean isSinhVien() { return "sinhvien".equalsIgnoreCase(role); }
}
