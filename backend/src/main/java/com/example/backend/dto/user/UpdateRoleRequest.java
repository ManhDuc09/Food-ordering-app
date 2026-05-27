package com.example.backend.dto.user;

import lombok.Data;
import java.util.UUID;

@Data
public class UpdateRoleRequest {
    private String role;
    private UUID branchId;
}
