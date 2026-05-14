package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.enums.Gender;
import org.jobportal.enums.Role;
import org.jobportal.model.User;

public class UserDAO implements IUserDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng User.
    // Dùng chung cho tất cả query SELECT từ bảng users.
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setPhoneNumber(rs.getString("phone_number"));

        // date_of_birth có thể NULL trong DB
        java.sql.Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            user.setDateOfBirth(dob.toLocalDate());
        }

        // gender lưu dạng VARCHAR (MALE/FEMALE/OTHER)
        String genderStr = rs.getString("gender");
        if (genderStr != null) {
            user.setGender(Gender.valueOf(genderStr));
        }

        user.setEmail(rs.getString("email"));
        user.setAddress(rs.getString("address"));

        // role lưu dạng VARCHAR (ADMIN/EMPLOYER/CANDIDATE)
        String roleStr = rs.getString("role");
        if (roleStr != null) {
            user.setRole(Role.valueOf(roleStr));
        }

        user.setActive(rs.getBoolean("is_active"));

        // created_at có thể NULL nếu DB dùng DEFAULT
        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        return user;
    }

    // Chức năng: Tìm user theo username và password
    // Đầu vào: username (String) - tên đăng nhập; password (String) - mật khẩu
    // Đầu ra: User - user tìm thấy
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public User findByUsernameAndPassword(String username, String passwordHash) {
        // Bước 1 - Query SELECT theo username và password đã hash
        String sql = "SELECT user_id, username, password_hash, full_name, phone_number, "
                + "date_of_birth, gender, email, address, role, is_active, created_at "
                + "FROM users WHERE username = ? AND password_hash = ?";

        // Bước 2 - Map ResultSet sang User
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm user theo username/password: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Tìm user theo id
    // Đầu vào: userId (String) - mã user
    // Đầu ra: User - user tìm thấy
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public User findById(String userId) {
        // Bước 1 - Query SELECT theo userId
        String sql = "SELECT user_id, username, password_hash, full_name, phone_number, "
                + "date_of_birth, gender, email, address, role, is_active, created_at "
                + "FROM users WHERE user_id = ?";

        // Bước 2 - Map ResultSet sang User
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm user theo id: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Lấy danh sách user có lọc
    // Đầu vào: roleFilter (Role) - lọc theo role; statusFilter (Boolean) - lọc theo
    // trạng thái
    // Đầu ra: List<User> - danh sách user
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: roleFilter hoặc statusFilter có thể null
    public List<User> findAll(Role roleFilter, Boolean statusFilter) {
        // Bước 1 - Tạo query SELECT với bộ lọc động
        StringBuilder sql = new StringBuilder(
                "SELECT user_id, username, password_hash, full_name, phone_number, "
                        + "date_of_birth, gender, email, address, role, is_active, created_at "
                        + "FROM users WHERE 1=1");

        List<Object> params = new ArrayList<>();

        // Thêm điều kiện lọc role nếu có
        if (roleFilter != null) {
            sql.append(" AND role = ?");
            params.add(roleFilter.name());
        }

        // Thêm điều kiện lọc is_active nếu có
        if (statusFilter != null) {
            sql.append(" AND is_active = ?");
            params.add(statusFilter);
        }

        sql.append(" ORDER BY created_at DESC");

        // Bước 2 - Map ResultSet sang list User
        List<User> result = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách user: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về danh sách
        return result;
    }

    // Chức năng: Thêm user
    // Đầu vào: user (User) - user cần thêm
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Phải hash password trước khi lưu (do BLL thực hiện)
    public boolean insert(User user) {
        // Bước 1 - Tạo câu lệnh INSERT user
        String sql = "INSERT INTO users (user_id, username, password_hash, full_name, phone_number, "
                + "date_of_birth, gender, email, address, role, is_active, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhoneNumber());

            // date_of_birth có thể null
            if (user.getDateOfBirth() != null) {
                ps.setDate(6, java.sql.Date.valueOf(user.getDateOfBirth()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }

            // gender lưu tên enum
            if (user.getGender() != null) {
                ps.setString(7, user.getGender().name());
            } else {
                ps.setNull(7, java.sql.Types.VARCHAR);
            }

            ps.setString(8, user.getEmail());
            ps.setString(9, user.getAddress());
            ps.setString(10, user.getRole().name());
            ps.setBoolean(11, user.isActive());

            // created_at có thể null (DB dùng DEFAULT CURRENT_TIMESTAMP)
            if (user.getCreatedAt() != null) {
                ps.setTimestamp(12, java.sql.Timestamp.valueOf(user.getCreatedAt()));
            } else {
                ps.setNull(12, java.sql.Types.TIMESTAMP);
            }

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm user: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật user
    // Đầu vào: user (User) - dữ liệu user
    // Đầu ra: boolean - true nếu update thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Cập nhật theo userId (không cập nhật password_hash và created_at)
    public boolean update(User user) {
        // Bước 1 - Tạo câu lệnh UPDATE user
        String sql = "UPDATE users SET full_name = ?, phone_number = ?, date_of_birth = ?, "
                + "gender = ?, email = ?, address = ? WHERE user_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhoneNumber());

            if (user.getDateOfBirth() != null) {
                ps.setDate(3, java.sql.Date.valueOf(user.getDateOfBirth()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            if (user.getGender() != null) {
                ps.setString(4, user.getGender().name());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            ps.setString(5, user.getEmail());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getUserId());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật user: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật trạng thái user
    // Đầu vào: userId (String) - mã user; isActive (boolean) - trạng thái
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Update cột is_active
    public boolean updateStatus(String userId, boolean isActive) {
        // Bước 1 - Tạo câu lệnh UPDATE is_active
        String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setString(2, userId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật trạng thái user: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật mật khẩu
    // Đầu vào: userId (String) - mã user; newPassword (String) - mật khẩu
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Lưu mật khẩu đã hash (do BLL thực hiện hash)
    public boolean updatePassword(String userId, String passwordHash) {
        // Bước 1 - Tạo câu lệnh UPDATE password
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, passwordHash);
            ps.setString(2, userId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật mật khẩu: " + e.getMessage(), e);
        }
    }

    // Chức năng: Xóa user
    // Đầu vào: userId (String) - mã user
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Cần đảm bảo không xóa chính mình; xử lý FK bên ngoài (BLL hoặc
    // CASCADE)
    public boolean delete(String userId) {
        // Bước 1 - Tạo câu lệnh DELETE user
        String sql = "DELETE FROM users WHERE user_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa user: " + e.getMessage(), e);
        }
    }

    // Chức năng: Kiểm tra tồn tại username
    // Đầu vào: username (String) - tên đăng nhập
    // Đầu ra: boolean - true nếu tồn tại
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Dùng để kiểm tra trùng username khi đăng ký
    public boolean existsByUsername(String username) {
        // Bước 1 - Query COUNT theo username
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        // Bước 2 - Đọc kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra username: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Kiểm tra tồn tại email
    // Đầu vào: email (String) - địa chỉ email
    // Đầu ra: boolean - true nếu tồn tại
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Dùng để kiểm tra trùng email khi đăng ký
    public boolean existsByEmail(String email) {
        // Bước 1 - Query COUNT theo email
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        // Bước 2 - Đọc kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra email: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Đếm user theo role
    // Đầu vào: role (Role) - vai trò
    // Đầu ra: int - số lượng user
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Đếm toàn hệ thống
    public int countByRole(Role role) {
        // Bước 1 - Query COUNT theo role
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";

        // Bước 2 - Đọc kết quả
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm user theo role: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về số lượng
        return 0;
    }
}

