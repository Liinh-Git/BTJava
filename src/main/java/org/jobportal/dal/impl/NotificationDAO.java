package org.jobportal.dal.impl;

import java.util.Collections;
import java.util.List;
import org.jobportal.dal.interfaces.INotificationDAO;
import org.jobportal.model.Notification;

public class NotificationDAO implements INotificationDAO {

    // LƯU Ý ĐẶC BIỆT: Chức năng Notification CHƯA THỰC HIỆN IMPLEMENT ở giai đoạn này.
    // Hiện tại chỉ tạo prototype/skeleton và TODO để dev triển khai sau.
    // Không viết logic insert/update/select thật và không nối Notification vào flow xử lý chính ở task này.

    // Chức năng: Lấy danh sách thông báo theo receiver
    // Đầu vào: receiverId (String) - mã người nhận
    // Đầu ra: List<Notification> - danh sách thông báo
    // Tương tác: Được gọi từ NotificationService; sẽ dùng JDBC
    // Ghi chú: Sắp xếp giảm dần theo thời gian
    @Override
    public List<Notification> findByReceiverId(String receiverId) {
        // TODO: Bước 1 - Query SELECT theo receiverId
        // TODO: Bước 2 - Map ResultSet sang list Notification
        // TODO: Bước 3 - Trả về danh sách
        return Collections.emptyList();
    }

    // LƯU Ý ĐẶC BIỆT: Chức năng Notification CHƯA THỰC HIỆN IMPLEMENT ở giai đoạn này.
    // Hiện tại chỉ tạo prototype/skeleton và TODO để dev triển khai sau.
    // Không viết logic insert/update/select thật và không nối Notification vào flow xử lý chính ở task này.

    // Chức năng: Thêm thông báo
    // Đầu vào: notification (Notification) - thông báo cần thêm
    // Đầu ra: boolean - true nếu insert thành công
    // Tương tác: Được gọi từ NotificationService; sẽ dùng JDBC
    // Ghi chú: Tạo notificationId và thời gian gửi
    @Override
    public boolean insert(Notification notification) {
        // TODO: Bước 1 - Tạo câu lệnh INSERT notification
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }

    // LƯU Ý ĐẶC BIỆT: Chức năng Notification CHƯA THỰC HIỆN IMPLEMENT ở giai đoạn này.
    // Hiện tại chỉ tạo prototype/skeleton và TODO để dev triển khai sau.
    // Không viết logic insert/update/select thật và không nối Notification vào flow xử lý chính ở task này.

    // Chức năng: Cập nhật trạng thái đã đọc
    // Đầu vào: notificationId (String) - mã thông báo; isRead (boolean) - trạng thái
    // Đầu ra: boolean - true nếu cập nhật thành công
    // Tương tác: Được gọi từ NotificationService; sẽ dùng JDBC
    // Ghi chú: Update cột is_read
    @Override
    public boolean updateIsRead(String notificationId, boolean isRead) {
        // TODO: Bước 1 - Tạo câu lệnh UPDATE is_read
        // TODO: Bước 2 - Thực thi và lấy kết quả
        // TODO: Bước 3 - Trả về boolean
        return false;
    }
}
