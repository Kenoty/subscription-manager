package com.example.subscriptionmanager.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSubscriptionRequest {
    @NotNull
    private Integer planId;
    private Boolean autoRenew = true;
}
