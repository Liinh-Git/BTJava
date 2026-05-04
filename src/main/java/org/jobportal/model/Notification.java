package org.jobportal.model;

import java.time.LocalDateTime;

public class Notification {
    private String notificationId;
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime dateTime;
    private boolean read;

    public Notification() {
    }

    public Notification(String notificationId, String senderId, String receiverId, String content,
                        LocalDateTime dateTime, boolean read) {
        this.notificationId = notificationId;
        this.senderId = senderId;
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
