package org.jobportal.dal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.CategoryDAO;
import org.jobportal.dal.impl.RecruitmentDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.ICategoryDAO;
import org.jobportal.dal.interfaces.IRecruitmentDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.enums.AdminStatus;
import org.jobportal.enums.JobType;
import org.jobportal.enums.RecruitmentStatus;
import org.jobportal.enums.Role;
import org.jobportal.model.Category;
import org.jobportal.model.Employer;
import org.jobportal.model.Recruitment;
import org.jobportal.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecruitmentDAOTest extends DalIntegrationTestBase {

    private final IRecruitmentDAO recruitmentDAO = new RecruitmentDAO();
    private final IUserDAO userDAO = new UserDAO();
    private final IEmployerDAO employerDAO = new EmployerDAO();
    private final ICategoryDAO categoryDAO = new CategoryDAO();

    @Test
    void testReadMethodsWithSelfCreatedData() {
        String userId = newUserId();
        User employerUser = newUser(userId, "recru" + userId.substring(2).toLowerCase(), "recru" + userId.substring(2).toLowerCase() + "@mail.com", Role.EMPLOYER);
        assertTrue(userDAO.insert(employerUser));
        registerCleanupId("users", userId);

        String employerId = newEmployerId();
        assertTrue(employerDAO.insert(new Employer(employerId, userId, "TEST CO", "ADDR", "DESC")));
        registerCleanupId("employers", employerId);

        String categoryId = newCategoryId();
        assertTrue(categoryDAO.insert(new Category(categoryId, "TEST CAT " + categoryId.substring(4))));
        registerCleanupId("categories", categoryId);

        String recruitmentId = newRecruitmentId();
        Recruitment r = new Recruitment(recruitmentId, employerId, categoryId, "TEST JAVA ROLE", "DESC",
                JobType.FULLTIME, RecruitmentStatus.OPEN, AdminStatus.APPROVED,
                1200.0, "HCM", "1 year", LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        assertTrue(recruitmentDAO.insert(r));
        registerCleanupId("recruitments", recruitmentId);

        assertNotNull(recruitmentDAO.findById(recruitmentId));
        assertTrue(recruitmentDAO.findByEmployerId(employerId).stream().anyMatch(x -> recruitmentId.equals(x.getRecruitmentId())));
        assertTrue(recruitmentDAO.findByAdminStatus("APPROVED").stream().anyMatch(x -> recruitmentId.equals(x.getRecruitmentId())));
        assertTrue(recruitmentDAO.searchByKeywordAndCategory("JAVA", categoryId, 0, 10).stream().anyMatch(x -> recruitmentId.equals(x.getRecruitmentId())));
        assertTrue(recruitmentDAO.countByCategory(categoryId) >= 1);
        assertTrue(recruitmentDAO.countOpenRecruitments() >= 1);
        List<Map<String, Object>> top = recruitmentDAO.getTopEmployers();
        assertTrue(top.stream().anyMatch(row -> employerId.equals(row.get("employer_id"))));
    }

    @Test
    void testInsertUpdateAndDelete() {
        String userId = newUserId();
        User employerUser = newUser(userId, "recupd" + userId.substring(2).toLowerCase(), "recupd" + userId.substring(2).toLowerCase() + "@mail.com", Role.EMPLOYER);
        assertTrue(userDAO.insert(employerUser));
        registerCleanupId("users", userId);

        String employerId = newEmployerId();
        assertTrue(employerDAO.insert(new Employer(employerId, userId, "TEST CO", "ADDR", "DESC")));
        registerCleanupId("employers", employerId);

        String categoryId = newCategoryId();
        assertTrue(categoryDAO.insert(new Category(categoryId, "TEST CAT " + categoryId.substring(4))));
        registerCleanupId("categories", categoryId);

        String recruitmentId = newRecruitmentId();
        Recruitment r = new Recruitment(recruitmentId, employerId, categoryId, "TEST RECRUITMENT", "TEST DESC",
                JobType.FULLTIME, RecruitmentStatus.OPEN, AdminStatus.PENDING,
                12345.0, "HN", "1 year", LocalDateTime.now(), LocalDateTime.now().plusDays(10));

        assertTrue(recruitmentDAO.insert(r));
        registerCleanupId("recruitments", recruitmentId);

        r.setTitle("TEST RECRUITMENT UPDATED");
        assertTrue(recruitmentDAO.update(r));
        assertTrue(recruitmentDAO.updateStatus(recruitmentId, RecruitmentStatus.CLOSED));
        assertTrue(recruitmentDAO.updateAdminStatus(recruitmentId, "APPROVED"));

        Recruitment after = recruitmentDAO.findById(recruitmentId);
        assertNotNull(after);
        assertEquals("TEST RECRUITMENT UPDATED", after.getTitle());

        assertTrue(recruitmentDAO.delete(recruitmentId));
        assertNull(recruitmentDAO.findById(recruitmentId));
    }
}
