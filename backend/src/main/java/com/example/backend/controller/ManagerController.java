package com.example.backend.controller;

import com.example.backend.dto.order.OrderDetailResponse;
import com.example.backend.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDetailResponse>> getBranchOrders() {
        return ResponseEntity.ok(managerService.getBranchOrders());
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderDetailResponse> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        return ResponseEntity.ok(managerService.updateOrderStatus(orderId, status));
    }
}
