package com.example.backend.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {
    private List<OrderItemRequest> items;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String fullName;
    private String phone;
    private String address;
    private UUID branchId;

    @Data
    public static class OrderItemRequest {
        private UUID productId;
        private Integer quantity;
    }
}
