package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.request.CreateSubscriptionRequest;
import com.example.subscriptionmanager.dto.response.SubscriptionEventResponse;
import com.example.subscriptionmanager.dto.response.SubscriptionResponse;
import com.example.subscriptionmanager.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "Управление подписками")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    @Operation(summary = "Получить все подписки")
    public ResponseEntity<List<SubscriptionResponse>> getAll() {
        return ResponseEntity.ok(subscriptionService.getMySubscriptions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить подписку по id")
    public ResponseEntity<SubscriptionResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(subscriptionService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Создать подписку")
    public ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok(subscriptionService.create(request));
    }

    @PostMapping("/{id}/toggle-auto-renew")
    @Operation(summary = "Переключить автопродление")
    public ResponseEntity<SubscriptionResponse> toggleAutoRenew(@PathVariable Integer id) {
        return ResponseEntity.ok(subscriptionService.toggleAutoRenew(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Отменить подписку")
    public ResponseEntity<Void> cancel(@PathVariable Integer id) {
        subscriptionService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/events")
    @Operation(summary = "История событий подписки")
    public ResponseEntity<List<SubscriptionEventResponse>> getEvents(@PathVariable Integer id) {
        return ResponseEntity.ok(subscriptionService.getEvents(id));
    }
}
