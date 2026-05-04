package org.jobportal.bll.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Role;

public class UserService {
    // Chức năng: Cập nhật thông tin công ty
    // Đầu vào: companyName (String) - tên công ty; address (String) - địa chỉ; description (String) - mô tả
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ CompanyInfoPanel; sẽ gọi EmployerDAO
    // Ghi chú: Lấy employerId từ session
    public boolean updateCompanyInfo(String companyName, String address, String description) {
        // TODO: Bước 1 - Lấy employerId từ session
        // TODO: Bước 2 - Validate dữ liệu đầu vào
        // TODO: Bước 3 - Cập nhật thông tin công ty trong DB
        return false;
    }

    // Chức năng: Lấy thông tin employer
    // Đầu vào: userId (String) - mã user
    // Đầu ra: UserDTO - thông tin employer
    // Tương tác: Được gọi từ CompanyInfoPanel; sẽ gọi UserDAO và EmployerDAO
    // Ghi chú: Ghép thông tin user và employer
    public UserDTO getEmployerInfo(String userId) {
        // TODO: Bước 1 - Truy vấn UserDAO.findById
        // TODO: Bước 2 - Truy vấn EmployerDAO.findByUserId
        // TODO: Bước 3 - Map sang UserDTO
        return null;
    }

    // Chức năng: Lấy danh sách người dùng
    // Đầu vào: roleFilter (Role) - lọc theo role; statusFilter (Boolean) - lọc theo trạng thái
    // Đầu ra: List<UserDTO> - danh sách user
    // Tương tác: Được gọi từ UserManagementPanel; sẽ gọi UserDAO
    // Ghi chú: roleFilter hoặc statusFilter có thể null
    public List<UserDTO> getAllUsers(Role roleFilter, Boolean statusFilter) {
        // TODO: Bước 1 - Gọi UserDAO.findAll với bộ lọc
        // TODO: Bước 2 - Map danh sách sang DTO
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // Chức năng: Cập nhật trạng thái user
    // Đầu vào: userId (String) - mã user; isActive (boolean) - trạng thái
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ UserManagementPanel; sẽ gọi UserDAO
    // Ghi chú: Trạng thái tương ứng cột is_active
    public boolean updateUserStatus(String userId, boolean isActive) {
        // TODO: Bước 1 - Gọi UserDAO.updateStatus
        // TODO: Bước 2 - Xử lý kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // Chức năng: Xóa người dùng
    // Đầu vào: userId (String) - mã user
    // Đầu ra: boolean - true nếu xóa thành công
    // Tương tác: Được gọi từ UserManagementPanel; sẽ gọi UserDAO
    // Ghi chú: Không được xóa chính mình
    public boolean deleteUser(String userId) {
        // TODO: Bước 1 - Kiểm tra userId không phải chính mình
        // TODO: Bước 2 - Gọi UserDAO.delete
        // TODO: Bước 3 - Trả về kết quả
        return false;
    }

    // Chức năng: Đếm user theo role
    // Đầu vào: role (Role) - vai trò
    // Đầu ra: int - số lượng user
    // Tương tác: Được gọi từ AdminDashboardPanel; sẽ gọi UserDAO
    // Ghi chú: Đếm toàn hệ thống
    public int countByRole(Role role) {
        // TODO: Bước 1 - Gọi UserDAO.countByRole
        // TODO: Bước 2 - Xử lý kết quả
        // TODO: Bước 3 - Trả về số lượng
        return 0;
    }
}
