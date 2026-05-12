package org.jobportal.dal.interfaces;

import java.util.List;
import java.util.Map;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.model.Recruitment;

public interface IRecruitmentDAO {
    // Chức năng: Tìm tin tuyển dụng theo id
    // Đầu vào: recruitmentId (String)
    // Đầu ra: Recruitment
    Recruitment findById(String recruitmentId);

    // Chức năng: Lấy danh sách tin theo employer
    // Đầu vào: employerId (String)
    // Đầu ra: List<Recruitment>
    List<Recruitment> findByEmployerId(String employerId);

    // Chức năng: Lấy danh sách tin theo admin_status
    // Đầu vào: adminStatus (String)
    // Đầu ra: List<Recruitment>
    List<Recruitment> findByAdminStatus(String adminStatus);

    // Chức năng: Tìm kiếm tin theo keyword và category
    // Đầu vào: keyword (String), categoryId (String), offset (int), limit (int)
    // Đầu ra: List<Recruitment>
    List<Recruitment> searchByKeywordAndCategory(String keyword, String categoryId, int offset, int limit);

    // Chức năng: Thêm tin tuyển dụng
    // Đầu vào: recruitment (Recruitment)
    // Đầu ra: boolean
    boolean insert(Recruitment recruitment);

    // Chức năng: Cập nhật tin tuyển dụng
    // Đầu vào: recruitment (Recruitment)
    // Đầu ra: boolean
    boolean update(Recruitment recruitment);

    // Chức năng: Cập nhật status của tin
    // Đầu vào: recruitmentId (String), status (RecruitmentStatus)
    // Đầu ra: boolean
    boolean updateStatus(String recruitmentId, RecruitmentStatus status);

    // Chức năng: Cập nhật admin_status của tin
    // Đầu vào: recruitmentId (String), adminStatus (String)
    // Đầu ra: boolean
    boolean updateAdminStatus(String recruitmentId, String adminStatus);

    // Chức năng: Xóa tin tuyển dụng
    // Đầu vào: recruitmentId (String)
    // Đầu ra: boolean
    boolean delete(String recruitmentId);

    // Chức năng: Đếm số tin theo category
    // Đầu vào: categoryId (String)
    // Đầu ra: int
    int countByCategory(String categoryId);

    // Chức năng: Đếm tổng số tin OPEN
    // Đầu vào: (void)
    // Đầu ra: int
    int countOpenRecruitments();

    // Chức năng: Lấy danh sách top employer
    // Đầu vào: (void)
    // Đầu ra: List<Map<String, Object>>
    List<Map<String, Object>> getTopEmployers();
}
