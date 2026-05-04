package org.jobportal.dal.interfaces;

import java.util.List;
import java.util.Map;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.model.Recruitment;

public interface IRecruitmentDAO {
    // Chuc nang: Tim tin tuyen dung theo id
    // Dau vao: recruitmentId (String)
    // Dau ra: Recruitment
    Recruitment findById(String recruitmentId);

    // Chuc nang: Lay danh sach tin theo employer
    // Dau vao: employerId (String)
    // Dau ra: List<Recruitment>
    List<Recruitment> findByEmployerId(String employerId);

    // Chuc nang: Lay danh sach tin theo admin_status
    // Dau vao: adminStatus (String)
    // Dau ra: List<Recruitment>
    List<Recruitment> findByAdminStatus(String adminStatus);

    // Chuc nang: Tim kiem tin theo keyword va category
    // Dau vao: keyword (String), categoryId (String), offset (int), limit (int)
    // Dau ra: List<Recruitment>
    List<Recruitment> searchByKeywordAndCategory(String keyword, String categoryId, int offset, int limit);

    // Chuc nang: Them tin tuyen dung
    // Dau vao: recruitment (Recruitment)
    // Dau ra: boolean
    boolean insert(Recruitment recruitment);

    // Chuc nang: Cap nhat tin tuyen dung
    // Dau vao: recruitment (Recruitment)
    // Dau ra: boolean
    boolean update(Recruitment recruitment);

    // Chuc nang: Cap nhat status cua tin
    // Dau vao: recruitmentId (String), status (RecruitmentStatus)
    // Dau ra: boolean
    boolean updateStatus(String recruitmentId, RecruitmentStatus status);

    // Chuc nang: Cap nhat admin_status cua tin
    // Dau vao: recruitmentId (String), adminStatus (String)
    // Dau ra: boolean
    boolean updateAdminStatus(String recruitmentId, String adminStatus);

    // Chuc nang: Xoa tin tuyen dung
    // Dau vao: recruitmentId (String)
    // Dau ra: boolean
    boolean delete(String recruitmentId);

    // Chuc nang: Dem so tin theo category
    // Dau vao: categoryId (String)
    // Dau ra: int
    int countByCategory(String categoryId);

    // Chuc nang: Dem tong so tin OPEN
    // Dau vao: (void)
    // Dau ra: int
    int countOpenRecruitments();

    // Chuc nang: Lay danh sach top employer
    // Dau vao: (void)
    // Dau ra: List<Map<String, Object>>
    List<Map<String, Object>> getTopEmployers();
}
