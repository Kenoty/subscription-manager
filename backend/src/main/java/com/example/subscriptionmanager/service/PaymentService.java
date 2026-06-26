package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.response.PaymentResponse;
import com.example.subscriptionmanager.mapper.PaymentMapper;
import com.example.subscriptionmanager.repository.PaymentRepository;
import com.example.subscriptionmanager.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final CurrentUserProvider currentUserProvider;

    public List<PaymentResponse> getMyPayments() {
        return paymentRepository.findByUserId(currentUserProvider.getCurrentUserId())
                .stream().map(paymentMapper::toResponse).toList();
    }
}
