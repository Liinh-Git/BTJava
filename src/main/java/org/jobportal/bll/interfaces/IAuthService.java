package org.jobportal.bll.interfaces;

import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Role;

public interface IAuthService {
    // Chuc nang: Tao tai khoan nguoi dung moi
    // Dau vao: username (String), email (String), password (String), confirmPassword (String), role (Role)
    // Dau ra: boolean
    boolean register(String username, String email, String password, String confirmPassword, Role role);

    // Chuc nang: Xac thuc dang nhap va tra ve thong tin nguoi dung
    // Dau vao: username (String), password (String), role (Role)
    // Dau ra: UserDTO
    UserDTO login(String username, String password, Role role);

    // Chuc nang: Dang xuat va xoa session
    // Dau vao: (void)
    // Dau ra: void
    void logout();

    // Chuc nang: Doi mat khau cho nguoi dung dang nhap
    // Dau vao: oldPassword (String), newPassword (String), confirmNewPassword (String)
    // Dau ra: boolean
    boolean changePassword(String oldPassword, String newPassword, String confirmNewPassword);
}
