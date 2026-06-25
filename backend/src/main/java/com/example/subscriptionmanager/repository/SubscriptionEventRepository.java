package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.SubscriptionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEvent, Integer> {
}
