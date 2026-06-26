package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDeviceRequest {
    @Size(max = 64)
    private String name;
    private String note;
}
