package com.example.backend.dto.categories;

import java.util.UUID;

import lombok.Data;

@Data
public class CategoriesResponse {
    private UUID categoryId;
    private String name;
    private String imageUrl;
}
