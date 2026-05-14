package org.jobportal.bll.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jobportal.bll.interfaces.INotificationService;
import org.jobportal.dal.impl.NotificationDAO;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.INotificationDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.dto.NotificationDTO;
import org.jobportal.model.Notification;
import org.jobportal.model.User;
import org.jobportal.utils.SessionManager;

public class NotificationService implements INotificationService {

    private final INotificationDAO notificationDAO = new NotificationDAO();
    private final IUserDAO userDAO = new UserDAO();
    private final SessionManager session = SessionManager.getInstance();

    @Override
    public List<NotificationDTO> getNotifications(String userId) {
        if (userId == null || userId.isBlank()) return new ArrayList<>();

        List<Notification> list = notificationDAO.findByReceiverId(userId);
        List<NotificationDTO> result = new ArrayList<>();
        for (Notification n : list) {
            result.add(mapToDTO(n));
        }
        return result;
    }

    @Override
    public boolean sendNotification(String senderId, String receiverId, String content) {
        if (receiverId == null || receiverId.isBlank()) return false;
        if (content == null || content.isBlank()) return false;

        String resolvedSenderId = senderId;
        if (resolvedSenderId == null || resolvedSenderId.isBlank()) {
            resolvedSenderId = session.getCurrentUserId();
        }
        if (resolvedSenderId == null || resolvedSenderId.isBlank()) {
            return false;
        }

        String notificationId = generateNotificationId();
        Notification notification = new Notification(
                notificationId,
                resolvedSenderId,
                receiverId,
                content.trim(),
                LocalDateTime.now(),
                false
        );

        return notificationDAO.insert(notification);
    }

    @Override
    public boolean markAsRead(String notificationId) {
        if (notificationId == null || notificationId.isBlank()) return false;
        return notificationDAO.updateIsRead(notificationId, true);
    }

    private NotificationDTO mapToDTO(Notification n) {
        String senderName = "System";
        if (n.getSenderId() != null && !n.getSenderId().isBlank()) {
            User sender = userDAO.findById(n.getSenderId());
            if (sender != null && sender.getFullName() != null && !sender.getFullName().isBlank()) {
                senderName = sender.getFullName();
            } else if (sender != null && sender.getUsername() != null && !sender.getUsername().isBlank()) {
                senderName = sender.getUsername();
            } else {
                senderName = n.getSenderId();
            }
        }

        return new NotificationDTO(
                n.getNotificationId(),
                n.getSenderId(),
                senderName,
                n.getReceiverId(),
                n.getContent(),
                n.getDateTime(),
                n.isRead()
        );
    }

    private String generateNotificationId() {
        long ts = System.currentTimeMillis() % 1_000_000L;
        return String.format("NOT-%06d", ts);
    }
}
