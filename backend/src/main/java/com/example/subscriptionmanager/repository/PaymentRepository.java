package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
