package org.jobportal.dal.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.model.Recruitment;

public class RecruitmentDAO {
    // Chức năng: Tìm tin tuyển dụng theo id
    // Đầu vào: recruitmentId (String) - mã tin
    // Đầu ra: Recruitment - tin tuyển dụng
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Trả về null nếu không tìm thấy
    public Recruitment findById(String recruitmentId) {
        // TODO: Bước 1 - Query SELECT theo recruitmentId
        // TODO: Bước 2 - Map ResultSet sang Recruitment
        // TODO: Bước 3 - Trả về kết quả
        return null;
    }

    // Chức năng: Lấy danh sách tin theo employer
    // Đầu vào: employerId (String) - mã employer
    // Đầu ra: List<Recruitment> - danh sách tin
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Lọc theo employerId
    public List<Recruitment> findByEmployerId(String employerId) {
        // TODO: Bước 1 - Query SELECT theo employerId
        // TODO: Bước 2 - Map ResultSet sang list Recruitment
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // Chức năng: Lấy danh sách tin theo admin_status
    // Đầu vào: adminStatus (String) - trạng thái duyệt
    // Đầu ra: List<Recruitment> - danh sách tin
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Lọc theo admin_status
    public List<Recruitment> findByAdminStatus(String adminStatus) {
        // TODO: Bước 1 - Query SELECT theo adminStatus
        // TODO: Bước 2 - Map ResultSet sang list Recruitment
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // Chức năng: Tìm kiếm tin theo keyword và category
    // Đầu vào: keyword (String) - từ khóa; categoryId (String) - danh mục; offset (int) - vị trí; limit (int) - giới hạn
    // Đầu ra: List<Recruitment> - danh sách tin
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Chỉ lấy tin có status=OPEN và admin_status=APPROVED
    public List<Recruitment> searchByKeywordAndCategory(String keyword, String categoryId, int offset, int limit) {
        // TODO: Bước 1 - Xây dựng câu lệnh tìm kiếm
        // TODO: Bước 2 - Map ResultSet sang list Recruitment
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // Chức năng: Thêm tin tuyển dụng
    // Đầu vào: recruitment (Recruitment) - đối tượng tin
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Set các trường mặc định trước khi insert
    public boolean insert(Recruitment recruitment) {
        // TODO: Bước 1 - Tạo câu lệnh INSERT
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Cập nhật tin tuyển dụng
    // Đầu vào: recruitment (Recruitment) - đối tượng tin
    // Đầu ra: boolean - true nếu update thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Cập nhật theo recruitmentId
    public boolean update(Recruitment recruitment) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Cập nhật status của tin
    // Đầu vào: recruitmentId (String) - mã tin; status (RecruitmentStatus) - trạng thái
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Update cột status
    public boolean updateStatus(String recruitmentId, RecruitmentStatus status) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE status
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Cập nhật admin_status của tin
    // Đầu vào: recruitmentId (String) - mã tin; adminStatus (String) - trạng thái duyệt
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Update cột admin_status
    public boolean updateAdminStatus(String recruitmentId, String adminStatus) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE admin_status
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Xóa tin tuyển dụng
    // Đầu vào: recruitmentId (String) - mã tin
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Cần kiểm tra ràng buộc trước khi xóa
    public boolean delete(String recruitmentId) {
        // TODO: Bước 1 - Tạo câu lệnh DELETE
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Đếm số tin theo category
    // Đầu vào: categoryId (String) - mã danh mục
    // Đầu ra: int - số tin
    // Tương tác: Được gọi từ CategoryService; sẽ dùng JDBC
    // Ghi chú: Dùng để kiểm tra ràng buộc xóa danh mục
    public int countByCategory(String categoryId) {
        // TODO: Bước 1 - Query COUNT theo categoryId
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }

    // Chuc nang: Dem tong so tin OPEN
    // Dau vao: (void)
    // Dau ra: int - so tin OPEN
    // Tuong tac: Duoc goi tu RecruitmentService; se dung JDBC
    // Ghi chu: Dem tren toan he thong
    public int countOpenRecruitments() {
        // TODO: Bước 1 - Query COUNT theo status OPEN
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }

    // Chức năng: Lấy danh sách top employer
    // Đầu vào: (void)
    // Đầu ra: List<Map<String, Object>> - danh sách thống kê
    // Tương tác: Được gọi từ RecruitmentService; sẽ dùng JDBC
    // Ghi chú: Có thể cần group by employer
    public List<Map<String, Object>> getTopEmployers() {
        // TODO: Bước 1 - Query thống kê top employer
        // TODO: Bước 2 - Map kết quả sang danh sách Map
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }
}
