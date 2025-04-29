package com.example.entity;

import java.time.Instant;

public class InfoDocument {
    private String id;
    private String operation;
    private Instant createdAt;

    public InfoDocument() {
    }

    public InfoDocument(String id, String operation, Instant createdAt) {
        this.id = id;
        this.operation = operation;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
