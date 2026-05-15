package org.jobportal.utils;

import java.util.regex.Pattern;

/**
 * ValidationUtils - Tap hop cac ham validate dung chung cho Service layer.
 * Khong goi Swing, khong goi DAO, khong co SQL.
 */
public class ValidationUtils {

    /** Pattern kiem tra dinh dang email co ban */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Ngan khoi tao instance ben ngoai
    private ValidationUtils() {}

    /**
     * Kiem tra chuoi null hoac rong (sau khi trim).
     * @return true neu null hoac blank
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Kiem tra dinh dang email.
     * @return true neu email hop le
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Kiem tra do dai mat khau toi thieu 6 ky tu.
     * @return true neu mat khau hop le
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Kiem tra hai chuoi mat khau khop nhau.
     * @return true neu khop
     */
    public static boolean isPasswordMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }

    /**
     * Kiem tra username: khong null/rong, 4-50 ky tu, chi chua chu/so/gach duoi.
     * @return true neu hop le
     */
    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username)) return false;
        String u = username.trim();
        return u.length() >= 4 && u.length() <= 50 && u.matches("[A-Za-z0-9_]+");
    }
}

