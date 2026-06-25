package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}
