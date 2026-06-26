package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.BillingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingPeriodRepository extends JpaRepository<BillingPeriod, Integer> {
    Optional<BillingPeriod> findByName(String name);
}
