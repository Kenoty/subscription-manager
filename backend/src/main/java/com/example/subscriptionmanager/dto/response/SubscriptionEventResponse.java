package com.example.subscriptionmanager.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SubscriptionEventResponse {
    private Integer id;
    private String eventType;
    private LocalDate eventDate;
    private Integer days;
}
