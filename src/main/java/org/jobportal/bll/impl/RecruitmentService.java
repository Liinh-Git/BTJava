package org.jobportal.bll.impl;

import org.jobportal.bll.interfaces.IRecruitmentService;
import org.jobportal.bll.interfaces.INotificationService;
import org.jobportal.dal.impl.ApplicationDAO;
import org.jobportal.dal.impl.CategoryDAO;
import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.RecruitmentDAO;
import org.jobportal.dal.interfaces.IApplicationDAO;
import org.jobportal.dal.interfaces.ICategoryDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.IRecruitmentDAO;
import org.jobportal.dto.RecruitmentDTO;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.enums.JobType;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.enums.Role;
import org.jobportal.model.Application;
import org.jobportal.model.Category;
import org.jobportal.model.Employer;
import org.jobportal.model.Recruitment;
import org.jobportal.utils.IdGenerator;
import org.jobportal.utils.SessionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * RecruitmentService - Xu ly nghiep vu tin tuyen dung.
 * Khong viet SQL, khong goi Swing.
 */
public class RecruitmentService implements IRecruitmentService {

    private final IRecruitmentDAO recruitmentDAO = new RecruitmentDAO();
    private final IApplicationDAO applicationDAO = new ApplicationDAO();
    private final IEmployerDAO    employerDAO    = new EmployerDAO();
    private final ICategoryDAO    categoryDAO    = new CategoryDAO();
    private final SessionManager  session        = SessionManager.getInstance();
    private final INotificationService notificationService = new NotificationService();

    // ------------------------------------------------------------------
    // ID generation
    // ------------------------------------------------------------------

    /** Sinh recruitmentId format "REC-" + 6 so => 10 ky tu */
    private String generateRecruitmentId() {
        String latestId = recruitmentDAO.getLatestRecruitmentId();
        return IdGenerator.nextId(latestId, "REC", 6);
    }

    // ------------------------------------------------------------------
    // postRecruitment
    // ------------------------------------------------------------------

    /**
     * Employer dang tin tuyen dung moi.
     * Mac dinh: status=OPEN, adminStatus=PENDING.
     */
    @Override
    public boolean postRecruitment(String title, String categoryId, JobType jobType,
                                   double salary, LocalDate dueDate, String description,
                                   String location) {
        String employerId = session.getEmployerId();
        if (employerId == null) {
            System.err.println("[RecruitmentService] postRecruitment: chua dang nhap hoac khong phai EMPLOYER.");
            return false;
        }
        if (title == null || title.isBlank()) {
            System.err.println("[RecruitmentService] postRecruitment: tieu de khong duoc de trong.");
            return false;
        }
        if (dueDate == null || !dueDate.isAfter(LocalDate.now())) {
            System.err.println("[RecruitmentService] postRecruitment: han nop phai sau ngay hom nay.");
            return false;
        }

        String recruitmentId = generateRecruitmentId();

        String resolvedLocation = null;
        if (location != null && !location.isBlank() && !"Địa chỉ mặc định công ty".equalsIgnoreCase(location.trim())) {
            resolvedLocation = location.trim();
        } else {
            Employer employer = employerDAO.findById(employerId);
            if (employer != null && employer.getCompanyAddress() != null && !employer.getCompanyAddress().isBlank()) {
                resolvedLocation = employer.getCompanyAddress().trim();
            }
        }

        Recruitment r = new Recruitment(
                recruitmentId, employerId, categoryId,
                title.trim(), description,
                jobType,
                RecruitmentStatus.OPEN,
                AdminStatus.PENDING,
                salary,
                resolvedLocation,
                null,
                LocalDateTime.now(),
                dueDate.atTime(23, 59, 59)
        );
        return recruitmentDAO.insert(r);
    }

    // ------------------------------------------------------------------
    // updateRecruitment
    // ------------------------------------------------------------------

    /**
     * Cap nhat tin tuyen dung.
     * Rang buoc: employer phai la chu tin.
     * Sau khi cap nhat, adminStatus reset ve PENDING.
     */
    @Override
    public boolean updateRecruitment(String recruitmentId, String title, String categoryId,
                                     JobType jobType, double salary, LocalDate dueDate,
                                     String description, String location) {
        Recruitment r = getAndVerifyOwnership(recruitmentId);
        if (r == null) return false;
        if (title == null || title.isBlank()) return false;

        r.setTitle(title.trim());
        r.setCategoryId(categoryId);
        r.setJobType(jobType);
        r.setSalary(salary);
        r.setDueDate(dueDate != null ? dueDate.atTime(23, 59, 59) : null);
        r.setDescription(description);
        r.setLocation(location != null ? location.trim() : null);
        r.setAdminStatus(AdminStatus.PENDING);

        return recruitmentDAO.update(r);
    }

