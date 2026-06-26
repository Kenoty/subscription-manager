package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findByServiceId(Integer serviceId);
}
