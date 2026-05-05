package com.example.backend.dto.branch;

import lombok.Data;
import java.util.UUID;

@Data
public class BranchResponse {
    private UUID branchId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isOpen;
}