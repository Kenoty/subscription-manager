package com.example.subscriptionmanager.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class DeviceResponse {
    private UUID id;
    private String name;
    private String type;
    private String note;
}
