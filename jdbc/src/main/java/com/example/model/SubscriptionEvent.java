package com.example.model;

import java.time.LocalDate;

public class SubscriptionEvent {
    private Integer id;
    private Integer subscriptionId;
    private Integer eventId;
    private LocalDate eventDate;
    private Integer days;

    public SubscriptionEvent(Integer subscriptionId, Integer eventId, LocalDate eventDate, Integer days) {
        this.subscriptionId = subscriptionId;
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.days = days;
    }

    public SubscriptionEvent(Integer id, Integer subscriptionId, Integer eventId, LocalDate eventDate, Integer days) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.days = days;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
