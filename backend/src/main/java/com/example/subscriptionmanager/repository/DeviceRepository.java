package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
