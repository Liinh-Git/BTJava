package org.jobportal.bll.interfaces;

import java.util.List;
import org.jobportal.dto.NotificationDTO;

public interface INotificationService {
    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Lay danh sach thong bao theo user
    // Dau vao: userId (String)
    // Dau ra: List<NotificationDTO>
    List<NotificationDTO> getNotifications(String userId);

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Gui thong bao
    // Dau vao: senderId (String), receiverId (String), content (String)
    // Dau ra: boolean
    boolean sendNotification(String senderId, String receiverId, String content);

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Danh dau thong bao da doc
    // Dau vao: notificationId (String)
    // Dau ra: boolean
    boolean markAsRead(String notificationId);
}
