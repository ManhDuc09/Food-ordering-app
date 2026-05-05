package com.example.backend.dto.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email; 
    private String password;
      private String fullName;
      private String phoneNumber;
}
