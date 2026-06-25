package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.BillingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingPeriodRepository extends JpaRepository<BillingPeriod, Integer> {
}
