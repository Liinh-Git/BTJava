package org.jobportal.dal;

import java.time.LocalDateTime;
import org.jobportal.dal.impl.ApplicationDAO;
import org.jobportal.dal.impl.CandidateDAO;
import org.jobportal.dal.impl.CategoryDAO;
import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.RecruitmentDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IApplicationDAO;
import org.jobportal.dal.interfaces.ICandidateDAO;
import org.jobportal.dal.interfaces.ICategoryDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.IRecruitmentDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.ApplicationStatus;
import org.jobportal.enums.JobType;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.enums.Role;
import org.jobportal.model.Application;
import org.jobportal.model.Candidate;
import org.jobportal.model.Category;
import org.jobportal.model.Employer;
import org.jobportal.model.Recruitment;
import org.jobportal.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationDAOTest extends DalIntegrationTestBase {

    private final IApplicationDAO applicationDAO = new ApplicationDAO();
    private final IUserDAO userDAO = new UserDAO();
    private final ICandidateDAO candidateDAO = new CandidateDAO();
    private final IEmployerDAO employerDAO = new EmployerDAO();
    private final ICategoryDAO categoryDAO = new CategoryDAO();
    private final IRecruitmentDAO recruitmentDAO = new RecruitmentDAO();

    @Test
    void testReadMethodsWithSelfCreatedData() {
        String candidateUserId = newUserId();
        User candidateUser = newUser(candidateUserId, "appcand" + candidateUserId.substring(2).toLowerCase(), "appcand" + candidateUserId.substring(2).toLowerCase() + "@mail.com", Role.CANDIDATE);
        assertTrue(userDAO.insert(candidateUser));
        registerCleanupId("users", candidateUserId);

        String candidateId = newCandidateId();
        assertTrue(candidateDAO.insert(new Candidate(candidateId, candidateUserId)));
        registerCleanupId("candidates", candidateId);

        String employerUserId = newUserId();
        User employerUser = newUser(employerUserId, "appemp" + employerUserId.substring(2).toLowerCase(), "appemp" + employerUserId.substring(2).toLowerCase() + "@mail.com", Role.EMPLOYER);
        assertTrue(userDAO.insert(employerUser));
        registerCleanupId("users", employerUserId);

        String employerId = newEmployerId();
        assertTrue(employerDAO.insert(new Employer(employerId, employerUserId, "APP CO", "ADDR", "DESC")));
        registerCleanupId("employers", employerId);

        String categoryId = newCategoryId();
        assertTrue(categoryDAO.insert(new Category(categoryId, "APP CAT " + categoryId.substring(4))));
        registerCleanupId("categories", categoryId);

        String recruitmentId = newRecruitmentId();
        Recruitment recruitment = new Recruitment(recruitmentId, employerId, categoryId, "APP RECRUIT", "DESC",
                JobType.FULLTIME, RecruitmentStatus.OPEN, AdminStatus.APPROVED,
                2000.0, "HN", "1 year", LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        assertTrue(recruitmentDAO.insert(recruitment));
        registerCleanupId("recruitments", recruitmentId);

        String appId = newApplicationId();
        Application application = new Application(appId, candidateId, recruitmentId, ApplicationStatus.PENDING, LocalDateTime.now());
        assertTrue(applicationDAO.insert(application));
        registerCleanupId("applications", appId);

        assertTrue(applicationDAO.findByCandidateId(candidateId).stream().anyMatch(a -> appId.equals(a.getApplicationId())));
        assertTrue(applicationDAO.findByRecruitmentId(recruitmentId).stream().anyMatch(a -> appId.equals(a.getApplicationId())));
        assertTrue(applicationDAO.findByRecruitmentIdAndStatus(recruitmentId, ApplicationStatus.PENDING).stream().anyMatch(a -> appId.equals(a.getApplicationId())));
        assertTrue(applicationDAO.existsByCandidateAndRecruitment(candidateId, recruitmentId));
        assertTrue(applicationDAO.countByCandidateId(candidateId) >= 1);
        assertTrue(applicationDAO.countByCandidateIdAndStatus(candidateId, ApplicationStatus.PENDING) >= 1);
        assertTrue(applicationDAO.countByEmployerId(employerId) >= 1);
        assertTrue(applicationDAO.countNewApplicantsToday(employerId) >= 1);
    }

    @Test
    void testInsertUpdateDelete() {
        String candidateUserId = newUserId();
        User candidateUser = newUser(candidateUserId, "appcand2" + candidateUserId.substring(2).toLowerCase(), "appcand2" + candidateUserId.substring(2).toLowerCase() + "@mail.com", Role.CANDIDATE);
        assertTrue(userDAO.insert(candidateUser));
        registerCleanupId("users", candidateUserId);

        String candidateId = newCandidateId();
        assertTrue(candidateDAO.insert(new Candidate(candidateId, candidateUserId)));
        registerCleanupId("candidates", candidateId);

        String employerUserId = newUserId();
        User employerUser = newUser(employerUserId, "appemp2" + employerUserId.substring(2).toLowerCase(), "appemp2" + employerUserId.substring(2).toLowerCase() + "@mail.com", Role.EMPLOYER);
        assertTrue(userDAO.insert(employerUser));
        registerCleanupId("users", employerUserId);

        String employerId = newEmployerId();
        assertTrue(employerDAO.insert(new Employer(employerId, employerUserId, "APP CO 2", "ADDR", "DESC")));
        registerCleanupId("employers", employerId);

        String categoryId = newCategoryId();
        assertTrue(categoryDAO.insert(new Category(categoryId, "APP CAT 2 " + categoryId.substring(4))));
        registerCleanupId("categories", categoryId);

        String recruitmentId = newRecruitmentId();
        Recruitment recruitment = new Recruitment(recruitmentId, employerId, categoryId, "APP RECRUIT 2", "DESC",
                JobType.FULLTIME, RecruitmentStatus.OPEN, AdminStatus.APPROVED,
                2000.0, "HN", "1 year", LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        assertTrue(recruitmentDAO.insert(recruitment));
        registerCleanupId("recruitments", recruitmentId);

        String appId = newApplicationId();
        Application application = new Application(appId, candidateId, recruitmentId, ApplicationStatus.PENDING, LocalDateTime.now());
        assertTrue(applicationDAO.insert(application));
        registerCleanupId("applications", appId);

        assertTrue(applicationDAO.updateStatus(appId, ApplicationStatus.REJECTED));
        assertTrue(applicationDAO.delete(appId));
        assertFalse(applicationDAO.existsByCandidateAndRecruitment(candidateId, recruitmentId));
    }
}
