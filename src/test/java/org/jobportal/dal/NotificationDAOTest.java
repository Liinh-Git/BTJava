package org.jobportal.dal;

import org.jobportal.dal.impl.NotificationDAO;
import org.jobportal.dal.interfaces.INotificationDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificationDAOTest extends DalIntegrationTestBase {

    private final INotificationDAO notificationDAO = new NotificationDAO();

    @Test
    void testSkeletonBehavior() {
        assertNotNull(notificationDAO.findByReceiverId(newUserId()));
        assertTrue(notificationDAO.findByReceiverId(newUserId()).isEmpty());
        assertFalse(notificationDAO.insert(null));
        assertFalse(notificationDAO.updateIsRead(newApplicationId(), true));
    }
}
