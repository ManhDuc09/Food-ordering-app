package com.example.backend.enums;

public enum UserRole {
    CUSTOMER("customer"),
    ADMIN("admin"),
    BRANCH_MANAGER("branch_manager");

    public final String value;

    UserRole(String value) {
        this.value = value;
    }
}
