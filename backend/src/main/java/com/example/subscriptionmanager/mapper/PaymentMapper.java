package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.PaymentResponse;
import com.example.subscriptionmanager.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setPaidAt(payment.getPaidAt());
        response.setSubscriptionId(payment.getSubscriptionEvent().getSubscription().getId());
        return response;
    }
}
