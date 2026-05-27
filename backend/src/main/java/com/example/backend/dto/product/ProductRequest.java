package com.example.backend.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isAvailable = true;
    private BigDecimal price;
    private Set<UUID> categoryIds;
}
