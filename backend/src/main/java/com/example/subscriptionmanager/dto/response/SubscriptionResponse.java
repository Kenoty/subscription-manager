package com.example.subscriptionmanager.dto.response;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SubscriptionResponse {
    private Integer id;
    private PlanResponse plan;
    private Boolean autoRenew;
    private OffsetDateTime cancelledAt;
}
