package com.example.backend.controller;

import com.example.backend.dto.order.OrderDetailResponse;
import com.example.backend.dto.order.OrderRequest;
import com.example.backend.dto.order.OrderResponse;
import com.example.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderDetailResponse>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PatchMapping("/{orderId}/payment")
    public ResponseEntity<OrderDetailResponse> updatePaymentMethod(
            @PathVariable UUID orderId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(orderService.updatePaymentMethod(orderId, body.get("method")));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDetailResponse> cancelOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
