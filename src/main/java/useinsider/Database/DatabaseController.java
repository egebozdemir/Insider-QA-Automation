package useinsider.Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseController {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bootcampdb";
    private static final String USER = "root";
    private static final String PASS = "KqzC5*LZxn^X4Q";

    private Connection conn;

    public DatabaseController() {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertTestResult(String caseName, String caseTestClass, String status, String caseStackTrace, Long caseDuration, String caseScreenshot) {
        String query = "INSERT INTO testresults (caseName, caseClass, caseStatus, caseStackTrace, caseDuration, caseScreenshot) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement prepStmt = conn.prepareStatement(query)) {
            prepStmt.setString(1, caseName);
            prepStmt.setString(2, caseTestClass);
            prepStmt.setString(3, status);
            prepStmt.setString(4, caseStackTrace);
            prepStmt.setString(5, String.valueOf(caseDuration));
            //if case is failed: inserts screenshot, if not: inserts null --> caseScreenshot column is accepting null in DB
            if(status.equals("failed")){
                InputStream inputStream = Files.newInputStream(Path.of(caseScreenshot));
                prepStmt.setBlob(6, inputStream);
            }else {
                prepStmt.setBlob(6, (InputStream) null);
            }
            prepStmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
