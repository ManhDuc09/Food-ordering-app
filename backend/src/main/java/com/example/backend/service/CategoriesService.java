package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.categories.CategoriesResponse;
import com.example.backend.mapper.CategoriesMapper;
import com.example.backend.repository.CategoriesRespository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRespository categoriesRespository;
    private final CategoriesMapper categoriesMapper;

    public List<CategoriesResponse> getAllCategories() {
        return categoriesRespository.findAll()
                .stream()
                .map(categoriesMapper::categoriesToResponse)
                .toList();
    }
    
}
