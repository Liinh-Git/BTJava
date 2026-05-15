package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.dal.interfaces.ICVDAO;
import org.jobportal.model.CV;
import org.jobportal.model.Education;

public class CVDAO implements ICVDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng CV.
    private CV mapRow(ResultSet rs) throws SQLException {
        CV cv = new CV();
        cv.setCvId(rs.getString("cv_id"));
        cv.setCandidateId(rs.getString("candidate_id"));
        cv.setObjective(rs.getString("objective"));
        cv.setSkills(rs.getString("skills"));
        cv.setDesiredPosition(rs.getString("desired_position"));
        cv.setLocation(rs.getString("location"));

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
        ensureLocationColumn();
        // Bước 1 - Query SELECT theo candidateId
        String sql = "SELECT cv_id, candidate_id, objective, skills, desired_position, location, desired_salary, last_updated "
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
        ensureLocationColumn();
        // Bước 1 - Tạo câu lệnh INSERT CV
        String sql = "INSERT INTO cvs (cv_id, candidate_id, objective, skills, desired_position, location, desired_salary, last_updated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Bước 2 - Thực thi và xử lý education
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cv.getCvId());
            ps.setString(2, cv.getCandidateId());
            ps.setString(3, cv.getObjective());
            ps.setString(4, cv.getSkills());
            ps.setString(5, cv.getDesiredPosition());
            ps.setString(6, cv.getLocation());

            if (cv.getDesiredSalary() != null) {
                ps.setDouble(7, cv.getDesiredSalary());
            } else {
                ps.setNull(7, java.sql.Types.DOUBLE);
            }

            if (cv.getLastUpdated() != null) {
                ps.setTimestamp(8, java.sql.Timestamp.valueOf(cv.getLastUpdated()));
            } else {
                ps.setNull(8, java.sql.Types.TIMESTAMP);
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
        ensureLocationColumn();
        // Bước 1 - Tạo câu lệnh UPDATE CV
        String sql = "UPDATE cvs SET objective = ?, skills = ?, desired_position = ?, "
                + "location = ?, desired_salary = ?, last_updated = ? WHERE cv_id = ?";

        // Bước 2 - Xử lý danh sách education (do BLL thực hiện trước/sau)
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cv.getObjective());
            ps.setString(2, cv.getSkills());
            ps.setString(3, cv.getDesiredPosition());
            ps.setString(4, cv.getLocation());

            if (cv.getDesiredSalary() != null) {
                ps.setDouble(5, cv.getDesiredSalary());
            } else {
                ps.setNull(5, java.sql.Types.DOUBLE);
            }

            if (cv.getLastUpdated() != null) {
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(cv.getLastUpdated()));
            } else {
                ps.setNull(6, java.sql.Types.TIMESTAMP);
            }

            ps.setString(7, cv.getCvId());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật CV: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Education> findEducationsByCvId(String cvId) {
        String sql = "SELECT education_id, cv_id, school, degree, major, start_year, end_year, description "
                + "FROM educations WHERE cv_id = ? ORDER BY end_year DESC, start_year DESC";
        List<Education> result = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cvId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Education education = new Education();
                    education.setEducationId(rs.getString("education_id"));
                    education.setCvId(rs.getString("cv_id"));
                    education.setSchool(rs.getString("school"));
                    education.setDegree(rs.getString("degree"));
                    education.setMajor(rs.getString("major"));

                    int startYear = rs.getInt("start_year");
                    education.setStartYear(rs.wasNull() ? null : startYear);

                    int endYear = rs.getInt("end_year");
                    education.setEndYear(rs.wasNull() ? null : endYear);

                    education.setDescription(rs.getString("description"));
                    result.add(education);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi lay education theo cvId: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean replaceEducations(String cvId, List<Education> educations) {
        String deleteSql = "DELETE FROM educations WHERE cv_id = ?";
        String insertSql = "INSERT INTO educations "
                + "(education_id, cv_id, school, degree, major, start_year, end_year, description) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                deletePs.setString(1, cvId);
                deletePs.executeUpdate();
            }

            if (educations != null && !educations.isEmpty()) {
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    int index = 0;
                    for (Education education : educations) {
                        String educationId = education.getEducationId();
                        if (educationId == null || educationId.isBlank()) {
                            educationId = generateEducationId(index++);
                        }
                        education.setEducationId(educationId);
                        education.setCvId(cvId);

                        insertPs.setString(1, educationId);
                        insertPs.setString(2, cvId);
                        insertPs.setString(3, education.getSchool());
                        insertPs.setString(4, education.getDegree());
                        insertPs.setString(5, education.getMajor());
                        if (education.getStartYear() != null) {
                            insertPs.setInt(6, education.getStartYear());
                        } else {
                            insertPs.setNull(6, java.sql.Types.INTEGER);
                        }
                        if (education.getEndYear() != null) {
                            insertPs.setInt(7, education.getEndYear());
                        } else {
                            insertPs.setNull(7, java.sql.Types.INTEGER);
                        }
                        insertPs.setString(8, education.getDescription());
                        insertPs.addBatch();
                    }
                    insertPs.executeBatch();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi luu education cua CV: " + e.getMessage(), e);
        }
    }

    private String generateEducationId(int index) {
        long seed = (System.currentTimeMillis() + index) % 1_000_000L;
        return String.format("EDU-%06d", seed);
    }

    private void ensureLocationColumn() {
        String sql = "ALTER TABLE cvs ADD COLUMN location VARCHAR(200) AFTER desired_position";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (!message.contains("duplicate")) {
                throw new RuntimeException("Loi khi kiem tra cot location cua CV: " + e.getMessage(), e);
            }
        }
    }
}
