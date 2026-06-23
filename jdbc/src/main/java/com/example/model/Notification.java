package com.example.model;

import java.time.OffsetDateTime;

public class Notification {
    private Integer id;
    private Integer userId;
    private String message;
    private Boolean isUnread;
    private OffsetDateTime createdAt;

    public Notification(Integer userId, String message, Boolean isUnread) {
        this.userId = userId;
        this.message = message;
        this.isUnread = isUnread;
    }

    public Notification(Integer id, Integer userId, String message, Boolean isUnread, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.isUnread = isUnread;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsUnread() {
        return isUnread;
    }

    public void setIsUnread(Boolean isUnread) {
        this.isUnread = isUnread;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
