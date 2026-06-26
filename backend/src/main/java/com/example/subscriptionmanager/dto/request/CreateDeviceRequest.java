package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateDeviceRequest {
    @NotBlank @Size(max = 64)
    private String name;
    @NotNull
    private Integer typeId;
    private String note;
}
