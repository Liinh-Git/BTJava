package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.dal.interfaces.IRecruitmentDAO;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.JobType;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.model.Recruitment;

public class RecruitmentDAO implements IRecruitmentDAO {

    // Ánh xạ một hàng ResultSet sang đối tượng Recruitment.
    // Dùng chung cho các query SELECT từ bảng recruitments.
    private Recruitment mapRow(ResultSet rs) throws SQLException {
        Recruitment r = new Recruitment();
        r.setRecruitmentId(rs.getString("recruitment_id"));
        r.setEmployerId(rs.getString("employer_id"));
        r.setCategoryId(rs.getString("category_id"));
        r.setTitle(rs.getString("title"));
        r.setDescription(rs.getString("description"));

        // job_type lưu dạng VARCHAR (FULLTIME/PARTTIME/INTERNSHIP)
        String jobTypeStr = rs.getString("job_type");
        if (jobTypeStr != null) {
            r.setJobType(JobType.valueOf(jobTypeStr));
        }

        // status lưu dạng VARCHAR (OPEN/CLOSED/EXPIRED)
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            r.setStatus(RecruitmentStatus.valueOf(statusStr));
        }

        // admin_status lưu dạng VARCHAR (PENDING/APPROVED/REJECTED)
        String adminStatusStr = rs.getString("admin_status");
        if (adminStatusStr != null) {
            r.setAdminStatus(AdminStatus.valueOf(adminStatusStr));
        }

        // salary có thể NULL trong DB
        double salary = rs.getDouble("salary");
        r.setSalary(rs.wasNull() ? null : salary);

        r.setLocation(rs.getString("location"));
        r.setExperienceRequired(rs.getString("experience_required"));

