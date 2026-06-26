package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.response.DeviceTypeResponse;
import com.example.subscriptionmanager.entity.DeviceType;
import org.springframework.stereotype.Component;

@Component
public class DeviceTypeMapper {
    public DeviceTypeResponse toResponse(DeviceType deviceType) {
        DeviceTypeResponse response = new DeviceTypeResponse();
        response.setId(deviceType.getId());
        response.setName(deviceType.getName());
        return response;
    }
}
