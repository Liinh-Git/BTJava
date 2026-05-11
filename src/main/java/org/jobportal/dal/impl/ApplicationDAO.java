package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.dal.interfaces.IApplicationDAO;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.model.Application;

public class ApplicationDAO implements IApplicationDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng Application.
    private Application mapRow(ResultSet rs) throws SQLException {
        Application application = new Application();
        application.setApplicationId(rs.getString("application_id"));
        application.setCandidateId(rs.getString("candidate_id"));
        application.setRecruitmentId(rs.getString("recruitment_id"));
        application.setStatus(ApplicationStatus.valueOf(rs.getString("status")));
        // applied_date có DEFAULT CURRENT_TIMESTAMP nhưng vẫn cần null-check để tránh NPE
        java.sql.Timestamp appliedTs = rs.getTimestamp("applied_date");
        if (appliedTs != null) {
            application.setAppliedDate(appliedTs.toLocalDateTime());
        }
        return application;
    }

    // Chức năng: Lấy danh sách đơn theo ứng viên
    // Đầu vào: candidateId (String) - mã ứng viên
    // Đầu ra: List<Application> - danh sách đơn
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Lọc theo candidateId
    public List<Application> findByCandidateId(String candidateId) {
        // Bước 1 - Query SELECT theo candidateId
        String sql = "SELECT application_id, candidate_id, recruitment_id, status, applied_date "
                + "FROM applications WHERE candidate_id = ? ORDER BY applied_date DESC";

        List<Application> result = new ArrayList<>();

        // Bước 2 - Map Result Set sang List<Application>
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidateId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy application theo candidateId: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về result
        return result;
    }

    // Chức năng: Lấy danh sách đơn theo tin
    // Đầu vào: recruitmentId (String) - mã tin
    // Đầu ra: List<Application> - danh sách đơn
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Lọc theo recruitmentId
    public List<Application> findByRecruitmentId(String recruitmentId) {
        // Bước 1 - Query SELECT theo recruitmentId
        String sql = "SELECT application_id, candidate_id, recruitment_id, status, applied_date "
                + "FROM applications WHERE recruitment_id = ? ORDER BY applied_date DESC";

        List<Application> result = new ArrayList<>();

        // Bước 2 - Map Result Set sang List<Application>
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recruitmentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy application theo recruitmentId: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về result
        return result;
    }

    /**
     * Lấy danh sách đơn ứng tuyển theo recruitment_id VÀ status cụ thể.
     * Query: SELECT từ applications WHERE recruitment_id = ? AND status = ?
     * Dùng để lọc đơn theo trạng thái trong ApplicationReviewPanel.
     */
    @Override
    public List<Application> findByRecruitmentIdAndStatus(String recruitmentId, ApplicationStatus status) {
        // Bước 1 - Query SELECT theo recruitmentId và status
        String sql = "SELECT application_id, candidate_id, recruitment_id, status, applied_date "
                + "FROM applications WHERE recruitment_id = ? AND status = ? ORDER BY applied_date DESC";

        List<Application> result = new ArrayList<>();

        // Bước 2 - Map Result Set sang List<Application>
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recruitmentId);
            ps.setString(2, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy application theo recruitmentId và status: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về result
        return result;
    }

    /**
     * Kiểm tra candidate đã ứng tuyển vào recruitment chưa.
     * Query: COUNT(*) từ applications WHERE candidate_id = ? AND recruitment_id = ?
     * Dùng để ngăn ứng tuyển trùng lặp.
     */
    @Override
    public boolean existsByCandidateAndRecruitment(String candidateId, String recruitmentId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE candidate_id = ? AND recruitment_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidateId);
            ps.setString(2, recruitmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi kiểm tra application tồn tại: " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Đếm tổng số đơn ứng tuyển của một candidate.
     * Query: COUNT(*) từ applications WHERE candidate_id = ?
     * Dùng cho thống kê tổng đơn của Candidate.
     */
    @Override
    public int countByCandidateId(String candidateId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE candidate_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidateId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm application theo candidateId: " + e.getMessage(), e);
        }
        return 0;
    }

    /**
     * Đếm số đơn ứng tuyển của candidate theo trạng thái cụ thể.
     * Query: COUNT(*) từ applications WHERE candidate_id = ? AND status = ?
     * Dùng để tính tỷ lệ thành công (số APPROVED / tổng).
     */
    @Override
    public int countByCandidateIdAndStatus(String candidateId, ApplicationStatus status) {
        String sql = "SELECT COUNT(*) FROM applications WHERE candidate_id = ? AND status = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, candidateId);
            ps.setString(2, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm application theo candidateId và status: " + e.getMessage(), e);
        }
        return 0;
    }

    /**
     * Đếm tổng số đơn ứng tuyển liên quan đến tất cả tin của một employer.
     * JOIN applications → recruitments → employer_id để đếm.
     * Dùng cho Employer Dashboard thống kê tổng đơn nhận được.
     */
    @Override
    public int countByEmployerId(String employerId) {
        String sql = "SELECT COUNT(*) FROM applications a "
                + "INNER JOIN recruitments r ON a.recruitment_id = r.recruitment_id "
                + "WHERE r.employer_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm application theo employerId: " + e.getMessage(), e);
        }
        return 0;
    }

    /**
     * Đếm số ứng viên mới nộp đơn trong ngày hôm nay cho employer.
     * JOIN applications → recruitments → employer_id, lọc applied_date = ngày hiện
     * tại.
     * Dùng cho Employer Dashboard hiển thị "Hôm nay có X ứng viên mới".
     */
    @Override
    public int countNewApplicantsToday(String employerId) {
        String sql = "SELECT COUNT(*) FROM applications a "
                + "INNER JOIN recruitments r ON a.recruitment_id = r.recruitment_id "
                + "WHERE r.employer_id = ? "
                + "AND a.applied_date >= CURDATE() "
                + "AND a.applied_date < DATE_ADD(CURDATE(), INTERVAL 1 DAY)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm ứng viên mới hôm nay: " + e.getMessage(), e);
        }
        return 0;
    }

    /**
     * Thêm đơn ứng tuyển mới vào bảng applications.
     * INSERT (application_id, candidate_id, recruitment_id, status, applied_date).
     * BLL phải sinh application_id và set applied_date = now() trước khi gọi.
     */
    @Override
    public boolean insert(Application application) {
        String sql = "INSERT INTO applications (application_id, candidate_id, recruitment_id, status, applied_date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, application.getApplicationId());
            ps.setString(2, application.getCandidateId());
            ps.setString(3, application.getRecruitmentId());
            ps.setString(4, application.getStatus() != null
                    ? application.getStatus().name()
                    : ApplicationStatus.PENDING.name());

            if (application.getAppliedDate() != null) {
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(application.getAppliedDate()));
            } else {
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm application: " + e.getMessage(), e);
        }
    }

    /**
     * Xóa đơn ứng tuyển theo application_id (hard delete).
     * BLL phải kiểm tra status = PENDING trước khi cho phép xóa.
     */
    @Override
    public boolean delete(String applicationId) {
        String sql = "DELETE FROM applications WHERE application_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, applicationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa application: " + e.getMessage(), e);
        }
    }

    /**
     * Cập nhật trạng thái đơn ứng tuyển theo application_id.
     * UPDATE cột status (PENDING/APPROVED/REJECTED) WHERE application_id = ?
     * Dùng khi Employer approve/reject, hoặc Candidate hủy đơn (REJECTED không cần
     * thiết nếu dùng delete).
     */
    @Override
    public boolean updateStatus(String applicationId, ApplicationStatus status) {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, applicationId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật status application: " + e.getMessage(), e);
        }
    }
}
