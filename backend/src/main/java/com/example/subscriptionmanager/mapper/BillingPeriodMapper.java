package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.BillingPeriodResponse;
import com.example.subscriptionmanager.entity.BillingPeriod;
import org.springframework.stereotype.Component;

@Component
public class BillingPeriodMapper {
    public BillingPeriodResponse toResponse(BillingPeriod billingPeriod) {
        BillingPeriodResponse response = new BillingPeriodResponse();
        response.setId(billingPeriod.getId());
        response.setName(billingPeriod.getName());
        return response;
    }
}
