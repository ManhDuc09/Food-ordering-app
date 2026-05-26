package com.example.backend.service;

import com.example.backend.dto.order.OrderDetailResponse;
import com.example.backend.model.Branch;
import com.example.backend.model.Order;
import com.example.backend.model.Payment;
import com.example.backend.model.User;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<OrderDetailResponse> getBranchOrders() {
        User manager = currentUser();
        Branch branch = manager.getBranch();
        if (branch == null) {
            throw new RuntimeException("Manager is not assigned to any branch");
        }

        return orderRepository.findByBranchOrderByCreatedAtDesc(branch).stream()
                .map(order -> {
                    Payment payment = paymentRepository.findByOrder(order).orElse(null);
                    return toDetailResponse(order, payment);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDetailResponse updateOrderStatus(UUID orderId, String newStatus) {
        User manager = currentUser();
        Branch branch = manager.getBranch();
        if (branch == null) {
            throw new RuntimeException("Manager is not assigned to any branch");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getBranch() == null || !order.getBranch().getBranchId().equals(branch.getBranchId())) {
            throw new RuntimeException("Order does not belong to your branch");
        }

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);
        Payment payment = paymentRepository.findByOrder(saved).orElse(null);
        return toDetailResponse(saved, payment);
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private OrderDetailResponse toDetailResponse(Order order, Payment payment) {
        OrderDetailResponse dto = new OrderDetailResponse();
        dto.setOrderId(order.getOrderId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setDeliveryName(order.getDeliveryName());
        dto.setDeliveryPhone(order.getDeliveryPhone());
        dto.setDeliveryAddress(order.getDeliveryAddress());

        if (order.getBranch() != null) {
            dto.setBranchName(order.getBranch().getName());
            dto.setBranchAddress(order.getBranch().getAddress());
        }

        if (order.getUser() != null) {
            dto.setCustomerEmail(order.getUser().getEmail());
        }

        if (payment != null) {
            dto.setPaymentId(payment.getId());
            dto.setPaymentMethod(payment.getMethod());
            dto.setPaymentStatus(payment.getStatus());
        }

        List<OrderDetailResponse.OrderItemDto> items = order.getItems().stream()
                .map(item -> new OrderDetailResponse.OrderItemDto(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice()
                ))
                .collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }
}
