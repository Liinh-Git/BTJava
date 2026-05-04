package org.jobportal.bll.impl;

import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Role;

public class AuthService {
    // Chức năng: Tạo tài khoản người dùng mới
    // Đầu vào: username (String) - tên đăng nhập; email (String) - địa chỉ email; password (String) - mật khẩu; confirmPassword (String) - xác nhận; role (Role) - vai trò
    // Đầu ra: boolean - true nếu tạo thành công
    // Tương tác: Được gọi từ RegisterPanel; sẽ gọi UserDAO, CandidateDAO/EmployerDAO, PasswordUtils, ValidationUtils
    // Ghi chú: Cần validate dữ liệu và tạo transaction khi ghi DB
    public boolean register(String username, String email, String password, String confirmPassword, Role role) {
        // TODO: Bước 1 - Validate dữ liệu đầu vào và trùng lặp username/email
        // TODO: Bước 2 - Hash password và tạo user mới
        // TODO: Bước 3 - Lưu user và thông tin candidate/employer
        return false;
    }

    // Chức năng: Xác thực đăng nhập và trả về thông tin người dùng
    // Đầu vào: username (String) - tên đăng nhập; password (String) - mật khẩu; role (Role) - vai trò
    // Đầu ra: UserDTO - thông tin người dùng (null nếu thất bại)
    // Tương tác: Được gọi từ LoginPanel; sẽ gọi UserDAO, PasswordUtils, SessionManager
    // Ghi chú: Cần kiểm tra role và trạng thái is_active
    public UserDTO login(String username, String password, Role role) {
        // TODO: Bước 1 - Hash password đầu vào
        // TODO: Bước 2 - Tìm user theo username và password
        // TODO: Bước 3 - Kiểm tra role, trạng thái và lưu session
        return null;
    }

    // Chức năng: Đăng xuất và xóa session
    // Đầu vào: (void)
    // Đầu ra: void
    // Tương tác: Được gọi từ SidebarPanel; gọi SessionManager
    // Ghi chú: Sau khi clear session thì quay về LoginPanel
    public void logout() {
        // TODO: Bước 1 - Xóa session hiện tại
        // TODO: Bước 2 - Điều hướng về màn hình đăng nhập
        // TODO: Bước 3 - Xử lý các tài nguyên liên quan nếu cần
    }

    // Chức năng: Đổi mật khẩu cho người dùng đăng nhập
    // Đầu vào: oldPassword (String) - mật khẩu cũ; newPassword (String) - mật khẩu mới; confirmNewPassword (String) - xác nhận
    // Đầu ra: boolean - true nếu đổi mật khẩu thành công
    // Tương tác: Được gọi từ màn hình My Account (nếu có); gọi UserDAO, PasswordUtils, SessionManager
    // Ghi chú: Cần kiểm tra mật khẩu cũ và validate mật khẩu mới
    public boolean changePassword(String oldPassword, String newPassword, String confirmNewPassword) {
        // TODO: Bước 1 - Lấy userId từ session và kiểm tra mật khẩu cũ
        // TODO: Bước 2 - Validate mật khẩu mới và hash
        // TODO: Bước 3 - Cập nhật mật khẩu trong DB
        return false;
    }
}
