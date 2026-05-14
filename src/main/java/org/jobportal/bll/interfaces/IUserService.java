package org.jobportal.bll.interfaces;

import java.util.List;
import java.time.LocalDate;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Gender;
import org.jobportal.enums.Role;

public interface IUserService {
    // Chuc nang: Cap nhat thong tin cong ty
    // Dau vao: companyName (String), address (String), description (String)
    // Dau ra: boolean
    boolean updateCompanyInfo(String companyName, String address, String description);

    // Chuc nang: Cap nhat thong tin ca nhan cho user dang dang nhap
    // Dau vao: fullName (String), phoneNumber (String)
    // Dau ra: boolean
    boolean updateUserProfile(String fullName, String phoneNumber, String email, String address, LocalDate dateOfBirth, Gender gender);

    // Chuc nang: Lay thong tin employer
    // Dau vao: userId (String)
    // Dau ra: UserDTO
    UserDTO getEmployerInfo(String userId);

    // Chuc nang: Lay danh sach nguoi dung
    // Dau vao: roleFilter (Role), statusFilter (Boolean)
    // Dau ra: List<UserDTO>
    List<UserDTO> getAllUsers(Role roleFilter, Boolean statusFilter);

    // Chuc nang: Tim kiem nguoi dung theo ten/email, role va trang thai
    // Dau vao: keyword (String), roleFilter (Role), statusFilter (Boolean)
    // Dau ra: List<UserDTO>
    List<UserDTO> searchUsers(String keyword, Role roleFilter, Boolean statusFilter);

    // Chuc nang: Cap nhat trang thai user
    // Dau vao: userId (String), isActive (boolean)
    // Dau ra: boolean
    boolean updateUserStatus(String userId, boolean isActive);

    // Chuc nang: Xoa nguoi dung
    // Dau vao: userId (String)
    // Dau ra: boolean
    boolean deleteUser(String userId);

    // Chuc nang: Dem user theo role
    // Dau vao: role (Role)
    // Dau ra: int
    int countByRole(Role role);
}
