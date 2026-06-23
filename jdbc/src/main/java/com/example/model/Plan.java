package com.example.model;

import java.math.BigDecimal;

public class Plan {
    private Integer id;
    private Integer serviceId;
    private String name;
    private BigDecimal price;
    private String currency;
    private Integer billingPeriodId;
    private Integer maxDevices;

    public Plan(Integer serviceId, String name, BigDecimal price, String currency, Integer billingPeriodId, Integer maxDevices) {
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.billingPeriodId = billingPeriodId;
        this.maxDevices = maxDevices;
    }

    public Plan(Integer id, Integer serviceId, String name, BigDecimal price, String currency, Integer billingPeriodId, Integer maxDevices) {
        this.id = id;
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.billingPeriodId = billingPeriodId;
        this.maxDevices = maxDevices;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getBillingPeriodId() {
        return billingPeriodId;
    }

    public void setBillingPeriodId(Integer billingPeriodId) {
        this.billingPeriodId = billingPeriodId;
    }

    public Integer getMaxDevices() {
        return maxDevices;
    }

    public void setMaxDevices(Integer maxDevices) {
        this.maxDevices = maxDevices;
    }
}
