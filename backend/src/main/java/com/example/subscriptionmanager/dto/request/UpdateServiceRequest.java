package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateServiceRequest {
    @Size(max = 64)
    private String name;
    private String description;
    @Size(max = 512)
    private String websiteUrl;
    @Size(max = 512)
    private String logoUrl;
}
