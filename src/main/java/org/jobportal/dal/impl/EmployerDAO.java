package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.model.Employer;

public class EmployerDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng Employer.
    private Employer mapRow(ResultSet rs) throws SQLException {
        Employer employer = new Employer();
        employer.setEmployerId(rs.getString("employer_id"));
        employer.setUserId(rs.getString("user_id"));
        employer.setCompanyName(rs.getString("company_name"));
        employer.setCompanyAddress(rs.getString("company_address"));
        employer.setCompanyDescription(rs.getString("company_description"));
        return employer;
    }

    // Chức năng: Tìm employer theo userId
    // Đầu vào: userId (String) - mã user
    // Đầu ra: Employer - employer tìm thấy
    // Tương tác: Được gọi từ UserService/AuthService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public Employer findByUserId(String userId) {
        // Bước 1 - Query SELECT theo userId
        String sql = "SELECT employer_id, user_id, company_name, company_address, company_description "
                + "FROM employers WHERE user_id = ?";

        // Bước 2 - Map ResultSet sang Employer
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm employer theo userId: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Tìm employer theo id
    // Đầu vào: employerId (String) - mã employer
    // Đầu ra: Employer - employer tìm thấy
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public Employer findById(String employerId) {
        // Bước 1 - Query SELECT theo employerId
        String sql = "SELECT employer_id, user_id, company_name, company_address, company_description "
                + "FROM employers WHERE employer_id = ?";

        // Bước 2 - Map ResultSet sang Employer
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm employer theo id: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Thêm employer
    // Đầu vào: employer (Employer) - employer cần thêm
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Gọi ngay sau khi insert user mới với role EMPLOYER
    public boolean insert(Employer employer) {
        // Bước 1 - Tạo câu lệnh INSERT
        String sql = "INSERT INTO employers (employer_id, user_id, company_name, company_address, company_description) "
                + "VALUES (?, ?, ?, ?, ?)";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employer.getEmployerId());
            ps.setString(2, employer.getUserId());
            ps.setString(3, employer.getCompanyName());
            ps.setString(4, employer.getCompanyAddress());
            ps.setString(5, employer.getCompanyDescription());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm employer: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật thông tin công ty
    // Đầu vào: employer (Employer) - employer cần cập nhật
    // Đầu ra: boolean - true nếu update thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Cập nhật company_name, company_address, company_description theo employerId
    public boolean update(Employer employer) {
        // Bước 1 - Tạo câu lệnh UPDATE
        String sql = "UPDATE employers SET company_name = ?, company_address = ?, company_description = ? "
                + "WHERE employer_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employer.getCompanyName());
            ps.setString(2, employer.getCompanyAddress());
            ps.setString(3, employer.getCompanyDescription());
            ps.setString(4, employer.getEmployerId());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật employer: " + e.getMessage(), e);
        }
    }

    // Chức năng: Xóa employer
    // Đầu vào: employerId (String) - mã employer
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Nên xóa employer trước khi xóa user do ràng buộc FK
    public boolean delete(String employerId) {
        // Bước 1 - Tạo câu lệnh DELETE
        String sql = "DELETE FROM employers WHERE employer_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employerId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa employer: " + e.getMessage(), e);
        }
    }
}
