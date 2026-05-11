package org.jobportal.dal;

import org.jobportal.dal.impl.CandidateDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.ICandidateDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.enums.Role;
import org.jobportal.model.Candidate;
import org.jobportal.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CandidateDAOTest extends DalIntegrationTestBase {

    private final ICandidateDAO candidateDAO = new CandidateDAO();
    private final IUserDAO userDAO = new UserDAO();

    @Test
    void testFindMethodsWithSelfCreatedData() {
        String userId = newUserId();
        User user = newUser(userId, "cand" + userId.substring(2).toLowerCase(), "cand" + userId.substring(2).toLowerCase() + "@mail.com", Role.CANDIDATE);
        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        String candidateId = newCandidateId();
        Candidate c = new Candidate(candidateId, userId);
        assertTrue(candidateDAO.insert(c));
        registerCleanupId("candidates", candidateId);

        Candidate byId = candidateDAO.findById(candidateId);
        assertNotNull(byId);
        assertEquals(userId, byId.getUserId());

        Candidate byUserId = candidateDAO.findByUserId(userId);
        assertNotNull(byUserId);
        assertEquals(candidateId, byUserId.getCandidateId());
    }

    @Test
    void testInsertAndDelete() {
        String userId = newUserId();
        User user = newUser(userId, "canddel" + userId.substring(2).toLowerCase(), "canddel" + userId.substring(2).toLowerCase() + "@mail.com", Role.CANDIDATE);
        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        String candidateId = newCandidateId();
        Candidate c = new Candidate(candidateId, userId);
        assertTrue(candidateDAO.insert(c));
        registerCleanupId("candidates", candidateId);

        assertTrue(candidateDAO.delete(candidateId));
        assertNull(candidateDAO.findById(candidateId));
    }
}
