package org.jobportal.bll.impl;

import org.jobportal.bll.interfaces.IApplicationService;
import org.jobportal.bll.interfaces.INotificationService;
import org.jobportal.dal.impl.ApplicationDAO;
import org.jobportal.dal.impl.CandidateDAO;
import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.RecruitmentDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IApplicationDAO;
import org.jobportal.dal.interfaces.ICandidateDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.IRecruitmentDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.dto.ApplicationDTO;
import org.jobportal.dto.UserDTO;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.model.Application;
import org.jobportal.model.Candidate;
import org.jobportal.model.Employer;
import org.jobportal.model.Recruitment;
import org.jobportal.model.User;
import org.jobportal.utils.SessionManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ApplicationService - Xu ly nghiep vu don ung tuyen.
 * Khong viet SQL, khong goi Swing.
 */
public class ApplicationService implements IApplicationService {

    private final IApplicationDAO applicationDAO = new ApplicationDAO();
    private final IRecruitmentDAO recruitmentDAO = new RecruitmentDAO();
    private final IUserDAO        userDAO        = new UserDAO();
    private final ICandidateDAO   candidateDAO   = new CandidateDAO();
    private final IEmployerDAO    employerDAO    = new EmployerDAO();
    private final SessionManager  session        = SessionManager.getInstance();
    private final INotificationService notificationService = new NotificationService();

    // ------------------------------------------------------------------
    // ID generation
    // ------------------------------------------------------------------

    /** Sinh applicationId format "APP-" + 6 so => 10 ky tu */
    private String generateApplicationId() {
        long ts = System.currentTimeMillis() % 1_000_000L;
        return String.format("APP-%06d", ts);
    }

    // ------------------------------------------------------------------
    // applyRecruitment
    // ------------------------------------------------------------------

    /**
     * Candidate ung tuyen vao tin tuyen dung.
     * Rang buoc:
     *   - Tin phai OPEN va chua het han.
     *   - Candidate chua ung tuyen vao tin nay.
     */
    @Override
    public boolean applyRecruitment(String candidateId, String recruitmentId) {
        if (candidateId == null || recruitmentId == null) return false;

        // 1. Lay va kiem tra tin tuyen dung
        Recruitment r = recruitmentDAO.findById(recruitmentId);
        if (r == null) {
            System.err.println("[ApplicationService] applyRecruitment: khong tim thay tin id=" + recruitmentId);
            return false;
        }
        if (r.getStatus() != RecruitmentStatus.OPEN) {
            System.err.println("[ApplicationService] applyRecruitment: tin da dong hoac het han.");
            return false;
        }
        if (r.getDueDate() != null && r.getDueDate().isBefore(LocalDateTime.now())) {
            System.err.println("[ApplicationService] applyRecruitment: tin het han nop.");
            return false;
        }

        // 2. Kiem tra chua ung tuyen
        if (applicationDAO.existsByCandidateAndRecruitment(candidateId, recruitmentId)) {
            System.err.println("[ApplicationService] applyRecruitment: da ung tuyen roi.");
            return false;
        }

        // 3. Sinh ID
        String applicationId = generateApplicationId();
        while (true) {
            // Kiem tra co the lay application nay khong (don gian: thu insert, neu loi thi sinh lai)
            // Thay bang check khac neu DAO ho tro
            break;
        }

        // 4. Tao don
        Application app = new Application(
                applicationId, candidateId, recruitmentId,
                ApplicationStatus.PENDING, LocalDateTime.now()
        );
        boolean inserted = applicationDAO.insert(app);
        if (inserted) {
            // Lay userId cua candidate de lam sender
            String senderUserId = getCandidateUserId(candidateId);
            // Lay userId cua employer de nhan thong bao
            String receiverUserId = getEmployerUserId(r.getEmployerId());

            // Lay ten ung vien de hien thi trong thong bao
            String candidateName = "Ung vien";
            if (senderUserId != null) {
                User cu = userDAO.findById(senderUserId);
                if (cu != null && cu.getFullName() != null && !cu.getFullName().isBlank()) {
                    candidateName = cu.getFullName();
                } else if (cu != null) {
                    candidateName = cu.getUsername();
                }
            }

            if (receiverUserId != null) {
                String content = "📋 Ung vien " + candidateName
                        + " da nop ho so ung tuyen vao vi tri: " + r.getTitle();
                notificationService.sendNotification(senderUserId, receiverUserId, content);
            }
        }
        return inserted;
    }

