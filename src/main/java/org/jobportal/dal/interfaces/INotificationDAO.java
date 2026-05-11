package org.jobportal.dal.interfaces;

import java.util.List;
import org.jobportal.model.Notification;

public interface INotificationDAO {
    // LƯU Ý ĐẶC BIỆT: Chức năng Notification CHƯA THỰC HIỆN IMPLEMENT ở giai đoạn
    // này.
    // Hiện tại chỉ tạo prototype/skeleton và TODO để dev triển khai sau.
    // Không viết logic insert/update/select thật và không nối Notification vào flow
    // xử lý chính ở task này.
    // Chức năng: Lấy danh sách thông báo theo receiver
    // Đầu vào: receiverId (String)
    // Đầu ra: List<Notification>
    List<Notification> findByReceiverId(String receiverId);

    // LƯU Ý ĐẶC BIỆT: Chức năng Notification CHƯA THỰC HIỆN IMPLEMENT ở giai đoạn
    // này.
    // Hiện tại chỉ tạo prototype/skeleton và TODO để dev triển khai sau.
    // Không viết logic insert/update/select thật và không nối Notification vào flow
    // xử lý chính ở task này.
    // Chức năng: Thêm thông báo
    // Đầu vào: notification (Notification)
    // Đầu ra: boolean
    boolean insert(Notification notification);

    // LƯU Ý ĐẶC BIỆT: Chức năng Notification CHƯA THỰC HIỆN IMPLEMENT ở giai đoạn
    // này.
    // Hiện tại chỉ tạo prototype/skeleton và TODO để dev triển khai sau.
    // Không viết logic insert/update/select thật và không nối Notification vào flow
    // xử lý chính ở task này.
    // Chức năng: Cập nhật trạng thái đã đọc
    // Đầu vào: notificationId (String), isRead (boolean)
    // Đầu ra: boolean
    boolean updateIsRead(String notificationId, boolean isRead);
}
