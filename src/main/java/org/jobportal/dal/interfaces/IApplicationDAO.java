package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.model.Application;

public interface IApplicationDAO {
    // Chuc nang: Lay danh sach don theo ung vien
    // Dau vao: candidateId (String)
    // Dau ra: List<Application>
    List<Application> findByCandidateId(String candidateId);

    // Chuc nang: Lay danh sach don theo tin
    // Dau vao: recruitmentId (String)
    // Dau ra: List<Application>
    List<Application> findByRecruitmentId(String recruitmentId);

    // Chuc nang: Lay danh sach don theo tin va trang thai
    // Dau vao: recruitmentId (String), status (ApplicationStatus)
    // Dau ra: List<Application>
    List<Application> findByRecruitmentIdAndStatus(String recruitmentId, ApplicationStatus status);

    // Chuc nang: Kiem tra ung vien da ung tuyen chua
    // Dau vao: candidateId (String), recruitmentId (String)
    // Dau ra: boolean
    boolean existsByCandidateAndRecruitment(String candidateId, String recruitmentId);

    // Chuc nang: Dem so don theo ung vien
    // Dau vao: candidateId (String)
    // Dau ra: int
    int countByCandidateId(String candidateId);

    // Chuc nang: Dem so don theo ung vien va trang thai
    // Dau vao: candidateId (String), status (ApplicationStatus)
    // Dau ra: int
    int countByCandidateIdAndStatus(String candidateId, ApplicationStatus status);

    // Chuc nang: Dem so don theo employer
    // Dau vao: employerId (String)
    // Dau ra: int
    int countByEmployerId(String employerId);

    // Chuc nang: Dem ung vien moi trong hom nay
    // Dau vao: employerId (String)
    // Dau ra: int
    int countNewApplicantsToday(String employerId);

    // Chuc nang: Them don ung tuyen
    // Dau vao: application (Application)
    // Dau ra: boolean
    boolean insert(Application application);

    // Chuc nang: Xoa don ung tuyen
    // Dau vao: applicationId (String)
    // Dau ra: boolean
    boolean delete(String applicationId);

    // Chuc nang: Cap nhat trang thai don ung tuyen
    // Dau vao: applicationId (String), status (ApplicationStatus)
    // Dau ra: boolean
    boolean updateStatus(String applicationId, ApplicationStatus status);
}