    // ------------------------------------------------------------------
    // deleteRecruitment
    // ------------------------------------------------------------------

    /**
     * Xoa tin tuyen dung.
     * Rang buoc: employer la chu tin, khong co don APPROVED.
     */
    @Override
    public boolean deleteRecruitment(String recruitmentId) {
        Recruitment r = getAndVerifyOwnership(recruitmentId);
        if (r == null) return false;

        List<Application> approvedApps =
                applicationDAO.findByRecruitmentIdAndStatus(recruitmentId, ApplicationStatus.APPROVED);
        if (approvedApps != null && !approvedApps.isEmpty()) {
            System.err.println("[RecruitmentService] deleteRecruitment: co don APPROVED, khong the xoa.");
            return false;
        }
        return recruitmentDAO.delete(recruitmentId);
    }

    // ------------------------------------------------------------------
    // closeRecruitment
    // ------------------------------------------------------------------

    @Override
    public boolean closeRecruitment(String recruitmentId) {
        Recruitment r = getAndVerifyOwnership(recruitmentId);
        if (r == null) return false;
        return recruitmentDAO.updateStatus(recruitmentId, RecruitmentStatus.CLOSED);
    }

    // ------------------------------------------------------------------
    // getRecruitmentById
    // ------------------------------------------------------------------

    @Override
    public RecruitmentDTO getRecruitmentById(String recruitmentId) {
        if (recruitmentId == null || recruitmentId.isBlank()) return null;
        Recruitment r = recruitmentDAO.findById(recruitmentId);
        return (r != null) ? mapToDTO(r) : null;
    }

    // ------------------------------------------------------------------
    // searchRecruitments — Chi OPEN + APPROVED
    // ------------------------------------------------------------------

    @Override
    public List<RecruitmentDTO> searchRecruitments(String keyword, String categoryId,
                                                    int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        int offset = (page - 1) * pageSize;

        List<Recruitment> list = recruitmentDAO.searchByKeywordAndCategory(
                keyword, categoryId, offset, pageSize);
        if (list == null || list.isEmpty()) return Collections.emptyList();

        List<RecruitmentDTO> result = new ArrayList<>();
        for (Recruitment r : list) {
            if (r.getStatus() == RecruitmentStatus.OPEN
                    && r.getAdminStatus() == AdminStatus.APPROVED) {
                result.add(mapToDTO(r));
            }
        }
        return result;
    }

    // ------------------------------------------------------------------
    // getRecruitmentsByEmployer
    // ------------------------------------------------------------------

    @Override
    public List<RecruitmentDTO> getRecruitmentsByEmployer(String employerId) {
        if (employerId == null || employerId.isBlank()) return Collections.emptyList();
        List<Recruitment> list = recruitmentDAO.findByEmployerId(employerId);
        return mapListToDTO(list);
    }

    // ------------------------------------------------------------------
    // getPendingRecruitments
    // ------------------------------------------------------------------

    @Override
    public List<RecruitmentDTO> getPendingRecruitments() {
        List<Recruitment> list = recruitmentDAO.findByAdminStatus("PENDING");
        return mapListToDTO(list);
    }

    // ------------------------------------------------------------------
    // adminModerate — Chi ADMIN duoc goi
    // ------------------------------------------------------------------

    @Override
    public boolean adminModerate(String recruitmentId, String decision) {
        if (session.getCurrentRole() != Role.ADMIN) {
            System.err.println("[RecruitmentService] adminModerate: chi ADMIN moi duoc duyet tin.");
            return false;
        }
        if (recruitmentId == null || recruitmentId.isBlank()) return false;
        if (!"APPROVED".equals(decision) && !"REJECTED".equals(decision)) {
            System.err.println("[RecruitmentService] adminModerate: decision phai la APPROVED hoac REJECTED.");
            return false;
        }
        boolean updated = recruitmentDAO.updateAdminStatus(recruitmentId, decision);
        if (updated) {
            Recruitment r = recruitmentDAO.findById(recruitmentId);
            if (r != null) {
                Employer employer = employerDAO.findById(r.getEmployerId());
                String receiverUserId = employer != null ? employer.getUserId() : null;
                if (receiverUserId != null) {
                    String content = "Tin tuyển dụng \"" + r.getTitle() + "\" đã "
                            + ("APPROVED".equals(decision) ? "được duyệt." : "bị từ chối.");
                    notificationService.sendNotification(session.getCurrentUserId(), receiverUserId, content);
                }
            }
        }
        return updated;
    }

