package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.request.*;
import com.example.subscriptionmanager.dto.response.*;
import com.example.subscriptionmanager.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Административные функции")
public class AdminController {

    private final UserService userService;
    private final ServiceCatalogService serviceCatalogService;
    private final PlanService planService;
    private final SubscriptionService subscriptionService;
    private final CatalogService catalogService;

    @GetMapping("/users")
    @Operation(summary = "Получить всех пользователей")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Получить пользователя по id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Удалить пользователя")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscriptions")
    @Operation(summary = "Получить все подписки")
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/services")
    @Operation(summary = "Получить все сервисы")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceCatalogService.getAll());
    }

    @PostMapping("/services")
    @Operation(summary = "Добавить сервис")
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody CreateServiceRequest request) {
        return ResponseEntity.ok(serviceCatalogService.create(request));
    }

    @PatchMapping("/services/{id}")
    @Operation(summary = "Обновить сервис")
    public ResponseEntity<ServiceResponse> updateService(@PathVariable Integer id,
                                                          @Valid @RequestBody UpdateServiceRequest request) {
        return ResponseEntity.ok(serviceCatalogService.update(id, request));
    }

    @DeleteMapping("/services/{id}")
    @Operation(summary = "Удалить сервис")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceCatalogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/plans")
    @Operation(summary = "Получить все планы")
    public ResponseEntity<List<PlanResponse>> getAllPlans() {
        return ResponseEntity.ok(planService.getAll());
    }

    @GetMapping("/plans/{id}")
    @Operation(summary = "Получить план по id")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.getById(id));
    }

    @PostMapping("/plans")
    @Operation(summary = "Добавить план")
    public ResponseEntity<PlanResponse> createPlan(@Valid @RequestBody CreatePlanRequest request) {
        return ResponseEntity.ok(planService.create(request));
    }

    @PatchMapping("/plans/{id}")
    @Operation(summary = "Обновить план")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable Integer id,
                                                    @Valid @RequestBody UpdatePlanRequest request) {
        return ResponseEntity.ok(planService.update(id, request));
    }

    @DeleteMapping("/plans/{id}")
    @Operation(summary = "Удалить план")
    public ResponseEntity<Void> deletePlan(@PathVariable Integer id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/catalog/billing-periods")
    @Operation(summary = "Получить все периоды оплаты")
    public ResponseEntity<List<BillingPeriodResponse>> getBillingPeriods() {
        return ResponseEntity.ok(catalogService.getBillingPeriods());
    }

    @PostMapping("/catalog/billing-periods")
    @Operation(summary = "Добавить период оплаты")
    public ResponseEntity<BillingPeriodResponse> createBillingPeriod(@Valid @RequestBody CreateCatalogItemRequest request) {
        return ResponseEntity.ok(catalogService.createBillingPeriod(request));
    }

    @DeleteMapping("/catalog/billing-periods/{id}")
    @Operation(summary = "Удалить период оплаты")
    public ResponseEntity<Void> deleteBillingPeriod(@PathVariable Integer id) {
        catalogService.deleteBillingPeriod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/catalog/device-types")
    @Operation(summary = "Получить все типы устройств")
    public ResponseEntity<List<DeviceTypeResponse>> getDeviceTypes() {
        return ResponseEntity.ok(catalogService.getDeviceTypes());
    }

    @PostMapping("/catalog/device-types")
    @Operation(summary = "Добавить тип устройства")
    public ResponseEntity<DeviceTypeResponse> createDeviceType(@Valid @RequestBody CreateCatalogItemRequest request) {
        return ResponseEntity.ok(catalogService.createDeviceType(request));
    }

    @DeleteMapping("/catalog/device-types/{id}")
    @Operation(summary = "Удалить тип устройства")
    public ResponseEntity<Void> deleteDeviceType(@PathVariable Integer id) {
        catalogService.deleteDeviceType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/catalog/event-types")
    @Operation(summary = "Получить все типы событий")
    public ResponseEntity<List<EventTypeResponse>> getEventTypes() {
        return ResponseEntity.ok(catalogService.getEventTypes());
    }

    @PostMapping("/catalog/event-types")
    @Operation(summary = "Добавить тип события")
    public ResponseEntity<EventTypeResponse> createEventType(@Valid @RequestBody CreateCatalogItemRequest request) {
        return ResponseEntity.ok(catalogService.createEventType(request));
    }

    @DeleteMapping("/catalog/event-types/{id}")
    @Operation(summary = "Удалить тип события")
    public ResponseEntity<Void> deleteEventType(@PathVariable Integer id) {
        catalogService.deleteEventType(id);
        return ResponseEntity.noContent().build();
    }
}
