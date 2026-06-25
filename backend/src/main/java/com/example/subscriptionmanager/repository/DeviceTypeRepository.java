package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.DeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, Integer> {
}
