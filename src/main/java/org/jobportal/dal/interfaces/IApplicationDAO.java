package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.model.Application;

public interface IApplicationDAO {

    // Chức năng: Lấy đơn ứng tuyển theo applicationId
    // Đầu vào: applicationId (String) - mã đơn ứng tuyển
    // Đầu ra: Application - đối tượng đơn ứng tuyển
    Application findById(String applicationId);

    // Chức năng: Lấy danh sách đơn theo ứng viên
    // Đầu vào: candidateId (String)
    // Đầu ra: List<Application>
    List<Application> findByCandidateId(String candidateId);

    // Chức năng: Lấy danh sách đơn theo tin
    // Đầu vào: recruitmentId (String)
    // Đầu ra: List<Application>
    List<Application> findByRecruitmentId(String recruitmentId);

    // Chức năng: Lấy danh sách đơn theo tin và trạng thái
    // Đầu vào: recruitmentId (String), status (ApplicationStatus)
    // Đầu ra: List<Application>
    List<Application> findByRecruitmentIdAndStatus(String recruitmentId, ApplicationStatus status);

    // Chức năng: Kiểm tra ứng viên đã ứng tuyển chưa
    // Đầu vào: candidateId (String), recruitmentId (String)
    // Đầu ra: boolean
    boolean existsByCandidateAndRecruitment(String candidateId, String recruitmentId);

    // Chức năng: Đếm số đơn theo ứng viên
    // Đầu vào: candidateId (String)
    // Đầu ra: int
    int countByCandidateId(String candidateId);

    // Chức năng: Đếm số đơn theo ứng viên và trạng thái
    // Đầu vào: candidateId (String), status (ApplicationStatus)
    // Đầu ra: int
    int countByCandidateIdAndStatus(String candidateId, ApplicationStatus status);

    // Chức năng: Đếm số đơn theo employer
    // Đầu vào: employerId (String)
    // Đầu ra: int
    int countByEmployerId(String employerId);

    // Chức năng: Đếm ứng viên mới trong hôm nay
    // Đầu vào: employerId (String)
    // Đầu ra: int
    int countNewApplicantsToday(String employerId);

    // Chức năng: Thêm đơn ứng tuyển
    // Đầu vào: application (Application)
    // Đầu ra: boolean
    boolean insert(Application application);

    // Chức năng: Lấy application_id lớn nhất hiện có (theo format APP-xxxxxx)
    // Đầu ra: String applicationId hoặc null nếu chưa có dữ liệu
    String getLatestApplicationId();

    // Chức năng: Xóa đơn ứng tuyển
    // Đầu vào: applicationId (String)
    // Đầu ra: boolean
    boolean delete(String applicationId);

    // Chức năng: Cập nhật trạng thái đơn ứng tuyển
    // Đầu vào: applicationId (String), status (ApplicationStatus)
    // Đầu ra: boolean
    boolean updateStatus(String applicationId, ApplicationStatus status);
}
