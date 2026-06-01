package com.example.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    private String fullName;
    private String phoneNumber;
}
