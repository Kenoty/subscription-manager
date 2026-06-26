package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.response.PaymentResponse;
import com.example.subscriptionmanager.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "История платежей")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Получить историю платежей")
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(paymentService.getMyPayments());
    }
}
