package com.hostel.model;

import java.time.LocalDateTime;

/**
 * BaseEntity: Demonstrates Unit 2 Inheritance, Abstract Classes, and Super.
 */
public abstract class BaseEntity {
    protected int id;
    protected LocalDateTime createdAt;

    public BaseEntity(int id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Abstract method to be overridden by subclasses (Polymorphism)
    public abstract String getSummary();
}
