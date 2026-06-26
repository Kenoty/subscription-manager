package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.PlanResponse;
import com.example.subscriptionmanager.entity.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanMapper {

    private final ServiceMapper serviceMapper;

    public PlanResponse toResponse(Plan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setName(plan.getName());
        response.setPrice(plan.getPrice());
        response.setCurrency(plan.getCurrency());
        response.setBillingPeriod(plan.getBillingPeriod().getName());
        response.setMaxDevices(plan.getMaxDevices());
        response.setService(serviceMapper.toResponse(plan.getService()));
        return response;
    }
}
