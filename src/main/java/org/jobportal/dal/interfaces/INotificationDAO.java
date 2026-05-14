package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.model.Notification;

public interface INotificationDAO {
    
    // Chức năng: Lấy danh sách thông báo theo receiver
    // Đầu vào: receiverId (String)
    // Đầu ra: List<Notification>
    List<Notification> findByReceiverId(String receiverId);

    // Chức năng: Thêm thông báo
    // Đầu vào: notification (Notification)
    // Đầu ra: boolean
    boolean insert(Notification notification);

    // Chức năng: Cập nhật trạng thái đã đọc
    // Đầu vào: notificationId (String), isRead (boolean)
    // Đầu ra: boolean
    boolean updateIsRead(String notificationId, boolean isRead);
}
