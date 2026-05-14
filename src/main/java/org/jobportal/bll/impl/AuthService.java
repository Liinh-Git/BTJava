package org.jobportal.bll.impl;

import org.jobportal.bll.interfaces.IAuthService;
import org.jobportal.dal.impl.CandidateDAO;
import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.ICandidateDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Role;
import org.jobportal.model.Candidate;
import org.jobportal.model.Employer;
import org.jobportal.model.User;
import org.jobportal.utils.PasswordUtils;
import org.jobportal.utils.SessionManager;
import org.jobportal.utils.ValidationUtils;

import java.time.LocalDateTime;

/**
 * AuthService - Xu ly nghiep vu xac thuc (dang ky, dang nhap, dang xuat, doi mat khau).
 * Khong viet SQL, khong goi Swing/JOptionPane.
 * Chi goi DAO / Utils / SessionManager.
 */
public class AuthService implements IAuthService {

    // Dung DAO cu the (impl) vi project chua dung DI framework.
    // Neu can test, co the doi sang interface va inject.
    private final IUserDAO      userDAO      = new UserDAO();
    private final ICandidateDAO candidateDAO = new CandidateDAO();
    private final IEmployerDAO  employerDAO  = new EmployerDAO();
    private final SessionManager session     = SessionManager.getInstance();
    private String lastErrorMessage;

    // ------------------------------------------------------------------
    // ID generation helpers
    // ------------------------------------------------------------------

    /**
     * Sinh userId theo format "U-" + 8 chu so zero-padded, tong 10 ky tu.
     * Vi du: "U-00001234"
     * Dung timestamp millis de giam xac suat trung (co the thay bang sequence sau).
     */
    private String generateUserId() {
        long ts = System.currentTimeMillis() % 100_000_000L; // 8 chu so
        return String.format("U-%08d", ts);
    }

    /** Sinh candidateId format "CAN-" + 6 so => 10 ky tu */
    private String generateCandidateId() {
        long ts = System.currentTimeMillis() % 1_000_000L;
        return String.format("CAN-%06d", ts);
    }

    /** Sinh employerId format "EMP-" + 6 so => 10 ky tu */
    private String generateEmployerId() {
        long ts = System.currentTimeMillis() % 1_000_000L;
        return String.format("EMP-%06d", ts);
    }

    // ------------------------------------------------------------------
    // register
    // ------------------------------------------------------------------

    /**
     * Dang ky tai khoan moi.
     * Quy tac:
     *   - Khong cho tao ADMIN tu UI.
     *   - Validate day du truoc khi luu.
     *   - Hash password bang PasswordUtils.hash().
     *   - Tao ban ghi trong users + candidates hoac employers.
     */
    @Override
    public boolean register(String username, String email, String password,
                            String confirmPassword, Role role) {
        lastErrorMessage = null;

        // 1. Khong cho tao ADMIN tu giao dien
        if (role == Role.ADMIN) {
            return fail("Không thể tạo tài khoản quản trị từ màn hình đăng ký.");
        }

        // 2. Validate cac truong bat buoc
        if (!ValidationUtils.isValidUsername(username)) {
            return fail("Tên đăng nhập phải dài 4-50 ký tự và chỉ gồm chữ, số hoặc dấu gạch dưới.");
        }
        if (!ValidationUtils.isValidEmail(email)) {
            return fail("Email không hợp lệ.");
        }
        if (!ValidationUtils.isValidPassword(password)) {
            return fail("Mật khẩu phải có ít nhất 6 ký tự.");
        }
        if (!ValidationUtils.isPasswordMatch(password, confirmPassword)) {
            return fail("Mật khẩu xác nhận không khớp.");
        }

        // 3. Kiem tra trung username / email
        if (userDAO.existsByUsername(username)) {
            return fail("Tên đăng nhập đã tồn tại.");
        }
        if (userDAO.existsByEmail(email)) {
            return fail("Email đã tồn tại.");
        }

        // 4. Sinh ID va hash password
        String userId = generateUserId();
        // Tranh trung neu may tinh nhanh
        while (userDAO.findById(userId) != null) {
            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
            userId = generateUserId();
        }

        String passwordHash = PasswordUtils.hash(password);
        LocalDateTime now = LocalDateTime.now();

        // 5. Tao User va luu vao DB
        User user = new User(userId, username, passwordHash,
                username,   // fullName tam dung username vi DB bat buoc NOT NULL; user cap nhat sau o UserProfile
                null, null, null, email,
                null,       // address tam de null, se cap nhat sau o UserProfile
                role, true, now);
        boolean userSaved;
        try {
            userSaved = userDAO.insert(user);
        } catch (RuntimeException ex) {
            return fail("Không thể lưu tài khoản người dùng: " + ex.getMessage());
        }
        if (!userSaved) {
            return fail("Không thể lưu tài khoản người dùng.");
        }

        // 6. Tao profile tuong ung theo role
        if (role == Role.CANDIDATE) {
            String candidateId = generateCandidateId();
            while (candidateDAO.findById(candidateId) != null) {
                try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                candidateId = generateCandidateId();
            }
            Candidate candidate = new Candidate(candidateId, userId);
            boolean candidateSaved = candidateDAO.insert(candidate);
            if (!candidateSaved) {
                userDAO.delete(userId);
                return fail("Không thể tạo hồ sơ ứng viên.");
            }

        } else if (role == Role.EMPLOYER) {
            String employerId = generateEmployerId();
            while (employerDAO.findById(employerId) != null) {
                try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                employerId = generateEmployerId();
            }
            // companyName mac dinh = username, Employer co the cap nhat qua CompanyInfoPanel
            Employer employer = new Employer(employerId, userId, username, null, null);
            boolean employerSaved = employerDAO.insert(employer);
            if (!employerSaved) {
                userDAO.delete(userId);
                return fail("Không thể tạo hồ sơ nhà tuyển dụng.");
            }
        }

        System.out.println("[AuthService] register: dang ky thanh cong - userId=" + userId);
        return true;
    }

