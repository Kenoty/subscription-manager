package com.example.subscriptionmanager.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PlanResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String currency;
    private String billingPeriod;
    private Integer maxDevices;
    private ServiceResponse service;
}
