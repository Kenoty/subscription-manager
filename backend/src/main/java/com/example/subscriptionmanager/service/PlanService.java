package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.CreatePlanRequest;
import com.example.subscriptionmanager.dto.request.UpdatePlanRequest;
import com.example.subscriptionmanager.dto.response.PlanResponse;
import com.example.subscriptionmanager.entity.BillingPeriod;
import com.example.subscriptionmanager.entity.Plan;
import com.example.subscriptionmanager.entity.Service;
import com.example.subscriptionmanager.exception.ResourceNotFoundException;
import com.example.subscriptionmanager.mapper.PlanMapper;
import com.example.subscriptionmanager.repository.BillingPeriodRepository;
import com.example.subscriptionmanager.repository.PlanRepository;
import com.example.subscriptionmanager.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final ServiceRepository serviceRepository;
    private final BillingPeriodRepository billingPeriodRepository;
    private final PlanMapper planMapper;

    public List<PlanResponse> getAll() {
        return planRepository.findAll().stream().map(planMapper::toResponse).toList();
    }

    public PlanResponse getById(Integer id) {
        return planMapper.toResponse(planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found")));
    }

    public PlanResponse create(CreatePlanRequest request) {
        Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        BillingPeriod billingPeriod = billingPeriodRepository.findById(request.getBillingPeriodId())
                .orElseThrow(() -> new ResourceNotFoundException("Billing period not found"));
        Plan plan = new Plan();
        plan.setService(service);
        plan.setName(request.getName());
        plan.setPrice(request.getPrice());
        plan.setCurrency(request.getCurrency());
        plan.setBillingPeriod(billingPeriod);
        plan.setMaxDevices(request.getMaxDevices());
        return planMapper.toResponse(planRepository.save(plan));
    }

    public PlanResponse update(Integer id, UpdatePlanRequest request) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
        if (request.getName() != null) plan.setName(request.getName());
        if (request.getPrice() != null) plan.setPrice(request.getPrice());
        if (request.getCurrency() != null) plan.setCurrency(request.getCurrency());
        if (request.getMaxDevices() != null) plan.setMaxDevices(request.getMaxDevices());
        if (request.getBillingPeriodId() != null) {
            BillingPeriod bp = billingPeriodRepository.findById(request.getBillingPeriodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Billing period not found"));
            plan.setBillingPeriod(bp);
        }
        return planMapper.toResponse(planRepository.save(plan));
    }

    public void delete(Integer id) {
        if (!planRepository.existsById(id)) throw new ResourceNotFoundException("Plan not found");
        planRepository.deleteById(id);
    }
}
