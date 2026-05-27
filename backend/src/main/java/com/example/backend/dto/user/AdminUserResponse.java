package com.example.backend.dto.user;

import lombok.Data;
import java.util.UUID;

@Data
public class AdminUserResponse {
    private UUID userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private UUID branchId;
    private String branchName;
}
