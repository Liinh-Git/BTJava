package org.jobportal.bll.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.JobType;

public interface IRecruitmentService {
    // Chuc nang: Dang tin tuyen dung moi
    // Dau vao: title (String), categoryId (String), jobType (JobType), salary (double), dueDate (LocalDate), description (String), location (String)
    // Dau ra: boolean
    boolean postRecruitment(String title, String categoryId, JobType jobType, double salary, LocalDate dueDate, String description, String location);

    // Chuc nang: Cap nhat tin tuyen dung
    // Dau vao: recruitmentId (String), title (String), categoryId (String), jobType (JobType), salary (double), dueDate (LocalDate), description (String), location (String)
    // Dau ra: boolean
    boolean updateRecruitment(String recruitmentId, String title, String categoryId, JobType jobType, double salary, LocalDate dueDate, String description, String location);

    // Chuc nang: Xoa tin tuyen dung
    // Dau vao: recruitmentId (String)
    // Dau ra: boolean
    boolean deleteRecruitment(String recruitmentId);

    // Chuc nang: Dong tin tuyen dung
    // Dau vao: recruitmentId (String)
    // Dau ra: boolean
    boolean closeRecruitment(String recruitmentId);

    // Chuc nang: Lay chi tiet tin tuyen dung
    // Dau vao: recruitmentId (String)
    // Dau ra: RecruitmentDTO
    RecruitmentDTO getRecruitmentById(String recruitmentId);

    // Chuc nang: Tim kiem tin tuyen dung
    // Dau vao: keyword (String), categoryId (String), page (int), pageSize (int)
    // Dau ra: List<RecruitmentDTO>
    List<RecruitmentDTO> searchRecruitments(String keyword, String categoryId, int page, int pageSize);

    // Chuc nang: Lay danh sach tin theo employer
    // Dau vao: employerId (String)
    // Dau ra: List<RecruitmentDTO>
    List<RecruitmentDTO> getRecruitmentsByEmployer(String employerId);

    // Chuc nang: Lay danh sach tin dang cho duyet
    // Dau vao: (void)
    // Dau ra: List<RecruitmentDTO>
    List<RecruitmentDTO> getPendingRecruitments();

    // Chuc nang: Admin duyet hoac tu choi tin
    // Dau vao: recruitmentId (String), decision (String)
    // Dau ra: boolean
    boolean adminModerate(String recruitmentId, String decision);

    // Chuc nang: Dem tong so tin dang OPEN
    // Dau vao: (void)
    // Dau ra: int
    int countOpenRecruitments();

    // Chuc nang: Lay danh sach top employer
    // Dau vao: (void)
    // Dau ra: List<Map<String, Object>>
    List<Map<String, Object>> getTopEmployers();
}
