package org.jobportal.bll.impl;

import org.jobportal.bll.interfaces.IUserService;
import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Role;
import org.jobportal.model.Employer;
import org.jobportal.model.User;
import org.jobportal.utils.SessionManager;

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
    // Private helper
    // ------------------------------------------------------------------

    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isActive(),
                null   // companyName: se set neu can thiet
        );
    }
}
