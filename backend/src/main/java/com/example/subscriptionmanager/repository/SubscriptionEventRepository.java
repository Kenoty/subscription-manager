package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.SubscriptionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEvent, Integer> {
    List<SubscriptionEvent> findBySubscriptionId(Integer subscriptionId);
}
