package com.example.backend.service;

import com.example.backend.dto.order.OrderDetailResponse;
import com.example.backend.dto.order.OrderRequest;
import com.example.backend.dto.order.OrderResponse;
import com.example.backend.model.Branch;
import com.example.backend.model.Order;
import com.example.backend.model.Payment;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.BranchRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private OrderService orderService;

    private User currentUser;

    @BeforeEach
    void setUpSecurityContext() {
        currentUser = new User();
        currentUser.setUserId(UUID.randomUUID());
        currentUser.setEmail("user@example.com");
        currentUser.setRole("customer");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@example.com", null, List.of()));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // ── createOrder ──────────────────────────────────────────────────────────

    @Test
    void createOrder_validRequest_savesOrderAndPayment() {
        UUID productId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        Product product = new Product();
        product.setProductId(productId);
        product.setName("Zinger");
        product.setPrice(BigDecimal.valueOf(50000));

        Branch branch = new Branch();
        branch.setBranchId(branchId);
        branch.setName("District 1");

        Order savedOrder = new Order();
        savedOrder.setOrderId(UUID.randomUUID());
        savedOrder.setStatus("pending");
        savedOrder.setTotalAmount(BigDecimal.valueOf(100000));
        savedOrder.setUser(currentUser);
        savedOrder.setBranch(branch);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
        when(productRepository.findByProductId(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderRequest request = buildOrderRequest(productId, 2, branchId);
        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getStatus()).isEqualTo("pending");
        assertThat(response.getTotalAmount()).isEqualTo(BigDecimal.valueOf(100000));
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void createOrder_emptyItems_throwsException() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));

        OrderRequest request = new OrderRequest();
        request.setItems(List.of());
        request.setBranchId(UUID.randomUUID());

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("at least one item");
    }

    @Test
    void createOrder_nullBranchId_throwsException() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));

        OrderRequest.OrderItemRequest item = new OrderRequest.OrderItemRequest();
        item.setProductId(UUID.randomUUID());
        item.setQuantity(1);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));
        request.setBranchId(null);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Branch is required");
    }

    @Test
    void createOrder_invalidQuantity_throwsException() {
        UUID branchId = UUID.randomUUID();
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(new Branch()));

        OrderRequest.OrderItemRequest item = new OrderRequest.OrderItemRequest();
        item.setProductId(UUID.randomUUID());
        item.setQuantity(0);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));
        request.setBranchId(branchId);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid item quantity");
    }

    @Test
    void createOrder_productNotFound_throwsException() {
        UUID productId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(branchRepository.findById(branchId)).thenReturn(Optional.of(new Branch()));
        when(productRepository.findByProductId(productId)).thenReturn(Optional.empty());

        OrderRequest request = buildOrderRequest(productId, 1, branchId);

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");
    }

    // ── cancelOrder ──────────────────────────────────────────────────────────

    @Test
    void cancelOrder_pendingOrder_setsStatusCancelled() {
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId, currentUser);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder(order)).thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);

        OrderDetailResponse response = orderService.cancelOrder(orderId);

        assertThat(response.getStatus()).isEqualTo("cancelled");
    }

    @Test
    void cancelOrder_alreadyCancelled_throwsException() {
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId, currentUser);
        order.setStatus("cancelled");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder(orderId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void cancelOrder_deliveredOrder_throwsException() {
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId, currentUser);
        order.setStatus("delivered");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder(orderId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void cancelOrder_differentUser_throwsUnauthorized() {
        UUID orderId = UUID.randomUUID();
        User otherUser = new User();
        otherUser.setUserId(UUID.randomUUID());

        Order order = pendingOrder(orderId, otherUser);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.cancelOrder(orderId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unauthorized");
    }

    // ── updatePaymentMethod ───────────────────────────────────────────────────

    @Test
    void updatePaymentMethod_toCOD_setsStatusPaid() {
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId, currentUser);

        Payment payment = new Payment();
        payment.setMethod("VNPAY");
        payment.setStatus("pending");
        payment.setOrder(order);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder(order)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        OrderDetailResponse response = orderService.updatePaymentMethod(orderId, "cod");

        assertThat(response.getPaymentMethod()).isEqualTo("COD");
        assertThat(response.getPaymentStatus()).isEqualTo("paid");
    }

    @Test
    void updatePaymentMethod_toVNPay_keepsStatusPending() {
        UUID orderId = UUID.randomUUID();
        Order order = pendingOrder(orderId, currentUser);

        Payment payment = new Payment();
        payment.setMethod("COD");
        payment.setStatus("paid");
        payment.setOrder(order);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder(order)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        OrderDetailResponse response = orderService.updatePaymentMethod(orderId, "vnpay");

        assertThat(response.getPaymentMethod()).isEqualTo("VNPAY");
        assertThat(response.getPaymentStatus()).isEqualTo("pending");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Order pendingOrder(UUID orderId, User owner) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus("pending");
        order.setTotalAmount(BigDecimal.valueOf(50000));
        order.setUser(owner);
        return order;
    }

    private OrderRequest buildOrderRequest(UUID productId, int quantity, UUID branchId) {
        OrderRequest.OrderItemRequest item = new OrderRequest.OrderItemRequest();
        item.setProductId(productId);
        item.setQuantity(quantity);

        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));
        request.setBranchId(branchId);
        request.setPaymentMethod("COD");
        request.setFullName("John Doe");
        request.setPhone("0123456789");
        request.setAddress("123 Main St");
        return request;
    }
}