    // ------------------------------------------------------------------
    // countOpenRecruitments / getTopEmployers
    // ------------------------------------------------------------------

    @Override
    public int countOpenRecruitments() {
        return recruitmentDAO.countOpenRecruitments();
    }

    @Override
    public List<Map<String, Object>> getTopEmployers() {
        List<Map<String, Object>> list = recruitmentDAO.getTopEmployers();
        return (list != null) ? list : Collections.emptyList();
    }

    // ------------------------------------------------------------------
    // Employer dashboard stats
    // ------------------------------------------------------------------

    @Override
    public int countRecruitmentsByEmployer(String employerId) {
        return countByEmployerWithFilters(employerId, null, null);
    }

    @Override
    public int countOpenRecruitmentsByEmployer(String employerId) {
        return countByEmployerWithFilters(employerId, RecruitmentStatus.OPEN, AdminStatus.APPROVED);
    }

    @Override
    public int countPendingRecruitmentsByEmployer(String employerId) {
        return countByEmployerWithFilters(employerId, null, AdminStatus.PENDING);
    }

    @Override
    public int countRejectedRecruitmentsByEmployer(String employerId) {
        return countByEmployerWithFilters(employerId, null, AdminStatus.REJECTED);
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    private Recruitment getAndVerifyOwnership(String recruitmentId) {
        if (recruitmentId == null || recruitmentId.isBlank()) return null;
        String employerId = session.getEmployerId();
        if (employerId == null) {
            System.err.println("[RecruitmentService] verify: chua dang nhap hoac khong phai EMPLOYER.");
            return null;
        }
        Recruitment r = recruitmentDAO.findById(recruitmentId);
        if (r == null) {
            System.err.println("[RecruitmentService] verify: khong tim thay recruitmentId=" + recruitmentId);
            return null;
        }
        if (!employerId.equals(r.getEmployerId())) {
            System.err.println("[RecruitmentService] verify: employer khong co quyen thao tac tin nay.");
            return null;
        }
        return r;
    }

    private RecruitmentDTO mapToDTO(Recruitment r) {
        String companyName = null;
        Employer employer = employerDAO.findById(r.getEmployerId());
        if (employer != null) companyName = employer.getCompanyName();

        String categoryName = null;
        Category category = categoryDAO.findById(r.getCategoryId());
        if (category != null) categoryName = category.getCategoryName();

        int applicationCount = 0;
        List<Application> apps = applicationDAO.findByRecruitmentId(r.getRecruitmentId());
        if (apps != null) {
            applicationCount = apps.size();
        }

        return new RecruitmentDTO(
                r.getRecruitmentId(), r.getEmployerId(), companyName,
                r.getCategoryId(), categoryName,
                r.getTitle(), r.getDescription(),
                r.getJobType(), r.getStatus(), r.getAdminStatus(),
                r.getSalary(), r.getLocation(), r.getExperienceRequired(),
                r.getCreatedDate(), r.getDueDate(),
                applicationCount
        );
    }

    private List<RecruitmentDTO> mapListToDTO(List<Recruitment> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<RecruitmentDTO> result = new ArrayList<>();
        for (Recruitment r : list) result.add(mapToDTO(r));
        return result;
    }

    private int countByEmployerWithFilters(String employerId, RecruitmentStatus statusFilter,
                                           AdminStatus adminStatusFilter) {
        if (employerId == null || employerId.isBlank()) return 0;
        List<Recruitment> list = recruitmentDAO.findByEmployerId(employerId);
        if (list == null || list.isEmpty()) return 0;

        int count = 0;
        for (Recruitment r : list) {
            if (statusFilter != null && r.getStatus() != statusFilter) continue;
            if (adminStatusFilter != null && r.getAdminStatus() != adminStatusFilter) continue;
            count++;
        }
        return count;
    }
}
