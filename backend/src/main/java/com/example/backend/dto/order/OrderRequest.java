package com.example.backend.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {
    @NotNull
    private List<OrderItemRequest> items;
    private BigDecimal totalAmount;
    private String paymentMethod;
    @NotBlank
    private String fullName;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
    @NotNull
    private UUID branchId;

    @Data
    public static class OrderItemRequest {
        private UUID productId;
        private Integer quantity;
    }
}
