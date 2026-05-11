package com.example.backend.mapper;

import com.example.backend.dto.product.ProductResponse;
import com.example.backend.model.Product;
import com.example.backend.model.Categories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "categoryNames", source = "categories", qualifiedByName = "mapCategoryNames")
    ProductResponse productToResponse(Product product);


    @Named("mapCategoryNames")
    default List<String> mapCategoryNames(Set<Categories> categories) {
        if (categories == null) return null;
        return categories.stream()
                .map(Categories::getName)
                .collect(Collectors.toList());
    }
}