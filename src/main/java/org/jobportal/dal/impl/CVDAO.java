package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.dal.interfaces.ICVDAO;
import org.jobportal.model.CV;

public class CVDAO implements ICVDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng CV.
    private CV mapRow(ResultSet rs) throws SQLException {
        CV cv = new CV();
        cv.setCvId(rs.getString("cv_id"));
        cv.setCandidateId(rs.getString("candidate_id"));
        cv.setObjective(rs.getString("objective"));
        cv.setSkills(rs.getString("skills"));
        cv.setDesiredPosition(rs.getString("desired_position"));

        // desired_salary có thể NULL
        double desiredSalary = rs.getDouble("desired_salary");
        cv.setDesiredSalary(rs.wasNull() ? null : desiredSalary);

        // last_updated là DATETIME
        java.sql.Timestamp lastUpdated = rs.getTimestamp("last_updated");
        if (lastUpdated != null) {
            cv.setLastUpdated(lastUpdated.toLocalDateTime());
        }

        return cv;
    }

    // Chức năng: Lấy CV theo candidateId
    // Đầu vào: candidateId (String) - mã ứng viên
    // Đầu ra: CV - thông tin CV
    // Tương tác: Được gọi từ CVService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu chưa có CV
    @Override
    public CV findByCandidateId(String candidateId) {
        // Bước 1 - Query SELECT theo candidateId
        String sql = "SELECT cv_id, candidate_id, objective, skills, desired_position, desired_salary, last_updated "
                + "FROM cvs WHERE candidate_id = ?";

        // Bước 2 - Map ResultSet sang CV
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidateId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy CV theo candidateId: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Thêm CV
    // Đầu vào: cv (CV) - dữ liệu CV
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ CVService; sẽ dùng JDBC
    // Ghi chú: Cần insert education nếu có (do BLL/CVService xử lý)
    @Override
    public boolean insert(CV cv) {
        // Bước 1 - Tạo câu lệnh INSERT CV
        String sql = "INSERT INTO cvs (cv_id, candidate_id, objective, skills, desired_position, desired_salary, last_updated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Bước 2 - Thực thi và xử lý education
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cv.getCvId());
            ps.setString(2, cv.getCandidateId());
            ps.setString(3, cv.getObjective());
            ps.setString(4, cv.getSkills());
            ps.setString(5, cv.getDesiredPosition());

            if (cv.getDesiredSalary() != null) {
                ps.setDouble(6, cv.getDesiredSalary());
            } else {
                ps.setNull(6, java.sql.Types.DOUBLE);
            }

            if (cv.getLastUpdated() != null) {
                ps.setTimestamp(7, java.sql.Timestamp.valueOf(cv.getLastUpdated()));
            } else {
                ps.setNull(7, java.sql.Types.TIMESTAMP);
            }

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm CV: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật CV
    // Đầu vào: cv (CV) - dữ liệu CV
    // Đầu ra: boolean - true nếu update thành công
    // Tương tác: Được gọi từ CVService; sẽ dùng JDBC
    // Ghi chú: Có thể áp dụng delete-then-insert cho education (do BLL xử lý)
    @Override
    public boolean update(CV cv) {
        // Bước 1 - Tạo câu lệnh UPDATE CV
        String sql = "UPDATE cvs SET objective = ?, skills = ?, desired_position = ?, "
                + "desired_salary = ?, last_updated = ? WHERE cv_id = ?";

        // Bước 2 - Xử lý danh sách education (do BLL thực hiện trước/sau)
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cv.getObjective());
            ps.setString(2, cv.getSkills());
            ps.setString(3, cv.getDesiredPosition());

            if (cv.getDesiredSalary() != null) {
                ps.setDouble(4, cv.getDesiredSalary());
            } else {
                ps.setNull(4, java.sql.Types.DOUBLE);
            }

            if (cv.getLastUpdated() != null) {
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(cv.getLastUpdated()));
            } else {
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            }

            ps.setString(6, cv.getCvId());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật CV: " + e.getMessage(), e);
        }
    }
}
