package com.example.backend.service;

import com.example.backend.dto.product.ProductResponse;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(productMapper::productToResponse)
                .toList();
    }

    public List<ProductResponse> getAvailableProducts() {
   
        return productRepository.findByIsAvailableTrue()
                .stream()
                .map(productMapper::productToResponse)
                .toList();
    }
}