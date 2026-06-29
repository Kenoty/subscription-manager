package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.response.DeviceTypeResponse;
import com.example.subscriptionmanager.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
@Tag(name = "Catalog", description = "Публичные справочники")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/device-types")
    @Operation(summary = "Получить все типы устройств")
    public ResponseEntity<List<DeviceTypeResponse>> getDeviceTypes() {
        return ResponseEntity.ok(catalogService.getDeviceTypes());
    }
}
