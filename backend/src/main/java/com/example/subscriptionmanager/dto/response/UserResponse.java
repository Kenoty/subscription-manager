package com.example.subscriptionmanager.dto.response;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private OffsetDateTime createdAt;
}
