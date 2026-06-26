package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.SubscriptionResponse;
import com.example.subscriptionmanager.entity.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final PlanMapper planMapper;

    public SubscriptionResponse toResponse(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(subscription.getId());
        response.setPlan(planMapper.toResponse(subscription.getPlan()));
        response.setAutoRenew(subscription.getAutoRenew());
        return response;
    }
}
