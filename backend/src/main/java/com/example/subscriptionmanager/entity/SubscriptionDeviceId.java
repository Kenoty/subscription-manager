package com.example.subscriptionmanager.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SubscriptionDeviceId implements Serializable {
    private UUID deviceId;
    private Integer subscriptionId;
}
