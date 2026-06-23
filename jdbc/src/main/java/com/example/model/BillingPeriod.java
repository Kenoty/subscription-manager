package com.example.model;

public class BillingPeriod {
    private Integer id;
    private String name;

    public BillingPeriod(String name) {
        this.name = name;
    }

    public BillingPeriod(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
