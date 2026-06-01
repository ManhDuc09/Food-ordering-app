package com.example.backend.service;

import com.example.backend.dto.product.ProductResponse;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.getAll();
        log.debug("Fetched {} products", products.size());
        return products.stream()
                .map(productMapper::productToResponse)
                .toList();
    }

    public List<ProductResponse> getAvailableProducts() {
        List<Product> products = productRepository.getAvailable();
        log.debug("Fetched {} available products", products.size());
        return products.stream()
                .map(productMapper::productToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findByProductId(id)
                .orElseThrow(() -> {
                    log.warn("Product not found: {}", id);
                    return new RuntimeException("Product not found");
                });
        return productMapper.productToResponse(product);
    }
}