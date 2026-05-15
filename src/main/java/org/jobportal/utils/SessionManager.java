package org.jobportal.utils;

import org.jobportal.dto.UserDTO;
import org.jobportal.enums.Role;

/**
 * SessionManager - Singleton quan ly phien dang nhap cua nguoi dung.
 * Cac Service goi SessionManager de lay thong tin nguoi dung hien tai
 * ma khong can truyen userId qua tung method.
 *
 * Luu y: day la session in-memory cho Desktop App (Swing),
 * khong dung cookie/token nhu web app.
 */
public class SessionManager {

    // ----------------------------------------------------------------
    // Singleton
    // ----------------------------------------------------------------
    private static SessionManager instance;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // ----------------------------------------------------------------
    // State
    // ----------------------------------------------------------------
    /** Thong tin co ban cua nguoi dung dang dang nhap */
    private UserDTO currentUser;

    /**
     * candidateId cua nguoi dang nhap (chi co gia tri neu role = CANDIDATE).
     * Duoc set sau khi tra cuu CandidateDAO o AuthService.
     */
    private String currentCandidateId;

    /**
     * employerId cua nguoi dang nhap (chi co gia tri neu role = EMPLOYER).
     * Duoc set sau khi tra cuu EmployerDAO o AuthService.
     */
    private String currentEmployerId;

    // ----------------------------------------------------------------
    // Login / Logout
    // ----------------------------------------------------------------

    /**
     * Luu thong tin user vao session.
     * @param user      UserDTO tra ve tu AuthService sau khi xac thuc thanh cong
     * @param profileId candidateId neu role=CANDIDATE, employerId neu role=EMPLOYER, null neu ADMIN
     */
    public void login(UserDTO user, String profileId) {
        this.currentUser = user;
        if (user.getRole() == Role.CANDIDATE) {
            this.currentCandidateId = profileId;
            this.currentEmployerId  = null;
        } else if (user.getRole() == Role.EMPLOYER) {
            this.currentEmployerId  = profileId;
            this.currentCandidateId = null;
        } else {
            // ADMIN
            this.currentCandidateId = null;
            this.currentEmployerId  = null;
        }
    }

    /** Xoa toan bo thong tin phien lam viec. */
    public void logout() {
        this.currentUser        = null;
        this.currentCandidateId = null;
        this.currentEmployerId  = null;
    }

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    /** @return true neu co nguoi dang dang nhap */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /** @return UserDTO nguoi dang nhap, null neu chua dang nhap */
    public UserDTO getCurrentUser() {
        return currentUser;
    }

    /** @return userId cua nguoi dang nhap, null neu chua dang nhap */
    public String getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : null;
    }

    /** @return Role cua nguoi dang nhap, null neu chua dang nhap */
    public Role getCurrentRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    /**
     * @return candidateId neu nguoi dang nhap la CANDIDATE, null neu khong phai
     */
    public String getCandidateId() {
        return currentCandidateId;
    }

    /**
     * @return employerId neu nguoi dang nhap la EMPLOYER, null neu khong phai
     */
    public String getEmployerId() {
        return currentEmployerId;
    }
}

