package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.categories.CategoriesResponse;
import com.example.backend.service.CategoriesService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<List<CategoriesResponse>> getAllCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }
    
    
}
