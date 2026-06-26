package com.example.subscriptionmanager.dto.response;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class NotificationResponse {
    private Integer id;
    private String message;
    private Boolean isUnread;
    private OffsetDateTime createdAt;
}
