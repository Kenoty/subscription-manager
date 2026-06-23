package com.example.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Payment {
    private Integer id;
    private Integer eventId;
    private BigDecimal amount;
    private String currency;
    private OffsetDateTime paidAt;

    public Payment(Integer eventId, BigDecimal amount, String currency) {
        this.eventId = eventId;
        this.amount = amount;
        this.currency = currency;
    }

    public Payment(Integer id, Integer eventId, BigDecimal amount, String currency, OffsetDateTime paidAt) {
        this.id = id;
        this.eventId = eventId;
        this.amount = amount;
        this.currency = currency;
        this.paidAt = paidAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
