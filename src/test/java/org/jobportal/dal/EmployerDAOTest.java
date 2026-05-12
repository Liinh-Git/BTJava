package org.jobportal.dal;

import org.jobportal.dal.impl.EmployerDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IEmployerDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.enums.Role;
import org.jobportal.model.Employer;
import org.jobportal.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployerDAOTest extends DalIntegrationTestBase {

    private final IEmployerDAO employerDAO = new EmployerDAO();
    private final IUserDAO userDAO = new UserDAO();

    @Test
    void testFindMethodsWithSelfCreatedData() {
        String userId = newUserId();
        User user = newUser(userId, "emp" + userId.substring(2).toLowerCase(), "emp" + userId.substring(2).toLowerCase() + "@mail.com", Role.EMPLOYER);
        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        String employerId = newEmployerId();
        Employer employer = new Employer(employerId, userId, "TEST CO", "ADDR", "DESC");
        assertTrue(employerDAO.insert(employer));
        registerCleanupId("employers", employerId);

        Employer byId = employerDAO.findById(employerId);
        assertNotNull(byId);
        assertEquals(userId, byId.getUserId());

        Employer byUser = employerDAO.findByUserId(userId);
        assertNotNull(byUser);
        assertEquals(employerId, byUser.getEmployerId());
    }

    @Test
    void testInsertUpdateDelete() {
        String userId = newUserId();
        User user = newUser(userId, "empupd" + userId.substring(2).toLowerCase(), "empupd" + userId.substring(2).toLowerCase() + "@mail.com", Role.EMPLOYER);
        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        String employerId = newEmployerId();
        Employer employer = new Employer(employerId, userId, "TEST CO", "ADDR", "DESC");
        assertTrue(employerDAO.insert(employer));
        registerCleanupId("employers", employerId);

        employer.setCompanyName("TEST CO UPDATED");
        assertTrue(employerDAO.update(employer));
        assertEquals("TEST CO UPDATED", employerDAO.findById(employerId).getCompanyName());

        assertTrue(employerDAO.delete(employerId));
        assertNull(employerDAO.findById(employerId));
    }
}
