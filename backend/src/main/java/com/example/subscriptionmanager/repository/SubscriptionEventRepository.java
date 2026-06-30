package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.SubscriptionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEvent, Integer> {
    List<SubscriptionEvent> findBySubscriptionId(Integer subscriptionId);

    @Modifying
    @Query("DELETE FROM SubscriptionEvent se WHERE se.subscription.id = :subscriptionId")
    void deleteBySubscriptionId(Integer subscriptionId);
}
