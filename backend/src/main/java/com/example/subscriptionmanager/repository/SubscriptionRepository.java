package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findByUserId(Integer userId);
    Optional<Subscription> findByIdAndUserId(Integer id, Integer userId);
}
