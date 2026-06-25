package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.SubscriptionDevice;
import com.example.subscriptionmanager.entity.SubscriptionDeviceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionDeviceRepository extends JpaRepository<SubscriptionDevice, SubscriptionDeviceId> {
}
