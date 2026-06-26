package com.example.subscriptionmanager.dto.response;

import lombok.Data;

@Data
public class ServiceResponse {
    private Integer id;
    private String name;
    private String description;
    private String websiteUrl;
    private String logoUrl;
}
