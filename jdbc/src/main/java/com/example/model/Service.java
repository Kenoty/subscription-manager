package com.example.model;

public class Service {
    private Integer id;
    private String name;
    private String description;
    private String websiteUrl;
    private String logoUrl;

    public Service(String name, String description, String websiteUrl, String logoUrl) {
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.logoUrl = logoUrl;
    }

    public Service(Integer id, String name, String description, String websiteUrl, String logoUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.logoUrl = logoUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
