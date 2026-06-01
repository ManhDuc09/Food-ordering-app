package com.example.backend.enums;

public enum PaymentStatus {
    PENDING("pending"),
    PAID("paid");

    public final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
