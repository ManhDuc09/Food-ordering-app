package com.example.backend.service;

import com.example.backend.dto.order.OrderDetailResponse;
import com.example.backend.dto.order.OrderRequest;
import com.example.backend.dto.order.OrderResponse;
import com.example.backend.model.Branch;
import com.example.backend.model.Order;
import com.example.backend.model.OrderItem;
import com.example.backend.model.Payment;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.BranchRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.enums.OrderStatus;
import com.example.backend.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        if (request.getBranchId() == null) {
            throw new RuntimeException("Branch is required");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING.value);
        order.setDeliveryName(request.getFullName());
        order.setDeliveryPhone(request.getPhone());
        order.setDeliveryAddress(request.getAddress());

        if (request.getBranchId() != null) {
            Branch branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            order.setBranch(branch);
        }

        Set<OrderItem> items = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new RuntimeException("Invalid item quantity");
            }

            Product product = productRepository.findByProductId(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            items.add(orderItem);

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setItems(items);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created: {} for user: {}", savedOrder.getOrderId(), email);

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setMethod(request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : "COD");
        payment.setStatus(PaymentStatus.PENDING.value);
        payment.setPaidAt(null);
        paymentRepository.save(payment);

        return new OrderResponse(savedOrder.getOrderId(), savedOrder.getStatus(), savedOrder.getTotalAmount());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMyOrders(int page, int size) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Page<Order> orderPage = orderRepository.findByUserOrderByCreatedAtDesc(
                user, PageRequest.of(page, size));

        List<OrderDetailResponse> orders = orderPage.getContent().stream()
                .map(order -> {
                    Payment payment = paymentRepository.findByOrder(order).orElse(null);
                    return toDetailResponse(order, payment);
                })
                .collect(Collectors.toList());

        return Map.of(
                "orders", orders,
                "hasMore", !orderPage.isLast(),
                "page", page
        );
    }

    @Transactional
    public OrderDetailResponse updatePaymentMethod(UUID orderId, String newMethod) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            log.warn("Unauthorized payment update attempt by {} on order {}", email, orderId);
            throw new RuntimeException("Unauthorized");
        }

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        log.info("Updating payment method for order {} to {}", orderId, newMethod);
        payment.setMethod(newMethod.toUpperCase());
        payment.setStatus("COD".equals(payment.getMethod()) ? PaymentStatus.PAID.value : PaymentStatus.PENDING.value);
        if ("COD".equals(payment.getMethod())) {
            payment.setPaidAt(LocalDateTime.now());
        }
        paymentRepository.save(payment);

        return toDetailResponse(order, payment);
    }

    @Transactional
    public OrderDetailResponse cancelOrder(UUID orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            log.warn("Unauthorized cancel attempt by {} on order {}", email, orderId);
            throw new RuntimeException("Unauthorized");
        }

        if (OrderStatus.CANCELLED.value.equals(order.getStatus()) || OrderStatus.DELIVERED.value.equals(order.getStatus())) {
            throw new RuntimeException("Không thể hủy đơn hàng này");
        }

        log.info("Order {} cancelled by {}", orderId, email);
        order.setStatus(OrderStatus.CANCELLED.value);
        orderRepository.save(order);

        Payment payment = paymentRepository.findByOrder(order).orElse(null);
        return toDetailResponse(order, payment);
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
