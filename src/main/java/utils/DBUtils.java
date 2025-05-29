package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=PhoneSt;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "123";
    
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
} 