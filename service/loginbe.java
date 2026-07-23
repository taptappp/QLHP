package service;

import DAO.DangnhapDAO;
import model.login;

public class loginbe {

    private final DangnhapDAO dao = new DangnhapDAO();


    public login dangNhap(String username, String password) {

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return null;
        }

        return dao.kiemTraDangNhap(username.trim(), password.trim());
    }
}
