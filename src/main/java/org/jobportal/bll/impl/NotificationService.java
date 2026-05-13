package org.jobportal.bll.impl;

import org.jobportal.bll.interfaces.INotificationService;
import org.jobportal.dal.impl.UserDAO;
import org.jobportal.dal.interfaces.IUserDAO;
import org.jobportal.dto.NotificationDTO;
import org.jobportal.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationService implements INotificationService {

    private static final List<NotificationDTO> STORE = new CopyOnWriteArrayList<>();
    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final IUserDAO userDAO = new UserDAO();

    @Override
    public List<NotificationDTO> getNotifications(String userId) {
        if (userId == null || userId.isBlank()) return new ArrayList<>();

        List<NotificationDTO> result = new ArrayList<>();
        for (NotificationDTO n : STORE) {
            if (userId.equals(n.getReceiverId())) {
                result.add(n);
            }
        }

        result.sort(Comparator.comparing(NotificationDTO::getDateTime,
                Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        return result;
    }

    @Override
    public boolean sendNotification(String senderId, String receiverId, String content) {
        if (receiverId == null || receiverId.isBlank()) return false;
        if (content == null || content.isBlank()) return false;

        String senderName = "System";
        if (senderId != null && !senderId.isBlank()) {
            User sender = userDAO.findById(senderId);
            if (sender != null && sender.getFullName() != null && !sender.getFullName().isBlank()) {
                senderName = sender.getFullName();
            } else if (sender != null && sender.getUsername() != null && !sender.getUsername().isBlank()) {
                senderName = sender.getUsername();
            } else {
                senderName = senderId;
            }
        }

        String notificationId = String.format("NOT-%d-%04d", System.currentTimeMillis(), COUNTER.incrementAndGet());
        NotificationDTO dto = new NotificationDTO(
                notificationId,
                senderId,
                senderName,
                receiverId,
                content.trim(),
                LocalDateTime.now(),
                false
        );
        STORE.add(dto);
        return true;
    }

    @Override
    public boolean markAsRead(String notificationId) {
        if (notificationId == null || notificationId.isBlank()) return false;
        for (NotificationDTO n : STORE) {
            if (notificationId.equals(n.getNotificationId())) {
                n.setRead(true);
                return true;
            }
        }
        return false;
    }
}
