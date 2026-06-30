package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.AttachDeviceRequest;
import com.example.subscriptionmanager.dto.request.CreateDeviceRequest;
import com.example.subscriptionmanager.dto.request.UpdateDeviceRequest;
import com.example.subscriptionmanager.dto.response.DeviceResponse;
import com.example.subscriptionmanager.entity.Device;
import com.example.subscriptionmanager.entity.DeviceType;
import com.example.subscriptionmanager.entity.Plan;
import com.example.subscriptionmanager.entity.Subscription;
import com.example.subscriptionmanager.entity.SubscriptionDevice;
import com.example.subscriptionmanager.entity.SubscriptionDeviceId;
import com.example.subscriptionmanager.entity.User;
import com.example.subscriptionmanager.exception.BadRequestException;
import com.example.subscriptionmanager.exception.ResourceNotFoundException;
import com.example.subscriptionmanager.mapper.DeviceMapper;
import com.example.subscriptionmanager.repository.DeviceRepository;
import com.example.subscriptionmanager.repository.DeviceTypeRepository;
import com.example.subscriptionmanager.repository.SubscriptionDeviceRepository;
import com.example.subscriptionmanager.repository.SubscriptionRepository;
import com.example.subscriptionmanager.repository.UserRepository;
import com.example.subscriptionmanager.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionDeviceRepository subscriptionDeviceRepository;
    private final UserRepository userRepository;
    private final DeviceMapper deviceMapper;
    private final CurrentUserProvider currentUserProvider;

    public List<DeviceResponse> getMyDevices() {
        return deviceRepository.findByUserId(currentUserProvider.getCurrentUserId())
                .stream().map(deviceMapper::toResponse).toList();
    }

    public DeviceResponse getById(UUID deviceId) {
        return deviceMapper.toResponse(deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found")));
    }

    public DeviceResponse create(CreateDeviceRequest request) {
        DeviceType type = deviceTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Device type not found"));
        User user = userRepository.findById(currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Device device = deviceMapper.toEntity(request, type, user);
        device.setId(UUID.randomUUID());
        return deviceMapper.toResponse(deviceRepository.save(device));
    }

    public DeviceResponse update(UUID deviceId, UpdateDeviceRequest request) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        deviceMapper.updateEntity(device, request);
        return deviceMapper.toResponse(deviceRepository.save(device));
    }

    public List<DeviceResponse> getBySubscription(Integer subscriptionId) {
        subscriptionRepository.findByIdAndUserIdAndCancelledAtIsNull(subscriptionId, currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        return subscriptionDeviceRepository.findBySubscriptionId(subscriptionId)
                .stream()
                .filter(sd -> sd.getRemovedAt() == null)
                .map(sd -> deviceMapper.toResponse(sd.getDevice()))
                .toList();
    }

    public void attachToSubscription(Integer subscriptionId, AttachDeviceRequest request) {
        Subscription subscription = subscriptionRepository.findByIdAndUserIdAndCancelledAtIsNull(
                subscriptionId, currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        Plan plan = subscription.getPlan();
        if (plan.getMaxDevices() != null) {
            long activeCount = subscriptionDeviceRepository.findBySubscriptionId(subscriptionId)
                    .stream().filter(sd -> sd.getRemovedAt() == null).count();
            if (activeCount >= plan.getMaxDevices()) {
                throw new BadRequestException("Device limit reached for this subscription");
            }
        }
        SubscriptionDeviceId id = new SubscriptionDeviceId(device.getId(), subscriptionId);
        SubscriptionDevice sd = new SubscriptionDevice();
        sd.setId(id);
        sd.setDevice(device);
        sd.setSubscription(subscription);
        sd.setAddedAt(OffsetDateTime.now());
        subscriptionDeviceRepository.save(sd);
    }

    public void detachFromSubscription(Integer subscriptionId, UUID deviceId) {
        subscriptionRepository.findByIdAndUserIdAndCancelledAtIsNull(subscriptionId, currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
        SubscriptionDeviceId id = new SubscriptionDeviceId(deviceId, subscriptionId);
        SubscriptionDevice sd = subscriptionDeviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not attached to this subscription"));
        sd.setRemovedAt(OffsetDateTime.now());
        subscriptionDeviceRepository.save(sd);
    }

    public void delete(UUID deviceId) {
        if (!deviceRepository.existsById(deviceId)) throw new ResourceNotFoundException("Device not found");
        deviceRepository.deleteById(deviceId);
    }
}
