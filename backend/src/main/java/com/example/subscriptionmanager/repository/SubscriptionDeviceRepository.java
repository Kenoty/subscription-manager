package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.SubscriptionDevice;
import com.example.subscriptionmanager.entity.SubscriptionDeviceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionDeviceRepository extends JpaRepository<SubscriptionDevice, SubscriptionDeviceId> {
    List<SubscriptionDevice> findBySubscriptionId(Integer subscriptionId);
    List<SubscriptionDevice> findByDeviceId(UUID deviceId);
}
