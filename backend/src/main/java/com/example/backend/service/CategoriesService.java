package com.example.backend.service;

import com.example.backend.dto.categories.CategoriesResponse;
import com.example.backend.mapper.CategoriesMapper;
import com.example.backend.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    public List<CategoriesResponse> getAllCategories() {
        return categoriesRepository.findAll()
                .stream()
                .map(categoriesMapper::categoriesToResponse)
                .toList();
    }
}
