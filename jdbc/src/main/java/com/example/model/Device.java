package com.example.model;

import java.util.UUID;

public class Device {
    private UUID id;
    private Integer typeId;
    private String name;
    private String note;

    public Device(Integer typeId, String name, String note) {
        this.typeId = typeId;
        this.name = name;
        this.note = note;
    }

    public Device(UUID id, Integer typeId, String name, String note) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
        this.note = note;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
