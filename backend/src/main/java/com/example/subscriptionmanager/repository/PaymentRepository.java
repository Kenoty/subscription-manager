package com.example.subscriptionmanager.repository;

import com.example.subscriptionmanager.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("SELECT p FROM Payment p WHERE p.subscriptionEvent.subscription.user.id = :userId")
    List<Payment> findByUserId(@Param("userId") Integer userId);
}
