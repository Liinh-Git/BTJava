package org.jobportal.dto;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String notificationId;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String content;
    private LocalDateTime dateTime;
    private boolean read;

    public NotificationDTO() {
    }

    public NotificationDTO(String notificationId, String senderId, String senderName, String receiverId,
                           String content, LocalDateTime dateTime, boolean read) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.content = content;
        this.dateTime = dateTime;
        this.read = read;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
