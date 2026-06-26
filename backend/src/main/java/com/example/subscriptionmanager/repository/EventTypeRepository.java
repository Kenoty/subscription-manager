package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
    Optional<EventType> findByName(String name);
}
