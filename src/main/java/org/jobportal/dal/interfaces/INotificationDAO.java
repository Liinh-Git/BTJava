package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.model.Notification;

public interface INotificationDAO {
    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Lay danh sach thong bao theo receiver
    // Dau vao: receiverId (String)
    // Dau ra: List<Notification>
    List<Notification> findByReceiverId(String receiverId);

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Them thong bao
    // Dau vao: notification (Notification)
    // Dau ra: boolean
    boolean insert(Notification notification);

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Cap nhat trang thai da doc
    // Dau vao: notificationId (String), isRead (boolean)
    // Dau ra: boolean
    boolean updateIsRead(String notificationId, boolean isRead);
}
