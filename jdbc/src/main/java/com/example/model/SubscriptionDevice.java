package com.example.model;

import java.time.OffsetDateTime;

public class SubscriptionDevice {
    private Integer deviceId;
    private Integer subscriptionId;
    private OffsetDateTime addedAt;
    private OffsetDateTime removedAt;

    public SubscriptionDevice(Integer deviceId, Integer subscriptionId, OffsetDateTime addedAt, OffsetDateTime removedAt) {
        this.deviceId = deviceId;
        this.subscriptionId = subscriptionId;
        this.addedAt = addedAt;
        this.removedAt = removedAt;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public OffsetDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(OffsetDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public OffsetDateTime getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(OffsetDateTime removedAt) {
        this.removedAt = removedAt;
    }
}
