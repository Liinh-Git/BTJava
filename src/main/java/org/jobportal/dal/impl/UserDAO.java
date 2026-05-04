package org.jobportal.dal.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.enums.Role;
import org.jobportal.model.User;

public class UserDAO {
    // Chức năng: Tìm user theo username và password
    // Đầu vào: username (String) - tên đăng nhập; password (String) - mật khẩu đã hash
    // Đầu ra: User - user tìm thấy
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public User findByUsernameAndPassword(String username, String password) {
        // TODO: Bước 1 - Query SELECT theo username và password
        // TODO: Bước 2 - Map ResultSet sang User
        // TODO: Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Tìm user theo id
    // Đầu vào: userId (String) - mã user
    // Đầu ra: User - user tìm thấy
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public User findById(String userId) {
        // TODO: Bước 1 - Query SELECT theo userId
        // TODO: Bước 2 - Map ResultSet sang User
        // TODO: Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Lấy danh sách user có lọc
    // Đầu vào: roleFilter (Role) - lọc theo role; statusFilter (Boolean) - lọc theo trạng thái
    // Đầu ra: List<User> - danh sách user
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: roleFilter hoặc statusFilter có thể null
    public List<User> findAll(Role roleFilter, Boolean statusFilter) {
        // TODO: Bước 1 - Tạo query SELECT với bộ lọc
        // TODO: Bước 2 - Map ResultSet sang list User
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // Chức năng: Thêm user
    // Đầu vào: user (User) - user cần thêm
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Phải hash password trước khi lưu
    public boolean insert(User user) {
        // TODO: Bước 1 - Tạo câu lệnh INSERT user
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Cập nhật user
    // Đầu vào: user (User) - dữ liệu user
    // Đầu ra: boolean - true nếu update thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Cập nhật theo userId
    public boolean update(User user) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE user
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Cập nhật trạng thái user
    // Đầu vào: userId (String) - mã user; isActive (boolean) - trạng thái
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Update cột is_active
    public boolean updateStatus(String userId, boolean isActive) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE is_active
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Cập nhật mật khẩu
    // Đầu vào: userId (String) - mã user; newHashedPassword (String) - mật khẩu đã hash
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Lưu mật khẩu đã hash
    public boolean updatePassword(String userId, String newHashedPassword) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE password
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Xóa user
    // Đầu vào: userId (String) - mã user
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Cần đảm bảo không xóa chính mình
    public boolean delete(String userId) {
        // TODO: Bước 1 - Tạo câu lệnh DELETE user
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Kiểm tra tồn tại username
    // Đầu vào: username (String) - tên đăng nhập
    // Đầu ra: boolean - true nếu tồn tại
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Dùng để kiểm tra trùng username
    public boolean existsByUsername(String username) {
        // TODO: Bước 1 - Query COUNT theo username
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Kiểm tra tồn tại email
    // Đầu vào: email (String) - địa chỉ email
    // Đầu ra: boolean - true nếu tồn tại
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Dùng để kiểm tra trùng email
    public boolean existsByEmail(String email) {
        // TODO: Bước 1 - Query COUNT theo email
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Đếm user theo role
    // Đầu vào: role (Role) - vai trò
    // Đầu ra: int - số lượng user
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Đếm toàn hệ thống
    public int countByRole(Role role) {
        // TODO: Bước 1 - Query COUNT theo role
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }
}
