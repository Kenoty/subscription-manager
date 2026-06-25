package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
}
