package org.jobportal.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL      = "jdbc:mysql://127.0.0.1:3306/job_portal";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Driver cũ
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy MySQL Driver!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean checkConnection() {
        // Dùng try-with-resources để tự đóng kết nối ngay sau khi test xong
        try (Connection conn = getConnection()) {
            return conn != null;
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra kết nối: " + e.getMessage());
            return false;
        }
    }
}