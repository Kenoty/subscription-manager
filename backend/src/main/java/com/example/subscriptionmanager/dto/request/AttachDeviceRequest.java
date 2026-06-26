package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AttachDeviceRequest {
    @NotNull
    private UUID deviceId;
}
