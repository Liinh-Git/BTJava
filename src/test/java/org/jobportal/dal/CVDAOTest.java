package org.jobportal.dal;

import java.time.LocalDateTime;
import org.jobportal.dal.impl.CVDAO;
import org.jobportal.dal.impl.CandidateDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.ICVDAO;
import org.jobportal.dal.interfaces.ICandidateDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.enums.Role;
import org.jobportal.model.CV;
import org.jobportal.model.Candidate;
import org.jobportal.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CVDAOTest extends DalIntegrationTestBase {

    private final ICVDAO cvDAO = new CVDAO();
    private final ICandidateDAO candidateDAO = new CandidateDAO();
    private final IUserDAO userDAO = new UserDAO();

    @Test
    void testFindByCandidateIdWithSelfCreatedData() {
        String userId = newUserId();
        User user = newUser(userId, "cvcand" + userId.substring(2).toLowerCase(), "cvcand" + userId.substring(2).toLowerCase() + "@mail.com", Role.CANDIDATE);
        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        String candidateId = newCandidateId();
        assertTrue(candidateDAO.insert(new Candidate(candidateId, userId)));
        registerCleanupId("candidates", candidateId);

        String cvId = newCvId();
        CV cv = new CV(cvId, candidateId, "TEST OBJ", "JAVA", "DEV", 1000.0, LocalDateTime.now());
        assertTrue(cvDAO.insert(cv));
        registerCleanupId("cvs", cvId);

        CV reloaded = cvDAO.findByCandidateId(candidateId);
        assertNotNull(reloaded);
        assertEquals(cvId, reloaded.getCvId());
    }

    @Test
    void testInsertAndUpdate() {
        String userId = newUserId();
        User user = newUser(userId, "cvupd" + userId.substring(2).toLowerCase(), "cvupd" + userId.substring(2).toLowerCase() + "@mail.com", Role.CANDIDATE);
        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        String candidateId = newCandidateId();
        assertTrue(candidateDAO.insert(new Candidate(candidateId, userId)));
        registerCleanupId("candidates", candidateId);

        String cvId = newCvId();
        CV cv = new CV(cvId, candidateId, "TEST OBJ", "JAVA", "DEV", 1000.0, LocalDateTime.now());
        assertTrue(cvDAO.insert(cv));
        registerCleanupId("cvs", cvId);

        cv.setObjective("TEST OBJ UPDATED");
        assertTrue(cvDAO.update(cv));
        assertEquals("TEST OBJ UPDATED", cvDAO.findByCandidateId(candidateId).getObjective());
    }
}
