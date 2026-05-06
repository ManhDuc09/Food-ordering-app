package com.example.backend.mapper;

import org.mapstruct.Mapper;

import com.example.backend.dto.categories.CategoriesResponse;
import com.example.backend.model.Categories;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    CategoriesResponse categoriesToResponse(Categories categories);   

}
