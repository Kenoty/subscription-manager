package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.request.CreateServiceRequest;
import com.example.subscriptionmanager.dto.request.UpdateServiceRequest;
import com.example.subscriptionmanager.dto.response.ServiceResponse;
import com.example.subscriptionmanager.entity.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceResponse toResponse(Service service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setWebsiteUrl(service.getWebsiteUrl());
        response.setLogoUrl(service.getLogoUrl());
        return response;
    }

    public Service toEntity(CreateServiceRequest request) {
        Service service = new Service();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setWebsiteUrl(request.getWebsiteUrl());
        service.setLogoUrl(request.getLogoUrl());
        return service;
    }

    public void updateEntity(Service service, UpdateServiceRequest request) {
        if (request.getName() != null) service.setName(request.getName());
        if (request.getDescription() != null) service.setDescription(request.getDescription());
        if (request.getWebsiteUrl() != null) service.setWebsiteUrl(request.getWebsiteUrl());
        if (request.getLogoUrl() != null) service.setLogoUrl(request.getLogoUrl());
    }
}
