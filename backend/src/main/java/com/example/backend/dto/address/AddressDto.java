package com.example.backend.dto.address;

import lombok.Data;

import java.util.UUID;

@Data
public class AddressDto {
    private UUID id;
    private String street;
    private String city;
    private Boolean isDefault;
    private Double latitude;
    private Double longitude;
}
