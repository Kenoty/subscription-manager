package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserId(Integer userId);
    Optional<Notification> findByIdAndUserId(Integer id, Integer userId);
}
