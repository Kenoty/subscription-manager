package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.SubscriptionEventResponse;
import com.example.subscriptionmanager.entity.SubscriptionEvent;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionEventMapper {

    public SubscriptionEventResponse toResponse(SubscriptionEvent event) {
        SubscriptionEventResponse response = new SubscriptionEventResponse();
        response.setId(event.getId());
        response.setEventType(event.getEventType().getName());
        response.setEventDate(event.getEventDate());
        response.setDays(event.getDays());
        return response;
    }
}
