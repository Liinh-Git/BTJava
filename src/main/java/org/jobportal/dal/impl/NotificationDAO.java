package org.jobportal.dal.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.model.Notification;

public class NotificationDAO {
    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Lay danh sach thong bao theo receiver
    // Dau vao: receiverId (String) - ma nguoi nhan
    // Dau ra: List<Notification> - danh sach thong bao
    // Tuong tac: Duoc goi tu NotificationService; se dung JDBC
    // Ghi chu: Sap xep giam dan theo thoi gian
    public List<Notification> findByReceiverId(String receiverId) {
        // TODO: Buoc 1 - Query SELECT theo receiverId
        // TODO: Buoc 2 - Map ResultSet sang list Notification
        // TODO: Buoc 3 - Tra ve danh sach
        return Collections.emptyList();
    }

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Them thong bao
    // Dau vao: notification (Notification) - thong bao can them
    // Dau ra: boolean - true neu insert thanh cong
    // Tuong tac: Duoc goi tu NotificationService; se dung JDBC
    // Ghi chu: Tao notificationId va thoi gian gui
    public boolean insert(Notification notification) {
        // TODO: Buoc 1 - Tao cau lenh INSERT notification
        // TODO: Buoc 2 - Thuc thi va lay ket qua
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }

    // LUU Y DAC BIET: Chuc nang Notification CHUA THUC HIEN IMPLEMENT o giai doan nay.
    // Hien tai chi tao prototype/skeleton va TODO de dev trien khai sau.
    // Khong viet logic insert/update/select that va khong noi Notification vao flow xu ly chinh o task nay.
    // Chuc nang: Cap nhat trang thai da doc
    // Dau vao: notificationId (String) - ma thong bao; isRead (boolean) - trang thai
    // Dau ra: boolean - true neu cap nhat thanh cong
    // Tuong tac: Duoc goi tu NotificationService; se dung JDBC
    // Ghi chu: Update cot is_read
    public boolean updateIsRead(String notificationId, boolean isRead) {
        // TODO: Buoc 1 - Tao cau lenh UPDATE is_read
        // TODO: Buoc 2 - Thuc thi va lay ket qua
        // TODO: Buoc 3 - Tra ve boolean
        return false;
    }
}
