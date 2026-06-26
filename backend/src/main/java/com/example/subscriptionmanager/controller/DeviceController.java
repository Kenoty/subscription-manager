package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.request.AttachDeviceRequest;
import com.example.subscriptionmanager.dto.request.CreateDeviceRequest;
import com.example.subscriptionmanager.dto.request.UpdateDeviceRequest;
import com.example.subscriptionmanager.dto.response.DeviceResponse;
import com.example.subscriptionmanager.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Tag(name = "Devices", description = "Управление устройствами")
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    @Operation(summary = "Получить свои устройства")
    public ResponseEntity<List<DeviceResponse>> getAll() {
        return ResponseEntity.ok(deviceService.getMyDevices());
    }

    @GetMapping("/{deviceId}")
    @Operation(summary = "Получить устройство по id")
    public ResponseEntity<DeviceResponse> getById(@PathVariable UUID deviceId) {
        return ResponseEntity.ok(deviceService.getById(deviceId));
    }

    @GetMapping("/subscriptions/{subscriptionId}")
    @Operation(summary = "Получить устройства подписки")
    public ResponseEntity<List<DeviceResponse>> getBySubscription(@PathVariable Integer subscriptionId) {
        return ResponseEntity.ok(deviceService.getBySubscription(subscriptionId));
    }

    @PostMapping
    @Operation(summary = "Добавить устройство")
    public ResponseEntity<DeviceResponse> create(@Valid @RequestBody CreateDeviceRequest request) {
        return ResponseEntity.ok(deviceService.create(request));
    }

    @PatchMapping("/{deviceId}")
    @Operation(summary = "Обновить устройство")
    public ResponseEntity<DeviceResponse> update(@PathVariable UUID deviceId,
                                                  @Valid @RequestBody UpdateDeviceRequest request) {
        return ResponseEntity.ok(deviceService.update(deviceId, request));
    }

    @PostMapping("/subscriptions/{subscriptionId}/attach")
    @Operation(summary = "Привязать устройство к подписке")
    public ResponseEntity<Void> attach(@PathVariable Integer subscriptionId,
                                       @Valid @RequestBody AttachDeviceRequest request) {
        deviceService.attachToSubscription(subscriptionId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/subscriptions/{subscriptionId}/detach/{deviceId}")
    @Operation(summary = "Отвязать устройство от подписки")
    public ResponseEntity<Void> detach(@PathVariable Integer subscriptionId,
                                       @PathVariable UUID deviceId) {
        deviceService.detachFromSubscription(subscriptionId, deviceId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{deviceId}")
    @Operation(summary = "Удалить устройство")
    public ResponseEntity<Void> delete(@PathVariable UUID deviceId) {
        deviceService.delete(deviceId);
        return ResponseEntity.noContent().build();
    }
}
