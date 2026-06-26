package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePlanRequest {
    @NotNull
    private Integer serviceId;
    @NotBlank @Size(max = 64)
    private String name;
    @NotNull @DecimalMin("0.01")
    private BigDecimal price;
    @NotBlank @Size(max = 3)
    private String currency;
    @NotNull
    private Integer billingPeriodId;
    private Integer maxDevices;
}
