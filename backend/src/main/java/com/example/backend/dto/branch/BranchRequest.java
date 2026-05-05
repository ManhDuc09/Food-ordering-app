package com.example.backend.dto.branch;

import lombok.Data;

@Data
public class BranchRequest {
   
    private String name;

    
    private String address;

   
    private Double latitude;

    
    private Double longitude;

    private Boolean isOpen = true;
}