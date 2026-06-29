package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.request.CreateDeviceRequest;
import com.example.subscriptionmanager.dto.request.UpdateDeviceRequest;
import com.example.subscriptionmanager.dto.response.DeviceResponse;
import com.example.subscriptionmanager.entity.Device;
import com.example.subscriptionmanager.entity.DeviceType;
import com.example.subscriptionmanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public DeviceResponse toResponse(Device device) {
        DeviceResponse response = new DeviceResponse();
        response.setId(device.getId());
        response.setName(device.getName());
        response.setType(device.getDeviceType().getName());
        response.setNote(device.getNote());
        return response;
    }

    public Device toEntity(CreateDeviceRequest request, DeviceType type, User user) {
        Device device = new Device();
        device.setName(request.getName());
        device.setDeviceType(type);
        device.setUser(user);
        device.setNote(request.getNote());
        return device;
    }

    public void updateEntity(Device device, UpdateDeviceRequest request) {
        if (request.getName() != null) device.setName(request.getName());
        if (request.getNote() != null) device.setNote(request.getNote());
    }
}