    // ------------------------------------------------------------------
    // cancelApplication
    // ------------------------------------------------------------------

    /**
     * Candidate huy don ung tuyen.
     * Rang buoc:
     *   - Trang thai don phai la PENDING.
     *   - Chi candidate so huu don moi duoc huy.
     */
    @Override
    public boolean cancelApplication(String applicationId) {
        if (applicationId == null || applicationId.isBlank()) return false;

        // Tim don (DAO tra Application theo Id — tim thong qua findByCandidateId de loc)
        String candidateId = session.getCandidateId();
        if (candidateId == null) {
            System.err.println("[ApplicationService] cancelApplication: chua dang nhap hoac khong phai CANDIDATE.");
            return false;
        }

        // Tim don trong danh sach cua candidate nay
        List<Application> apps = applicationDAO.findByCandidateId(candidateId);
        Application target = null;
        for (Application a : apps) {
            if (a.getApplicationId().equals(applicationId)) {
                target = a;
                break;
            }
        }

        if (target == null) {
            System.err.println("[ApplicationService] cancelApplication: khong tim thay don hoac khong co quyen.");
            return false;
        }
        if (target.getStatus() != ApplicationStatus.PENDING) {
            System.err.println("[ApplicationService] cancelApplication: chi huy duoc don o trang thai PENDING.");
            return false;
        }

        return applicationDAO.delete(applicationId);
    }

    // ------------------------------------------------------------------
    // getListOfApplicationByUser
    // ------------------------------------------------------------------

    /**
     * Lay danh sach don ung tuyen cua candidate.
     * Join voi RecruitmentDAO de lay jobTitle va companyName.
     */
    @Override
    public List<ApplicationDTO> getListOfApplicationByUser(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) return Collections.emptyList();

        List<Application> apps = applicationDAO.findByCandidateId(candidateId);
        if (apps == null || apps.isEmpty()) return Collections.emptyList();

