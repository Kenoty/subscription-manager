package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.NotificationResponse;
import com.example.subscriptionmanager.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setMessage(notification.getMessage());
        response.setIsUnread(notification.getIsUnread());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
