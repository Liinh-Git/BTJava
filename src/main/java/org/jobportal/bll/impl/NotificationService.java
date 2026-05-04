package org.jobportal.bll.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.dto.NotificationDTO;

public class NotificationService {
    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Lay danh sach thong bao theo user
    // Dau vao: userId (String) - ma user
    // Dau ra: List<NotificationDTO> - danh sach thong bao
    // Tuong tac: Duoc goi tu HeaderPanel; se goi NotificationDAO
    // Ghi chu: Sap xep giam dan theo thoi gian
    public List<NotificationDTO> getNotifications(String userId) {
        // TODO: Buoc 1 - Truy van danh sach thong bao theo userId
        // TODO: Buoc 2 - Sap xep va map sang DTO
        // TODO: Buoc 3 - Tra ve danh sach
        return Collections.emptyList();
    }

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Gui thong bao
    // Dau vao: senderId (String) - nguoi gui; receiverId (String) - nguoi nhan; content (String) - noi dung
    // Dau ra: boolean - true neu gui thanh cong
    // Tuong tac: Duoc goi tu ApplicationService/RecruitmentService; se goi NotificationDAO
    // Ghi chu: Tao notificationId va thoi gian gui
    public boolean sendNotification(String senderId, String receiverId, String content) {
        // TODO: Buoc 1 - Tao doi tuong Notification
        // TODO: Buoc 2 - Luu Notification vao DB
        // TODO: Buoc 3 - Tra ve ket qua
        return false;
    }

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Danh dau thong bao da doc
    // Dau vao: notificationId (String) - ma thong bao
    // Dau ra: boolean - true neu cap nhat thanh cong
    // Tuong tac: Duoc goi tu HeaderPanel; se goi NotificationDAO
    // Ghi chu: Chi cap nhat is_read
    public boolean markAsRead(String notificationId) {
        // TODO: Buoc 1 - Goi NotificationDAO.updateIsRead
        // TODO: Buoc 2 - Xu ly ket qua
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }
}
