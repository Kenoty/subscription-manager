package com.example.subscriptionmanager.dto.response;

import lombok.Data;

@Data
public class SubscriptionResponse {
    private Integer id;
    private PlanResponse plan;
    private Boolean autoRenew;
}
