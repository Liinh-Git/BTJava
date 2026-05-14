package org.jobportal.bll.interfaces;

import java.util.List;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.dto.UserDTO;

public interface IApplicationService {
    // Chuc nang: Ung tuyen vao mot tin tuyen dung
    // Dau vao: candidateId (String), recruitmentId (String)
    // Dau ra: boolean
    boolean applyRecruitment(String candidateId, String recruitmentId);

    // Chuc nang: Huy don ung tuyen
    // Dau vao: applicationId (String)
    // Dau ra: boolean
    boolean cancelApplication(String applicationId);

    // Chuc nang: Lay danh sach don ung tuyen cua ung vien
    // Dau vao: candidateId (String)
    // Dau ra: List<ApplicationDTO>
    List<ApplicationDTO> getListOfApplicationByUser(String candidateId);

    // Chuc nang: Dem tong so don ung tuyen cua ung vien
    // Dau vao: candidateId (String)
    // Dau ra: int
    int getTotalApplyCountByUser(String candidateId);

    // Chuc nang: Dem so don da duoc duyet
    // Dau vao: candidateId (String)
    // Dau ra: int
    int getApprovedApplicationByUser(String candidateId);

    // Chuc nang: Kiem tra ung vien da ung tuyen chua
    // Dau vao: candidateId (String), recruitmentId (String)
    // Dau ra: boolean
    boolean hasApplied(String candidateId, String recruitmentId);

    // Chuc nang: Lay danh sach don ung tuyen theo tin
    // Dau vao: recruitmentId (String)
    // Dau ra: List<ApplicationDTO>
    List<ApplicationDTO> getApplicationsByRecruitmentId(String recruitmentId);

    // Chuc nang: Lay danh sach ho so dang cho duyet theo employer
    // Dau vao: employerId (String)
    // Dau ra: List<ApplicationDTO>
    List<ApplicationDTO> getPendingApplicationsByEmployer(String employerId);

    // Chuc nang: Duyet don ung tuyen
    // Dau vao: userId (String), applicationId (String)
    // Dau ra: boolean
    boolean approveApplication(String userId, String applicationId);

    // Chuc nang: Tu choi don ung tuyen
    // Dau vao: userId (String), applicationId (String)
    // Dau ra: boolean
    boolean rejectApplication(String userId, String applicationId);

    // Chuc nang: Lay thong tin ung vien
    // Dau vao: candidateId (String)
    // Dau ra: UserDTO
    UserDTO getCandidateInfo(String candidateId);

    // Chuc nang: Dem tong don ung tuyen theo employer
    // Dau vao: employerId (String)
    // Dau ra: int
    int getTotalApplicationCount(String employerId);

    // Chuc nang: Dem ung vien moi trong hom nay
    // Dau vao: employerId (String)
    // Dau ra: int
    int getNewApplicantsToday(String employerId);

}
