package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.model.Candidate;

public class CandidateDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng Candidate.
    private Candidate mapRow(ResultSet rs) throws SQLException {
        Candidate candidate = new Candidate();
        candidate.setCandidateId(rs.getString("candidate_id"));
        candidate.setUserId(rs.getString("user_id"));
        return candidate;
    }

    // Chức năng: Tìm candidate theo userId
    // Đầu vào: userId (String) - mã user
    // Đầu ra: Candidate - candidate tìm thấy
    // Tương tác: Được gọi từ AuthService/UserService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public Candidate findByUserId(String userId) {
        // Bước 1 - Query SELECT theo userId
        String sql = "SELECT candidate_id, user_id FROM candidates WHERE user_id = ?";

        // Bước 2 - Map ResultSet sang Candidate
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm candidate theo userId: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Tìm candidate theo id
    // Đầu vào: candidateId (String) - mã candidate
    // Đầu ra: Candidate - candidate tìm thấy
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public Candidate findById(String candidateId) {
        // Bước 1 - Query SELECT theo candidateId
        String sql = "SELECT candidate_id, user_id FROM candidates WHERE candidate_id = ?";

        // Bước 2 - Map ResultSet sang Candidate
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidateId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm candidate theo id: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Thêm candidate
    // Đầu vào: candidate (Candidate) - candidate cần thêm
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ AuthService; sẽ dùng JDBC
    // Ghi chú: Gọi ngay sau khi insert user mới với role CANDIDATE
    public boolean insert(Candidate candidate) {
        // Bước 1 - Tạo câu lệnh INSERT
        String sql = "INSERT INTO candidates (candidate_id, user_id) VALUES (?, ?)";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidate.getCandidateId());
            ps.setString(2, candidate.getUserId());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm candidate: " + e.getMessage(), e);
        }
    }

    // Chức năng: Xóa candidate
    // Đầu vào: candidateId (String) - mã candidate
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ UserService; sẽ dùng JDBC
    // Ghi chú: Nên xóa candidate trước khi xóa user do ràng buộc FK
    public boolean delete(String candidateId) {
        // Bước 1 - Tạo câu lệnh DELETE
        String sql = "DELETE FROM candidates WHERE candidate_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidateId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa candidate: " + e.getMessage(), e);
        }
    }
}
