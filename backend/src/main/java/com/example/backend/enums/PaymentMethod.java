package com.example.backend.enums;

public enum PaymentMethod {
    COD("COD"),
    VNPAY("VNPAY"),
    MOMO("MOMO");

    public final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}
