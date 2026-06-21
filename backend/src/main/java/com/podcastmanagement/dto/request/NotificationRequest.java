package com.podcastmanagement.dto.request;

public class NotificationRequest {

    private String title;

    private String message;

    private Long userId;

    private Long senderId;

    public NotificationRequest() {}

    public NotificationRequest(String title, String message, Long userId, Long senderId) {
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.senderId = senderId;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
}
