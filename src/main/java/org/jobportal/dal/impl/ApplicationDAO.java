package org.jobportal.dal.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.model.Application;

public class ApplicationDAO {
    // Chức năng: Lấy danh sách đơn theo ứng viên
    // Đầu vào: candidateId (String) - mã ứng viên
    // Đầu ra: List<Application> - danh sách đơn
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Lọc theo candidateId
    public List<Application> findByCandidateId(String candidateId) {
        // TODO: Bước 1 - Query SELECT theo candidateId
        // TODO: Bước 2 - Map ResultSet sang list Application
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // Chức năng: Lấy danh sách đơn theo tin
    // Đầu vào: recruitmentId (String) - mã tin
    // Đầu ra: List<Application> - danh sách đơn
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Lọc theo recruitmentId
    public List<Application> findByRecruitmentId(String recruitmentId) {
        // TODO: Buoc 1 - Query SELECT theo recruitmentId
        // TODO: Buoc 2 - Map ResultSet sang list Application
        // TODO: Buoc 3 - Tra ve danh sach
        return Collections.emptyList();
    }

    // Chức năng: Lấy danh sách đơn theo tin và trạng thái
    // Đầu vào: recruitmentId (String) - mã tin; status (ApplicationStatus) - trạng thái
    // Đầu ra: List<Application> - danh sách đơn
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Lọc theo recruitmentId và status
    public List<Application> findByRecruitmentIdAndStatus(String recruitmentId, ApplicationStatus status) {
        // TODO: Bước 1 - Query SELECT theo recruitmentId và status
        // TODO: Bước 2 - Map ResultSet sang list Application
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // Chức năng: Kiểm tra ứng viên đã ứng tuyển chưa
    // Đầu vào: candidateId (String) - mã ứng viên; recruitmentId (String) - mã tin
    // Đầu ra: boolean - true nếu đã ứng tuyển
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Dùng để chống apply trùng
    public boolean existsByCandidateAndRecruitment(String candidateId, String recruitmentId) {
        // TODO: Bước 1 - Query COUNT theo candidateId và recruitmentId
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Đếm số đơn theo ứng viên
    // Đầu vào: candidateId (String) - mã ứng viên
    // Đầu ra: int - số đơn
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Đếm theo candidateId
    public int countByCandidateId(String candidateId) {
        // TODO: Bước 1 - Query COUNT theo candidateId
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }

    // Chức năng: Đếm số đơn theo ứng viên và trạng thái
    // Đầu vào: candidateId (String) - mã ứng viên; status (ApplicationStatus) - trạng thái
    // Đầu ra: int - số đơn
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Đếm theo candidateId và status
    public int countByCandidateIdAndStatus(String candidateId, ApplicationStatus status) {
        // TODO: Bước 1 - Query COUNT theo candidateId và status
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }

    // Chuc nang: Dem so don theo employer
    // Dau vao: employerId (String) - ma employer
    // Dau ra: int - so don
    // Tuong tac: Duoc goi tu ApplicationService; se dung JDBC
    // Ghi chu: Dem thong qua recruitment
    public int countByEmployerId(String employerId) {
        // TODO: Bước 1 - Query COUNT theo employerId
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }

    // Chức năng: Đếm ứng viên mới trong hôm nay
    // Đầu vào: employerId (String) - mã employer
    // Đầu ra: int - số ứng viên mới
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Lọc theo ngày hiện tại
    public int countNewApplicantsToday(String employerId) {
        // TODO: Bước 1 - Query COUNT theo employerId và ngày
        // TODO: Bước 2 - Đọc kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }

    // Chức năng: Thêm đơn ứng tuyển
    // Đầu vào: application (Application) - đơn ứng tuyển
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Tạo applicationId và appliedDate trước khi insert
    public boolean insert(Application application) {
        // TODO: Bước 1 - Tạo câu lệnh INSERT
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Xóa đơn ứng tuyển
    // Đầu vào: applicationId (String) - mã đơn
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ ApplicationService; sẽ dùng JDBC
    // Ghi chú: Có thể thay bằng update status CANCELLED
    public boolean delete(String applicationId) {
        // TODO: Bước 1 - Tạo câu lệnh DELETE
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chuc nang: Cap nhat trang thai don ung tuyen
    // Dau vao: applicationId (String) - ma don; status (ApplicationStatus) - trang thai
    // Dau ra: boolean - true neu cap nhat thanh cong
    // Tuong tac: Duoc goi tu ApplicationService; se dung JDBC
    // Ghi chu: Update cot status
    public boolean updateStatus(String applicationId, ApplicationStatus status) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE status
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }
}
