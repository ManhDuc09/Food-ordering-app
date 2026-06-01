package com.example.backend.service;

import com.example.backend.dto.product.ProductResponse;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_returnsAllMappedProducts() {
        Product product = buildProduct("Zinger", true);
        ProductResponse response = buildResponse(product.getProductId(), "Zinger", true);

        when(productRepository.getAll()).thenReturn(List.of(product));
        when(productMapper.productToResponse(product)).thenReturn(response);

        List<ProductResponse> result = productService.getAllProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Zinger");
    }

    @Test
    void getAllProducts_emptyRepository_returnsEmptyList() {
        when(productRepository.getAll()).thenReturn(List.of());

        List<ProductResponse> result = productService.getAllProducts();

        assertThat(result).isEmpty();
    }

    @Test
    void getAvailableProducts_returnsOnlyAvailableProducts() {
        Product product = buildProduct("Original Recipe", true);
        ProductResponse response = buildResponse(product.getProductId(), "Original Recipe", true);

        when(productRepository.getAvailable()).thenReturn(List.of(product));
        when(productMapper.productToResponse(product)).thenReturn(response);

        List<ProductResponse> result = productService.getAvailableProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsAvailable()).isTrue();
    }

    @Test
    void getProductById_existingId_returnsProduct() {
        UUID id = UUID.randomUUID();
        Product product = buildProduct("Twister", true);
        product.setProductId(id);
        ProductResponse response = buildResponse(id, "Twister", true);

        when(productRepository.findByProductId(id)).thenReturn(Optional.of(product));
        when(productMapper.productToResponse(product)).thenReturn(response);

        ProductResponse result = productService.getProductById(id);

        assertThat(result.getProductId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Twister");
    }

    @Test
    void getProductById_unknownId_throwsRuntimeException() {
        UUID id = UUID.randomUUID();
        when(productRepository.findByProductId(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");
    }

    private Product buildProduct(String name, boolean available) {
        Product p = new Product();
        p.setProductId(UUID.randomUUID());
        p.setName(name);
        p.setPrice(BigDecimal.valueOf(50000));
        p.setIsAvailable(available);
        p.setCategories(Set.of());
        return p;
    }

    private ProductResponse buildResponse(UUID id, String name, boolean available) {
        ProductResponse r = new ProductResponse();
        r.setProductId(id);
        r.setName(name);
        r.setIsAvailable(available);
        return r;
    }
}
