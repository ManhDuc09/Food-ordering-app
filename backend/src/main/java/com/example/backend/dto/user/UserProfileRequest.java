package com.example.backend.dto.user;

import lombok.Data;

@Data
public class UserProfileRequest {
    private String fullName;
    private String phoneNumber;
}
