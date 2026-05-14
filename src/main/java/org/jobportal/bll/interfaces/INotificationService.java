package org.jobportal.bll.interfaces;

import java.util.List;
import org.jobportal.dto.NotificationDTO;

public interface INotificationService {

    // Chức năng: Lấy danh sách thông báo theo user.
    // Đầu vào: userId (String).
    // Đầu ra: List<NotificationDTO>.
    List<NotificationDTO> getNotifications(String userId);
    // Chức năng: Gửi thông báo.
    // Đầu vào: senderId (String), receiverId (String), content (String).
    // Đầu ra: boolean.
    boolean sendNotification(String senderId, String receiverId, String content);

    // Chức năng: Đánh dấu thông báo đã đọc.
    // Đầu vào: notificationId (String).
    // Đầu ra: boolean.
    boolean markAsRead(String notificationId);
}
