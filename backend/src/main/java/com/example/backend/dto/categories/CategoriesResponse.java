package com.example.backend.dto.categories;

import java.util.UUID;

import lombok.Data;

@Data
public class CategoriesResponse {
    private Integer categoryId;
    private String name;
}