        // created_date và due_date là DATETIME
        java.sql.Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            r.setCreatedDate(createdDate.toLocalDateTime());
        }

        java.sql.Timestamp dueDate = rs.getTimestamp("due_date");
        if (dueDate != null) {
            r.setDueDate(dueDate.toLocalDateTime());
        }

        return r;
    }

    // Chức năng: Tìm tin tuyển dụng theo id
    // Đầu vào: recruitmentId (String) - mã tin
    // Đầu ra: Recruitment - tin tuyển dụng
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    @Override
    public Recruitment findById(String recruitmentId) {
        // Bước 1 - Query SELECT theo recruitmentId
        String sql = "SELECT recruitment_id, employer_id, category_id, title, description, "
                + "job_type, status, admin_status, salary, location, experience_required, "
                + "created_date, due_date FROM recruitments WHERE recruitment_id = ?";

        // Bước 2 - Map ResultSet sang Recruitment
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recruitmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm recruitment theo id: " + e.getMessage(), e);
        }
        // Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Lấy danh sách tin theo employer
    // Đầu vào: employerId (String) - mã employer
    // Đầu ra: List<Recruitment> - danh sách tin
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Lọc theo employerId, sắp xếp mới nhất trước
    @Override
    public List<Recruitment> findByEmployerId(String employerId) {
        // Bước 1 - Query SELECT theo employerId
        String sql = "SELECT recruitment_id, employer_id, category_id, title, description, "
                + "job_type, status, admin_status, salary, location, experience_required, "
                + "created_date, due_date FROM recruitments WHERE employer_id = ? "
                + "ORDER BY created_date DESC";

        List<Recruitment> result = new ArrayList<>();

        // Bước 2 - Map ResultSet sang list Recruitment
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy recruitment theo employerId: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về danh sách
        return result;
    }

    // Chức năng: Lấy danh sách tin theo admin_status
    // Đầu vào: adminStatus (String) - trạng thái duyệt
    // Đầu ra: List<Recruitment> - danh sách tin
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Lọc theo admin_status (PENDING/APPROVED/REJECTED)
    @Override
    public List<Recruitment> findByAdminStatus(String adminStatus) {
        // Bước 1 - Query SELECT theo adminStatus
        String sql = "SELECT recruitment_id, employer_id, category_id, title, description, "
                + "job_type, status, admin_status, salary, location, experience_required, "
                + "created_date, due_date FROM recruitments WHERE admin_status = ? "
                + "ORDER BY created_date DESC";

        List<Recruitment> result = new ArrayList<>();

        // Bước 2 - Map ResultSet sang list Recruitment
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, adminStatus);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy recruitment theo adminStatus: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về danh sách
        return result;
    }

    // Chức năng: Tìm kiếm tin theo keyword và category
    // Đầu vào: keyword (String) - từ khóa; categoryId (String) - danh mục; offset (int) - vị trí; limit (int) - giới hạn
    // Đầu ra: List<Recruitment> - danh sách tin
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Chỉ lấy tin có status=OPEN và admin_status=APPROVED
    @Override
    public List<Recruitment> searchByKeywordAndCategory(String keyword, String categoryId, int offset, int limit) {
        // Bước 1 - Xây dựng câu lệnh tìm kiếm (status=OPEN, admin_status=APPROVED, lọc keyword và category)
        StringBuilder sql = new StringBuilder(
                "SELECT r.recruitment_id, r.employer_id, r.category_id, r.title, r.description, "
                + "r.job_type, r.status, r.admin_status, r.salary, r.location, r.experience_required, "
                + "r.created_date, r.due_date "
                + "FROM recruitments r "
                + "WHERE r.status = 'OPEN' AND r.admin_status = 'APPROVED'");

        List<Object> params = new ArrayList<>();

        // Lọc theo keyword trên cột title
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND r.title LIKE ?");
            params.add("%" + keyword.trim() + "%");
        }

        // Lọc theo category_id nếu có
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            sql.append(" AND r.category_id = ?");
            params.add(categoryId.trim());
        }

        sql.append(" ORDER BY r.created_date DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<Recruitment> result = new ArrayList<>();

        // Bước 2 - Map ResultSet sang list Recruitment
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
            throw new RuntimeException("Lỗi khi tìm kiếm recruitment: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về danh sách
        return result;
    }

    // Chức năng: Thêm tin tuyển dụng
    // Đầu vào: recruitment (Recruitment) - đối tượng tin
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Set các trường mặc định trước khi insert (status=OPEN, admin_status=PENDING do BLL)
    @Override
    public boolean insert(Recruitment recruitment) {
        // Bước 1 - Tạo câu lệnh INSERT
        String sql = "INSERT INTO recruitments (recruitment_id, employer_id, category_id, title, description, "
                + "job_type, status, admin_status, salary, location, experience_required, created_date, due_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recruitment.getRecruitmentId());
            ps.setString(2, recruitment.getEmployerId());
            ps.setString(3, recruitment.getCategoryId());
            ps.setString(4, recruitment.getTitle());
            ps.setString(5, recruitment.getDescription());
            ps.setString(6, recruitment.getJobType() != null ? recruitment.getJobType().name() : null);
            ps.setString(7, recruitment.getStatus() != null ? recruitment.getStatus().name() : RecruitmentStatus.OPEN.name());
            ps.setString(8, recruitment.getAdminStatus() != null ? recruitment.getAdminStatus().name() : AdminStatus.PENDING.name());

            if (recruitment.getSalary() != null) {
                ps.setDouble(9, recruitment.getSalary());
            } else {
                ps.setNull(9, java.sql.Types.DOUBLE);
            }

            ps.setString(10, recruitment.getLocation());
            ps.setString(11, recruitment.getExperienceRequired());

            if (recruitment.getCreatedDate() != null) {
                ps.setTimestamp(12, java.sql.Timestamp.valueOf(recruitment.getCreatedDate()));
            } else {
                ps.setNull(12, java.sql.Types.TIMESTAMP);
            }

            if (recruitment.getDueDate() != null) {
                ps.setTimestamp(13, java.sql.Timestamp.valueOf(recruitment.getDueDate()));
            } else {
                ps.setNull(13, java.sql.Types.TIMESTAMP);
            }

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm recruitment: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật tin tuyển dụng
    // Đầu vào: recruitment (Recruitment) - đối tượng tin
    // Đầu ra: boolean - true nếu update thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Cập nhật theo recruitmentId (không đổi employer_id, created_date)
    @Override
    public boolean update(Recruitment recruitment) {
        // Bước 1 - Tạo câu lệnh UPDATE
        String sql = "UPDATE recruitments SET category_id = ?, title = ?, description = ?, "
                + "job_type = ?, status = ?, admin_status = ?, salary = ?, location = ?, "
                + "experience_required = ?, due_date = ? WHERE recruitment_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recruitment.getCategoryId());
            ps.setString(2, recruitment.getTitle());
            ps.setString(3, recruitment.getDescription());
            ps.setString(4, recruitment.getJobType() != null ? recruitment.getJobType().name() : null);
            ps.setString(5, recruitment.getStatus() != null ? recruitment.getStatus().name() : null);
            ps.setString(6, recruitment.getAdminStatus() != null ? recruitment.getAdminStatus().name() : null);

            if (recruitment.getSalary() != null) {
                ps.setDouble(7, recruitment.getSalary());
            } else {
                ps.setNull(7, java.sql.Types.DOUBLE);
            }

            ps.setString(8, recruitment.getLocation());
            ps.setString(9, recruitment.getExperienceRequired());

            if (recruitment.getDueDate() != null) {
                ps.setTimestamp(10, java.sql.Timestamp.valueOf(recruitment.getDueDate()));
            } else {
                ps.setNull(10, java.sql.Types.TIMESTAMP);
            }

            ps.setString(11, recruitment.getRecruitmentId());

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật recruitment: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật status của tin
    // Đầu vào: recruitmentId (String) - mã tin; status (RecruitmentStatus) - trạng thái
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Update cột status (OPEN/CLOSED/EXPIRED)
    @Override
    public boolean updateStatus(String recruitmentId, RecruitmentStatus status) {
        // Bước 1 - Tạo câu lệnh UPDATE status
        String sql = "UPDATE recruitments SET status = ? WHERE recruitment_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setString(2, recruitmentId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật status recruitment: " + e.getMessage(), e);
        }
    }

    // Chức năng: Cập nhật admin_status của tin
    // Đầu vào: recruitmentId (String) - mã tin; adminStatus (String) - trạng thái duyệt
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Update cột admin_status (PENDING/APPROVED/REJECTED)
    @Override
    public boolean updateAdminStatus(String recruitmentId, String adminStatus) {
        // Bước 1 - Tạo câu lệnh UPDATE admin_status
        String sql = "UPDATE recruitments SET admin_status = ? WHERE recruitment_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, adminStatus);
            ps.setString(2, recruitmentId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật adminStatus recruitment: " + e.getMessage(), e);
        }
    }

    // Chức năng: Xóa tin tuyển dụng
    // Đầu vào: recruitmentId (String) - mã tin
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Cần kiểm tra ràng buộc trước khi xóa (BLL kiểm tra)
    @Override
    public boolean delete(String recruitmentId) {
        // Bước 1 - Tạo câu lệnh DELETE
        String sql = "DELETE FROM recruitments WHERE recruitment_id = ?";

        // Bước 2 - Thực thi và lấy kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, recruitmentId);

            // Bước 3 - Trả về boolean
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa recruitment: " + e.getMessage(), e);
        }
    }

    // Chức năng: Đếm số tin theo category
    // Đầu vào: categoryId (String) - mã danh mục
    // Đầu ra: int - số tin
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Dùng để kiểm tra ràng buộc xóa danh mục
    @Override
    public int countByCategory(String categoryId) {
        // Bước 1 - Query COUNT theo categoryId
        String sql = "SELECT COUNT(*) FROM recruitments WHERE category_id = ?";

        // Bước 2 - Đọc kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Bước 3 - Trả về số lượng
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm recruitment theo category: " + e.getMessage(), e);
        }
        return 0;
    }

    // Chức năng: Đếm tổng số tin OPEN
    // Đầu vào: (void)
    // Đầu ra: int - số tin OPEN
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Đếm trên toàn hệ thống
    @Override
    public int countOpenRecruitments() {
        // Bước 1 - Query COUNT theo status OPEN
        String sql = "SELECT COUNT(*) FROM recruitments WHERE status = 'OPEN'";

        // Bước 2 - Đọc kết quả
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                // Bước 3 - Trả về số lượng
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm recruitment OPEN: " + e.getMessage(), e);
        }
        return 0;
    }

    // Chức năng: Lấy danh sách top employer
    // Đầu vào: (void)
    // Đầu ra: List<Map<String, Object>> - danh sách thống kê
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: GROUP BY employer_id, sắp xếp theo số tin giảm dần, lấy tối đa 5
    @Override
    public List<Map<String, Object>> getTopEmployers() {
        // Bước 1 - Query thống kê top employer (JOIN employers + recruitments, GROUP BY, ORDER BY COUNT DESC)
        String sql = "SELECT e.employer_id, e.company_name, COUNT(r.recruitment_id) AS recruitment_count "
                + "FROM employers e "
                + "LEFT JOIN recruitments r ON r.employer_id = e.employer_id "
                + "GROUP BY e.employer_id, e.company_name "
                + "ORDER BY recruitment_count DESC "
                + "LIMIT 5";

        List<Map<String, Object>> result = new ArrayList<>();

        // Bước 2 - Map kết quả sang danh sách Map
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("employer_id", rs.getString("employer_id"));
                row.put("company_name", rs.getString("company_name"));
                row.put("recruitment_count", rs.getInt("recruitment_count"));
                result.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy top employers: " + e.getMessage(), e);
        }

        // Bước 3 - Trả về danh sách
        return result;
    }
}
