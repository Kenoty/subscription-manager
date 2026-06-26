package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.response.NotificationResponse;
import com.example.subscriptionmanager.entity.Notification;
import com.example.subscriptionmanager.entity.User;
import com.example.subscriptionmanager.exception.ResourceNotFoundException;
import com.example.subscriptionmanager.mapper.NotificationMapper;
import com.example.subscriptionmanager.repository.NotificationRepository;
import com.example.subscriptionmanager.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CurrentUserProvider currentUserProvider;

    public List<NotificationResponse> getMyNotifications() {
        return notificationRepository.findByUserId(currentUserProvider.getCurrentUserId())
                .stream().map(notificationMapper::toResponse).toList();
    }

    public NotificationResponse markAsRead(Integer id) {
        Notification notification = notificationRepository
                .findByIdAndUserId(id, currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setIsUnread(false);
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }

    public void createForUser(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }
}
