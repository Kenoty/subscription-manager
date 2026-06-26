package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCatalogItemRequest {
    @NotBlank @Size(max = 32)
    private String name;
}
