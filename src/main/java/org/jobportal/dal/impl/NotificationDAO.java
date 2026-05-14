package org.jobportal.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jobportal.config.DatabaseConfig;
import org.jobportal.dal.interfaces.INotificationDAO;
import org.jobportal.model.Notification;

public class NotificationDAO implements INotificationDAO {

    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setNotificationId(rs.getString("notification_id"));
        n.setSenderId(rs.getString("sender_id"));
        n.setReceiverId(rs.getString("receiver_id"));
        n.setContent(rs.getString("content"));
        java.sql.Timestamp ts = rs.getTimestamp("date_time");
        if (ts != null) {
            n.setDateTime(ts.toLocalDateTime());
        }
        n.setRead(rs.getBoolean("is_read"));
        return n;
    }

    @Override
    public List<Notification> findByReceiverId(String receiverId) {
        String sql = "SELECT notification_id, sender_id, receiver_id, content, date_time, is_read "
                + "FROM notifications WHERE receiver_id = ? ORDER BY date_time DESC";

        List<Notification> result = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, receiverId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi lay notifications theo receiverId: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean insert(Notification notification) {
        String sql = "INSERT INTO notifications (notification_id, sender_id, receiver_id, content, date_time, is_read) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, notification.getNotificationId());
            if (notification.getSenderId() != null && !notification.getSenderId().isBlank()) {
                ps.setString(2, notification.getSenderId());
            } else {
                ps.setNull(2, java.sql.Types.VARCHAR);
            }
            ps.setString(3, notification.getReceiverId());
            ps.setString(4, notification.getContent());
            if (notification.getDateTime() != null) {
                ps.setTimestamp(5, java.sql.Timestamp.valueOf(notification.getDateTime()));
            } else {
                ps.setNull(5, java.sql.Types.TIMESTAMP);
            }
            ps.setBoolean(6, notification.isRead());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi insert notification: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateIsRead(String notificationId, boolean isRead) {
        String sql = "UPDATE notifications SET is_read = ? WHERE notification_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isRead);
            ps.setString(2, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi update is_read notification: " + e.getMessage(), e);
        }
    }
}
