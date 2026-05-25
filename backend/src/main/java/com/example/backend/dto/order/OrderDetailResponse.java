package com.example.backend.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderDetailResponse {
    private UUID orderId;
    private String status;
    private BigDecimal totalAmount;
    private OffsetDateTime createdAt;
    private String deliveryName;
    private String deliveryPhone;
    private String deliveryAddress;
    private String paymentMethod;
    private String paymentStatus;
    private UUID paymentId;
    private List<OrderItemDto> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemDto {
        private String productName;
        private Integer quantity;
        private BigDecimal price;
    }
}
