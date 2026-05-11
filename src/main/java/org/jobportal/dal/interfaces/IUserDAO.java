package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.enums.Role;
import org.jobportal.model.User;

public interface IUserDAO {
    // Chức năng: Tìm user theo username và password hash
    // Đầu vào: username (String), passwordHash (String)
    // Đầu ra: User
    User findByUsernameAndPassword(String username, String passwordHash);

    // Chức năng: Tìm user theo id
    // Đầu vào: userId (String)
    // Đầu ra: User
    User findById(String userId);

    // Chức năng: Lấy danh sách user có lọc
    // Đầu vào: roleFilter (Role), statusFilter (Boolean)
    // Đầu ra: List<User>
    List<User> findAll(Role roleFilter, Boolean statusFilter);

    // Chức năng: Thêm user
    // Đầu vào: user (User)
    // Đầu ra: boolean
    boolean insert(User user);

    // Chức năng: C?p nh?t user
    // Đầu vào: user (User)
    // Đầu ra: boolean
    boolean update(User user);

    // Chức năng: Cập nhật trạng thái user
    // Đầu vào: userId (String), isActive (boolean)
    // Đầu ra: boolean
    boolean updateStatus(String userId, boolean isActive);

    // Chức năng: Cập nhật mật khẩu hash
    // Đầu vào: userId (String), passwordHash (String)
    // Đầu ra: boolean
    boolean updatePassword(String userId, String passwordHash);

    // Chức năng: Xóa user
    // Đầu vào: userId (String)
    // Đầu ra: boolean
    boolean delete(String userId);

    // Chức năng: Kiểm tra tồn tại username
    // Đầu vào: username (String)
    // Đầu ra: boolean
    boolean existsByUsername(String username);

    // Chức năng: Kiểm tra tồn tại email
    // Đầu vào: email (String)
    // Đầu ra: boolean
    boolean existsByEmail(String email);

    // Chức năng: Tìm user theo role
    // Đầu vào: role (Role)
    // Đầu ra: int
    int countByRole(Role role);
}
