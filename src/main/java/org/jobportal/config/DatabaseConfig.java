package org.jobportal.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String DEFAULT_URL = "jdbc:mysql://127.0.0.1:3306/job_portal";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static final Properties PROPERTIES = loadProperties();

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (inputStream != null) {
                properties.load(inputStream);
            }

        } catch (Exception e) {
            System.err.println("Không thể đọc file config.properties, dùng cấu hình mặc định. Lý do: " + e.getMessage());
        }

        return properties;
    }

    public static String getDbUrl() {
        String systemUrl = System.getProperty("db.url");
        if (systemUrl != null && !systemUrl.isBlank()) {
            return systemUrl;
        }

        String configUrl = PROPERTIES.getProperty("db.url");
        if (configUrl != null && !configUrl.isBlank()) {
            return configUrl;
        }

        String host = PROPERTIES.getProperty("db.host");
        String port = PROPERTIES.getProperty("db.port");
        String name = PROPERTIES.getProperty("db.name");

        if (host != null && !host.isBlank()
                && port != null && !port.isBlank()
                && name != null && !name.isBlank()) {
            return "jdbc:mysql://" + host + ":" + port + "/" + name;
        }

        return DEFAULT_URL;
    }

    public static String getDbUser() {
        String systemUsername = System.getProperty("db.username");
        if (systemUsername != null && !systemUsername.isBlank()) {
            return systemUsername;
        }

        String configUsername = PROPERTIES.getProperty("db.username");
        if (configUsername != null && !configUsername.isBlank()) {
            return configUsername;
        }

        String configUser = PROPERTIES.getProperty("db.user");
        if (configUser != null && !configUser.isBlank()) {
            return configUser;
        }

        return DEFAULT_USER;
    }

    public static String getDbPassword() {
        String systemPassword = System.getProperty("db.password");
        if (systemPassword != null) {
            return systemPassword;
        }

        return PROPERTIES.getProperty("db.password", DEFAULT_PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Giữ driver JDBC cũ theo cấu hình hiện tại của project
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy MySQL Driver!", e);
        }

        return DriverManager.getConnection(getDbUrl(), getDbUser(), getDbPassword());
    }

    public static boolean checkConnection() {
        try (Connection connection = getConnection()) {
            return connection != null;
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra kết nối: " + e.getMessage());
            return false;
        }
    }
}