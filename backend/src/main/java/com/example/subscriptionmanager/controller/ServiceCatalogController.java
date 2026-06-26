package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.response.PlanResponse;
import com.example.subscriptionmanager.dto.response.ServiceResponse;
import com.example.subscriptionmanager.service.ServiceCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Services", description = "Каталог сервисов")
public class ServiceCatalogController {

    private final ServiceCatalogService serviceCatalogService;

    @GetMapping
    @Operation(summary = "Получить все сервисы")
    public ResponseEntity<List<ServiceResponse>> getAll() {
        return ResponseEntity.ok(serviceCatalogService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить сервис по id")
    public ResponseEntity<ServiceResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceCatalogService.getById(id));
    }

    @GetMapping("/{id}/plans")
    @Operation(summary = "Получить планы сервиса")
    public ResponseEntity<List<PlanResponse>> getPlans(@PathVariable Integer id) {
        return ResponseEntity.ok(serviceCatalogService.getPlansByService(id));
    }
}
