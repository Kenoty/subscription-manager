package com.example.model;

public class Subscription {
    private Integer id;
    private Integer userId;
    private Integer planId;
    private Boolean autoRenew;

    public Subscription(Integer userId, Integer planId, Boolean autoRenew) {
        this.userId = userId;
        this.planId = planId;
        this.autoRenew = autoRenew;
    }

    public Subscription(Integer id, Integer userId, Integer planId, Boolean autoRenew) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.autoRenew = autoRenew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
