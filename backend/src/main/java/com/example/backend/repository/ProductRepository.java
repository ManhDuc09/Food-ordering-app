package com.example.backend.repository;

import com.example.backend.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.categories")
    List<Product> getAll();


    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.categories WHERE p.isAvailable = true")
    List<Product> getAvailable();

    @EntityGraph(attributePaths = {"categories"})
    Optional<Product> findByProductId(UUID productId);

    List<Product> findByCategories_NameIgnoreCase(String name);  
}