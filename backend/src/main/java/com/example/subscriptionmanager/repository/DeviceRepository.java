package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    @Query("SELECT sd.device FROM SubscriptionDevice sd WHERE sd.subscription.user.id = :userId AND sd.removedAt IS NULL")
    List<Device> findActiveDevicesByUserId(@Param("userId") Integer userId);
}
