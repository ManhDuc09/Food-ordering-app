package com.example.backend.enums;

public enum OrderStatus {
    PENDING("pending"),
    PREPARING("preparing"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    public final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
