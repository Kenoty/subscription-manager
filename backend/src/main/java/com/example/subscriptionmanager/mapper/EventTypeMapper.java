package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.EventTypeResponse;
import com.example.subscriptionmanager.entity.EventType;
import org.springframework.stereotype.Component;

@Component
public class EventTypeMapper {
    public EventTypeResponse toResponse(EventType eventType) {
        EventTypeResponse response = new EventTypeResponse();
        response.setId(eventType.getId());
        response.setName(eventType.getName());
        return response;
    }
}
