package com.example.backend.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class ProductResponse {
    private UUID productId;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isAvailable;
    private List<String> categoryNames; 
    private BigDecimal price;           
}