package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePlanRequest {
    @Size(max = 64)
    private String name;
    @DecimalMin("0.01")
    private BigDecimal price;
    @Size(max = 3)
    private String currency;
    private Integer billingPeriodId;
    private Integer maxDevices;
}
