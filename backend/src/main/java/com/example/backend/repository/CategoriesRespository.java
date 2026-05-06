package com.example.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Categories;

public interface CategoriesRespository extends JpaRepository<Categories, UUID> {

    

    
}