    @Override
    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    // ------------------------------------------------------------------
    // login
    // ------------------------------------------------------------------

    /**
     * Dang nhap: xac thuc username + password + role + trang thai is_active.
     * Neu thanh cong, luu session va tra ve UserDTO.
     * Neu that bai, tra ve null.
     */
    @Override
    public UserDTO login(String username, String password, Role role) {

        // 1. Validate dau vao co ban
        if (ValidationUtils.isNullOrEmpty(username) || ValidationUtils.isNullOrEmpty(password)) {
            return null;
        }

        // 2. Hash password de so sanh
        String passwordHash = PasswordUtils.hash(password);

        // 3. Tim user trong DB
        User user = userDAO.findByUsernameAndPassword(username, passwordHash);
        if (user == null) {
            System.err.println("[AuthService] login: sai ten dang nhap hoac mat khau.");
            return null;
        }

        // 4. Kiem tra role khop
        if (user.getRole() != role) {
            System.err.println("[AuthService] login: role khong khop (chon sai loai tai khoan).");
            return null;
        }

        // 5. Kiem tra tai khoan dang hoat dong
        if (!user.isActive()) {
            System.err.println("[AuthService] login: tai khoan da bi khoa.");
            return null;
        }

        // 6. Map sang DTO
        UserDTO dto = mapToDTO(user);

        // 7. Lay profileId tuong ung va luu session
        String profileId = null;
        if (role == Role.CANDIDATE) {
            Candidate c = candidateDAO.findByUserId(user.getUserId());
            profileId = (c != null) ? c.getCandidateId() : null;
        } else if (role == Role.EMPLOYER) {
            Employer e = employerDAO.findByUserId(user.getUserId());
            if (e != null) {
                profileId = e.getEmployerId();
                dto.setCompanyName(e.getCompanyName());
            }
        }

        session.login(dto, profileId);
        System.out.println("[AuthService] login: thanh cong - userId=" + user.getUserId() + ", role=" + role);
        return dto;
    }

    // ------------------------------------------------------------------
    // logout
    // ------------------------------------------------------------------

    @Override
    public void logout() {
        System.out.println("[AuthService] logout: userId=" + session.getCurrentUserId());
        session.logout();
    }

    // ------------------------------------------------------------------
    // changePassword
    // ------------------------------------------------------------------

    /**
     * Doi mat khau: lay userId tu session, kiem tra mat khau cu, validate moi, cap nhat DB.
     */
    @Override
    public boolean changePassword(String oldPassword, String newPassword, String confirmNewPassword) {

        // 1. Kiem tra da dang nhap
        if (!session.isLoggedIn()) {
            System.err.println("[AuthService] changePassword: chua dang nhap.");
            return false;
        }
        String userId = session.getCurrentUserId();

        // 2. Lay user va kiem tra mat khau cu
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }
        String oldHash = PasswordUtils.hash(oldPassword);
        if (!user.getPasswordHash().equals(oldHash)) {
            System.err.println("[AuthService] changePassword: mat khau cu khong dung.");
            return false;
        }

        // 3. Validate mat khau moi
        if (!ValidationUtils.isValidPassword(newPassword)) {
            System.err.println("[AuthService] changePassword: mat khau moi qua ngan.");
            return false;
        }
        if (!ValidationUtils.isPasswordMatch(newPassword, confirmNewPassword)) {
            System.err.println("[AuthService] changePassword: xac nhan mat khau khong khop.");
            return false;
        }

        // 4. Cap nhat mat khau
        String newHash = PasswordUtils.hash(newPassword);
        boolean updated = userDAO.updatePassword(userId, newHash);
        if (updated) {
            System.out.println("[AuthService] changePassword: doi mat khau thanh cong.");
        }
        return updated;
    }

    // ------------------------------------------------------------------
    // Private helpers
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
                null   // companyName: se set sau neu la EMPLOYER
        );
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender());
        return dto;
    }

    private boolean fail(String message) {
        lastErrorMessage = message;
        System.err.println("[AuthService] " + message);
        return false;
    }
}

