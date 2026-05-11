package org.jobportal.dal;

import java.util.List;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.enums.Role;
import org.jobportal.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest extends DalIntegrationTestBase {

    private final IUserDAO userDAO = new UserDAO();

    @Test
    void testReadMethodsWithSelfCreatedData() {
        String userId = newUserId();
        String username = "testu" + userId.substring(2).toLowerCase();
        String email = username + "@mail.com";

        User user = newUser(userId, username, email, Role.CANDIDATE);
        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        User byId = userDAO.findById(userId);
        assertNotNull(byId);
        assertEquals(username, byId.getUsername());

        User byLogin = userDAO.findByUsernameAndPassword(username, "MatKhau@123");
        assertNotNull(byLogin);

        List<User> roleUsers = userDAO.findAll(Role.CANDIDATE, true);
        assertTrue(roleUsers.stream().anyMatch(u -> userId.equals(u.getUserId())));

        assertTrue(userDAO.existsByUsername(username));
        assertTrue(userDAO.existsByEmail(email));
        assertTrue(userDAO.countByRole(Role.CANDIDATE) >= 1);
    }

    @Test
    void testInsertUpdateStatusPasswordDelete() {
        String userId = newUserId();
        String username = "upd" + userId.substring(2).toLowerCase();
        User user = newUser(userId, username, username + "@mail.com", Role.CANDIDATE);

        assertTrue(userDAO.insert(user));
        registerCleanupId("users", userId);

        user.setFullName("TEST Updated");
        user.setEmail("updated." + user.getEmail());
        assertTrue(userDAO.update(user));

        assertTrue(userDAO.updateStatus(userId, false));
        assertTrue(userDAO.updatePassword(userId, "NewPass@123"));

        User reloaded = userDAO.findById(userId);
        assertNotNull(reloaded);
        assertFalse(reloaded.isActive());

        assertTrue(userDAO.delete(userId));
        assertNull(userDAO.findById(userId));
    }
}