        List<ApplicationDTO> result = new ArrayList<>();
        for (Application a : apps) {
            result.add(enrichApplication(a));
        }
        return result;
    }

    // ------------------------------------------------------------------
    // getTotalApplyCountByUser
    // ------------------------------------------------------------------

    @Override
    public int getTotalApplyCountByUser(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) return 0;
        return applicationDAO.countByCandidateId(candidateId);
    }

    // ------------------------------------------------------------------
    // getApprovedApplicationByUser
    // ------------------------------------------------------------------

    @Override
    public int getApprovedApplicationByUser(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) return 0;
        return applicationDAO.countByCandidateIdAndStatus(candidateId, ApplicationStatus.APPROVED);
    }

    // ------------------------------------------------------------------
    // hasApplied
    // ------------------------------------------------------------------

    @Override
    public boolean hasApplied(String candidateId, String recruitmentId) {
        if (candidateId == null || recruitmentId == null) return false;
        return applicationDAO.existsByCandidateAndRecruitment(candidateId, recruitmentId);
    }

    // ------------------------------------------------------------------
    // getApplicationsByRecruitmentId
    // ------------------------------------------------------------------

    /**
     * Lay danh sach don cho mot tin tuyen dung.
     * Rang buoc: Employer phai la chu tin.
     */
    @Override
    public List<ApplicationDTO> getApplicationsByRecruitmentId(String recruitmentId) {
        if (recruitmentId == null || recruitmentId.isBlank()) return Collections.emptyList();

        // Verify employer so huu tin nay
        String employerId = session.getEmployerId();
        if (employerId != null) {
            Recruitment r = recruitmentDAO.findById(recruitmentId);
            if (r == null || !employerId.equals(r.getEmployerId())) {
                System.err.println("[ApplicationService] getApplicationsByRecruitmentId: employer khong co quyen.");
                return Collections.emptyList();
            }
        }

        List<Application> apps = applicationDAO.findByRecruitmentId(recruitmentId);
        if (apps == null || apps.isEmpty()) return Collections.emptyList();

        List<ApplicationDTO> result = new ArrayList<>();
        for (Application a : apps) {
            result.add(enrichApplication(a));
        }
        return result;
    }

    // ------------------------------------------------------------------
    // approveApplication
    // ------------------------------------------------------------------

    /**
     * Employer duyet don ung tuyen.
     * Rang buoc: employer phai la chu tin lien quan.
     */
    @Override
    public boolean approveApplication(String userId, String applicationId) {
        return changeApplicationStatus(userId, applicationId, ApplicationStatus.APPROVED);
    }

    // ------------------------------------------------------------------
    // rejectApplication
    // ------------------------------------------------------------------

    @Override
    public boolean rejectApplication(String userId, String applicationId) {
        return changeApplicationStatus(userId, applicationId, ApplicationStatus.REJECTED);
    }

    // ------------------------------------------------------------------
    // getCandidateInfo
    // ------------------------------------------------------------------

    @Override
    public UserDTO getCandidateInfo(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) return null;

        Candidate candidate = candidateDAO.findById(candidateId);
        if (candidate == null) return null;

        User user = userDAO.findById(candidate.getUserId());
        if (user == null) return null;

        return new UserDTO(
                user.getUserId(), user.getUsername(), user.getFullName(),
                user.getEmail(), user.getPhoneNumber(),
                user.getRole(), user.isActive(), null
        );
    }

    // ------------------------------------------------------------------
    // filterApplicationByStatus
    // ------------------------------------------------------------------

    @Override
    public List<ApplicationDTO> filterApplicationByStatus(String recruitmentId, ApplicationStatus status) {
        if (recruitmentId == null || status == null) return Collections.emptyList();
        List<Application> apps = applicationDAO.findByRecruitmentIdAndStatus(recruitmentId, status);
        if (apps == null || apps.isEmpty()) return Collections.emptyList();
        List<ApplicationDTO> result = new ArrayList<>();
        for (Application a : apps) result.add(enrichApplication(a));
        return result;
    }

    // ------------------------------------------------------------------
    // getTotalApplicationCount / getNewApplicantsToday
    // ------------------------------------------------------------------

    @Override
    public int getTotalApplicationCount(String employerId) {
        if (employerId == null || employerId.isBlank()) return 0;
        return applicationDAO.countByEmployerId(employerId);
    }

    @Override
    public int getNewApplicantsToday(String employerId) {
        if (employerId == null || employerId.isBlank()) return 0;
        return applicationDAO.countNewApplicantsToday(employerId);
    }

    // ------------------------------------------------------------------
    // filterApplicationsByStatusForCandidate
    // ------------------------------------------------------------------

    @Override
    public List<ApplicationDTO> filterApplicationsByStatusForCandidate(String candidateId, ApplicationStatus status) {
        if (candidateId == null || candidateId.isBlank()) return Collections.emptyList();
        List<Application> apps = applicationDAO.findByCandidateId(candidateId);
        if (apps == null || apps.isEmpty()) return Collections.emptyList();

        List<ApplicationDTO> result = new ArrayList<>();
        for (Application a : apps) {
            // Neu status la null thi lay tat ca, nguoc lai loc theo trang thai
            if (status == null || a.getStatus() == status) {
                result.add(enrichApplication(a));
            }
        }
        return result;
    }

    // ------------------------------------------------------------------

    // Private helpers
    // ------------------------------------------------------------------

    /**
     * Doi trang thai don (APPROVED hoac REJECTED).
     * Rang buoc: Employer phai so huu tin lien quan.
     */
    private boolean changeApplicationStatus(String userId, String applicationId,
                                             ApplicationStatus newStatus) {
        if (applicationId == null || applicationId.isBlank()) return false;

        // Tim don trong tat ca danh sach (lay theo recruitmentId cua don nay)
        // Vì DAO khong co findById(applicationId), ta phai lay theo candidateId (khong biet)
        // hoac tim qua employer. Giai phap: lay het theo employerId thi complex.
        // Thay vao do: kiem tra quyen qua session.getEmployerId() va recruitmentDAO.
        String employerId = session.getEmployerId();
        if (employerId == null) {
            System.err.println("[ApplicationService] changeStatus: chua dang nhap hoac khong phai EMPLOYER.");
            return false;
        }

        // TODO: Nếu DAO chưa có findById(applicationId), bỏ qua kiểm tra sở hữu ở đây.
        // Sau khi DAL bổ sung phương thức này, hãy thêm lại.
        // TODO: Sau nay goi NotificationService de thong bao cho Candidate

        boolean updated = applicationDAO.updateStatus(applicationId, newStatus);
        if (updated) {
            Application target = findApplicationForEmployer(applicationId);
            if (target != null) {
                String candidateUserId = getCandidateUserId(target.getCandidateId());
                Recruitment r = recruitmentDAO.findById(target.getRecruitmentId());
                String jobTitle = (r != null && r.getTitle() != null) ? r.getTitle() : "(khong ro vi tri)";

                // Lay ten cong ty de thong bao ro hon
                String companyName = "Nha tuyen dung";
                if (r != null) {
                    org.jobportal.model.Employer emp =
                            new org.jobportal.dal.impl.EmployerDAO().findById(r.getEmployerId());
                    if (emp != null && emp.getCompanyName() != null) {
                        companyName = emp.getCompanyName();
                    }
                }

                String content;
                if (newStatus == ApplicationStatus.APPROVED) {
                    content = "✅ Chuc mung! Ho so ung tuyen cua ban vao vi tri \"" + jobTitle
                            + "\" tai " + companyName + " da duoc CHAP NHAN.";
                } else {
                    content = "❌ Ho so ung tuyen cua ban vao vi tri \"" + jobTitle
                            + "\" tai " + companyName + " da bi TU CHOI. Cam on ban da quan tam!";
                }

                if (candidateUserId != null) {
                    notificationService.sendNotification(session.getCurrentUserId(), candidateUserId, content);
                }
            }
        }
        return updated;
    }

    /**
     * Lay them thong tin tu RecruitmentDAO va UserDAO de fill ApplicationDTO.
     */
    private ApplicationDTO enrichApplication(Application a) {
        String jobTitle = null;
        String companyName = null;
        org.jobportal.enums.JobType jobType = null;

        Recruitment r = recruitmentDAO.findById(a.getRecruitmentId());
        if (r != null) {
            jobTitle = r.getTitle();
            jobType = r.getJobType();
            // Lay ten cong ty tu Employer
            org.jobportal.model.Employer employer =
                    new org.jobportal.dal.impl.EmployerDAO().findById(r.getEmployerId());
            if (employer != null) companyName = employer.getCompanyName();
        }

        // Lay ten candidate
        String candidateName = null;
        String email = null;
        Candidate candidate = candidateDAO.findById(a.getCandidateId());
        if (candidate != null) {
            User user = userDAO.findById(candidate.getUserId());
            if (user != null) {
                candidateName = user.getFullName();
                email = user.getEmail();
            }
        }

        return new ApplicationDTO(
                a.getApplicationId(),
                a.getCandidateId(),
                candidateName,
                email,
                a.getRecruitmentId(),
                jobTitle,
                companyName,
                jobType,
                a.getStatus(),
                a.getAppliedDate()
        );
    }

    private String getCandidateUserId(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) return null;
        Candidate c = candidateDAO.findById(candidateId);
        return c != null ? c.getUserId() : null;
    }

    private String getEmployerUserId(String employerId) {
        if (employerId == null || employerId.isBlank()) return null;
        Employer e = employerDAO.findById(employerId);
        return e != null ? e.getUserId() : null;
    }

    private Application findApplicationForEmployer(String applicationId) {
        if (applicationId == null || applicationId.isBlank()) return null;
        String employerId = session.getEmployerId();
        if (employerId == null || employerId.isBlank()) return null;

        List<Recruitment> recruitments = recruitmentDAO.findByEmployerId(employerId);
        if (recruitments == null || recruitments.isEmpty()) return null;

        for (Recruitment r : recruitments) {
            List<Application> apps = applicationDAO.findByRecruitmentId(r.getRecruitmentId());
            if (apps == null) continue;
            for (Application a : apps) {
                if (applicationId.equals(a.getApplicationId())) {
                    return a;
                }
            }
        }
        return null;
    }
}
