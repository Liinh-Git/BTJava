package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.enums.Role;
import org.jobportal.model.User;

public interface IUserDAO {
    // Ch?c nang: Tìm user theo username và password hash
    // Ð?u vào: username (String), passwordHash (String)
    // Ð?u ra: User
    User findByUsernameAndPassword(String username, String passwordHash);

    // Ch?c nang: Tìm user theo id
    // Ð?u vào: userId (String)
    // Ð?u ra: User
    User findById(String userId);

    // Ch?c nang: L?y danh sách user có l?c
    // Ð?u vào: roleFilter (Role), statusFilter (Boolean)
    // Ð?u ra: List<User>
    List<User> findAll(Role roleFilter, Boolean statusFilter);

    // Ch?c nang: Thêm user
    // Ð?u vào: user (User)
    // Ð?u ra: boolean
    boolean insert(User user);

    // Ch?c nang: C?p nh?t user
    // Ð?u vào: user (User)
    // Ð?u ra: boolean
    boolean update(User user);

    // Ch?c nang: C?p nh?t tr?ng thái user
    // Ð?u vào: userId (String), isActive (boolean)
    // Ð?u ra: boolean
    boolean updateStatus(String userId, boolean isActive);

    // Ch?c nang: C?p nh?t m?t kh?u dã hash
    // Ð?u vào: userId (String), passwordHash (String)
    // Ð?u ra: boolean
    boolean updatePassword(String userId, String passwordHash);

    // Ch?c nang: Xóa user
    // Ð?u vào: userId (String)
    // Ð?u ra: boolean
    boolean delete(String userId);

    // Ch?c nang: Ki?m tra t?n t?i username
    // Ð?u vào: username (String)
    // Ð?u ra: boolean
    boolean existsByUsername(String username);

    // Ch?c nang: Ki?m tra t?n t?i email
    // Ð?u vào: email (String)
    // Ð?u ra: boolean
    boolean existsByEmail(String email);

    // Ch?c nang: Ð?m user theo role
    // Ð?u vào: role (Role)
    // Ð?u ra: int
    int countByRole(Role role);
}
