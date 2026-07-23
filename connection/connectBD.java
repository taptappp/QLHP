package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class connectBD {
    private static final String URL="jdbc:sqlserver://localhost\\SQLEXPRESS;"
            + "databaseName=QuanLiHocPhan;"
            +"encrypt=true;trustServerCertificate=true";
    private static final String USER="doanjava";
    private static final String PASSWORD="05092005";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            System.out.println("!!!!");
            e.printStackTrace();
            return null;
        }
    }
}
