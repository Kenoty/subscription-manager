package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.CreateCatalogItemRequest;
import com.example.subscriptionmanager.dto.response.BillingPeriodResponse;
import com.example.subscriptionmanager.dto.response.DeviceTypeResponse;
import com.example.subscriptionmanager.dto.response.EventTypeResponse;
import com.example.subscriptionmanager.entity.BillingPeriod;
import com.example.subscriptionmanager.entity.DeviceType;
import com.example.subscriptionmanager.entity.EventType;
import com.example.subscriptionmanager.exception.ResourceNotFoundException;
import com.example.subscriptionmanager.mapper.BillingPeriodMapper;
import com.example.subscriptionmanager.mapper.DeviceTypeMapper;
import com.example.subscriptionmanager.mapper.EventTypeMapper;
import com.example.subscriptionmanager.repository.BillingPeriodRepository;
import com.example.subscriptionmanager.repository.DeviceTypeRepository;
import com.example.subscriptionmanager.repository.EventTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final BillingPeriodRepository billingPeriodRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final EventTypeRepository eventTypeRepository;
    private final BillingPeriodMapper billingPeriodMapper;
    private final DeviceTypeMapper deviceTypeMapper;
    private final EventTypeMapper eventTypeMapper;

    public List<BillingPeriodResponse> getBillingPeriods() {
        return billingPeriodRepository.findAll().stream().map(billingPeriodMapper::toResponse).toList();
    }

    public BillingPeriodResponse createBillingPeriod(CreateCatalogItemRequest request) {
        BillingPeriod bp = new BillingPeriod();
        bp.setName(request.getName());
        return billingPeriodMapper.toResponse(billingPeriodRepository.save(bp));
    }

    public void deleteBillingPeriod(Integer id) {
        if (!billingPeriodRepository.existsById(id)) throw new ResourceNotFoundException("Billing period not found");
        billingPeriodRepository.deleteById(id);
    }

    public List<DeviceTypeResponse> getDeviceTypes() {
        return deviceTypeRepository.findAll().stream().map(deviceTypeMapper::toResponse).toList();
    }

    public DeviceTypeResponse createDeviceType(CreateCatalogItemRequest request) {
        DeviceType dt = new DeviceType();
        dt.setName(request.getName());
        return deviceTypeMapper.toResponse(deviceTypeRepository.save(dt));
    }

    public void deleteDeviceType(Integer id) {
        if (!deviceTypeRepository.existsById(id)) throw new ResourceNotFoundException("Device type not found");
        deviceTypeRepository.deleteById(id);
    }

    public List<EventTypeResponse> getEventTypes() {
        return eventTypeRepository.findAll().stream().map(eventTypeMapper::toResponse).toList();
    }

    public EventTypeResponse createEventType(CreateCatalogItemRequest request) {
        EventType et = new EventType();
        et.setName(request.getName());
        return eventTypeMapper.toResponse(eventTypeRepository.save(et));
    }

    public void deleteEventType(Integer id) {
        if (!eventTypeRepository.existsById(id)) throw new ResourceNotFoundException("Event type not found");
        eventTypeRepository.deleteById(id);
    }
}
