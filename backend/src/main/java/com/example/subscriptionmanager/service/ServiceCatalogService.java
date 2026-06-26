package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.CreateServiceRequest;
import com.example.subscriptionmanager.dto.request.UpdateServiceRequest;
import com.example.subscriptionmanager.dto.response.PlanResponse;
import com.example.subscriptionmanager.dto.response.ServiceResponse;
import com.example.subscriptionmanager.entity.Service;
import com.example.subscriptionmanager.exception.ResourceNotFoundException;
import com.example.subscriptionmanager.mapper.PlanMapper;
import com.example.subscriptionmanager.mapper.ServiceMapper;
import com.example.subscriptionmanager.repository.PlanRepository;
import com.example.subscriptionmanager.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final PlanRepository planRepository;
    private final ServiceMapper serviceMapper;
    private final PlanMapper planMapper;

    public List<ServiceResponse> getAll() {
        return serviceRepository.findAll().stream().map(serviceMapper::toResponse).toList();
    }

    public ServiceResponse getById(Integer id) {
        return serviceMapper.toResponse(serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found")));
    }

    public List<PlanResponse> getPlansByService(Integer serviceId) {
        if (!serviceRepository.existsById(serviceId)) throw new ResourceNotFoundException("Service not found");
        return planRepository.findByServiceId(serviceId).stream().map(planMapper::toResponse).toList();
    }

    public ServiceResponse create(CreateServiceRequest request) {
        return serviceMapper.toResponse(serviceRepository.save(serviceMapper.toEntity(request)));
    }

    public ServiceResponse update(Integer id, UpdateServiceRequest request) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        serviceMapper.updateEntity(service, request);
        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    public void delete(Integer id) {
        if (!serviceRepository.existsById(id)) throw new ResourceNotFoundException("Service not found");
        serviceRepository.deleteById(id);
    }
}
