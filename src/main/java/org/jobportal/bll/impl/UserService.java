package org.jobportal.bll.impl;

import org.jobportal.bll.interfaces.IUserService;
import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Gender;
import org.jobportal.enums.Role;
import org.jobportal.model.Employer;
import org.jobportal.model.User;
import org.jobportal.utils.SessionManager;
import org.jobportal.utils.ValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UserService - Xu ly nghiep vu lien quan den nguoi dung.
 * Khong viet SQL, khong goi Swing.
 */
public class UserService implements IUserService {

    private final IUserDAO     userDAO     = new UserDAO();
    private final IEmployerDAO employerDAO = new EmployerDAO();
    private final SessionManager session   = SessionManager.getInstance();

    // ------------------------------------------------------------------
    // getAllUsers
    // ------------------------------------------------------------------

    /**
     * Lay danh sach nguoi dung, co loc theo role va/hoac trang thai.
     * @param roleFilter   null = khong loc theo role
     * @param statusFilter null = khong loc theo trang thai
     */
    @Override
    public List<UserDTO> getAllUsers(Role roleFilter, Boolean statusFilter) {
        List<User> users = userDAO.findAll(roleFilter, statusFilter);
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        List<UserDTO> result = new ArrayList<>();
        for (User u : users) {
            result.add(mapToDTO(u));
        }
        return result;
    }

    @Override
    public List<UserDTO> searchUsers(String keyword, Role roleFilter, Boolean statusFilter) {
        List<UserDTO> users = getAllUsers(roleFilter, statusFilter);
        if (users.isEmpty()) {
            return users;
        }

        String normalizedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        if (normalizedKeyword.isEmpty()) {
            return users;
        }

        List<UserDTO> result = new ArrayList<>();
        for (UserDTO user : users) {
            String username = user.getUsername() != null ? user.getUsername().toLowerCase() : "";
            String fullName = user.getFullName() != null ? user.getFullName().toLowerCase() : "";
            String email = user.getEmail() != null ? user.getEmail().toLowerCase() : "";
            if (username.contains(normalizedKeyword)
                    || fullName.contains(normalizedKeyword)
                    || email.contains(normalizedKeyword)) {
                result.add(user);
            }
        }
        return result;
    }

    // ------------------------------------------------------------------
    // updateUserStatus
    // ------------------------------------------------------------------

    @Override
    public boolean updateUserStatus(String userId, boolean isActive) {
        if (userId == null || userId.isBlank()) return false;
        return userDAO.updateStatus(userId, isActive);
    }

    // ------------------------------------------------------------------
    // deleteUser
    // ------------------------------------------------------------------

    /**
     * Xoa nguoi dung.
     * Rang buoc: khong duoc xoa chinh ban than dang dang nhap.
     */
    @Override
    public boolean deleteUser(String userId) {
        if (userId == null || userId.isBlank()) return false;

        // Khong xoa chinh minh
        String currentId = session.getCurrentUserId();
        if (userId.equals(currentId)) {
            System.err.println("[UserService] deleteUser: khong the xoa chinh ban than dang dang nhap.");
            return false;
        }

        return userDAO.delete(userId);
    }

    // ------------------------------------------------------------------
    // countByRole
    // ------------------------------------------------------------------

    @Override
    public int countByRole(Role role) {
        if (role == null) return 0;
        return userDAO.countByRole(role);
    }

    // ------------------------------------------------------------------
    // getEmployerInfo
    // ------------------------------------------------------------------

    /**
     * Lay thong tin employer: ghep User + Employer => UserDTO co chua companyName.
     */
    @Override
    public UserDTO getEmployerInfo(String userId) {
        if (userId == null || userId.isBlank()) return null;

        User user = userDAO.findById(userId);
        if (user == null) return null;

        UserDTO dto = mapToDTO(user);

        // Lay them thong tin cong ty
        Employer employer = employerDAO.findByUserId(userId);
        if (employer != null) {
            dto.setCompanyName(employer.getCompanyName());
            dto.setCompanyAddress(employer.getCompanyAddress());
            dto.setCompanyDescription(employer.getCompanyDescription());
        }

        return dto;
    }

    // ------------------------------------------------------------------
    // updateCompanyInfo
    // ------------------------------------------------------------------

    /**
     * Cap nhat thong tin cong ty cho Employer dang dang nhap.
     * Lay employerId tu session.
     */
    @Override
    public boolean updateCompanyInfo(String companyName, String address, String description) {
        String employerId = session.getEmployerId();
        if (employerId == null) {
            System.err.println("[UserService] updateCompanyInfo: khong tim thay employerId trong session.");
            return false;
        }
        if (companyName == null || companyName.isBlank()) {
            System.err.println("[UserService] updateCompanyInfo: ten cong ty khong duoc de trong.");
            return false;
        }

        Employer employer = employerDAO.findById(employerId);
        if (employer == null) {
            System.err.println("[UserService] updateCompanyInfo: khong tim thay employer.");
            return false;
        }

        employer.setCompanyName(companyName.trim());
        employer.setCompanyAddress(address != null ? address.trim() : null);
        employer.setCompanyDescription(description != null ? description.trim() : null);

        return employerDAO.update(employer);
    }

    // ------------------------------------------------------------------
    // updateUserProfile
    // ------------------------------------------------------------------

    /**
     * Cap nhat thong tin ca nhan (ten hien thi, so dien thoai) cho user dang dang nhap.
     */
    @Override
    public boolean updateUserProfile(String fullName, String phoneNumber, String email, String address, LocalDate dateOfBirth, Gender gender) {
        if (!session.isLoggedIn()) {
            System.err.println("[UserService] updateUserProfile: chua dang nhap.");
            return false;
        }

        if (ValidationUtils.isNullOrEmpty(fullName)) {
            System.err.println("[UserService] updateUserProfile: fullName khong duoc de trong.");
            return false;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            System.err.println("[UserService] updateUserProfile: email khong hop le.");
            return false;
        }

        String userId = session.getCurrentUserId();
        User user = userDAO.findById(userId);
        if (user == null) {
            System.err.println("[UserService] updateUserProfile: khong tim thay user.");
            return false;
        }

        user.setFullName(fullName.trim());
        String phone = (phoneNumber != null) ? phoneNumber.trim() : null;
        user.setPhoneNumber((phone != null && !phone.isEmpty()) ? phone : null);
        user.setEmail(email.trim());
        String addr = (address != null) ? address.trim() : null;
        user.setAddress((addr != null && !addr.isEmpty()) ? addr : null);
        user.setDateOfBirth(dateOfBirth);
        user.setGender(gender);

        boolean updated = userDAO.update(user);
        if (updated) {
            UserDTO current = session.getCurrentUser();
            if (current != null) {
                current.setFullName(user.getFullName());
                current.setPhoneNumber(user.getPhoneNumber());
                current.setEmail(user.getEmail());
                current.setAddress(user.getAddress());
                current.setDateOfBirth(user.getDateOfBirth());
                current.setGender(user.getGender());
            }
        }
        return updated;
    }

    // ------------------------------------------------------------------
    // Private helper
    // ------------------------------------------------------------------

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isActive(),
                null   // companyName: se set neu can thiet
        );
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
        return dto;
    }
}
