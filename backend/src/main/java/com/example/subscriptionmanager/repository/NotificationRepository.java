package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
