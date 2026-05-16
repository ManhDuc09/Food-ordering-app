package com.example.backend.service;

import com.example.backend.dto.product.ProductResponse;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {  
        List<Product> products = productRepository.getAll();
    
    
    products.forEach(p -> System.out.println("Product: " + p.getName() + " | Categories size: " + p.getCategories().size()));
        return products
                .stream()  
                .map(productMapper::productToResponse)  
                .toList();  
    }  

    public List<ProductResponse> getAvailableProducts() {  
        return productRepository.getAvailable() // Updated method
                .stream()  
                .map(productMapper::productToResponse)  
                .toList();  
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findByProductId(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.productToResponse(product);
    }
}