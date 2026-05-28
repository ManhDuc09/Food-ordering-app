package com.example.backend.controller;

import com.example.backend.model.Order;
import com.example.backend.model.Payment;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final VNPayService vnpayService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createPayment(
            @RequestBody Map<String, String> body,
            HttpServletRequest request) {

        UUID orderId = UUID.fromString(body.get("orderId"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        long amount = order.getTotalAmount().longValue();
        String ipAddr = getClientIp(request);
        String paymentUrl = vnpayService.createPaymentUrl(orderId.toString(), amount, ipAddr);

        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(@RequestParam Map<String, String> params) {
        boolean validSignature = vnpayService.verifyReturn(params);
        String responseCode = params.getOrDefault("vnp_ResponseCode", "");
        String txnRef = params.getOrDefault("vnp_TxnRef", "");
        boolean success = validSignature && "00".equals(responseCode);

        if (success && !txnRef.isEmpty()) {
            try {
                Order order = orderRepository.findById(UUID.fromString(txnRef)).orElse(null);
                if (order != null) {
                    Payment payment = paymentRepository.findByOrder(order).orElse(null);
                    if (payment != null) {
                        payment.setStatus("paid");
                        payment.setPaidAt(LocalDateTime.now());
                        paymentRepository.save(payment);
                    }
                }
            } catch (Exception ignored) {}
        }

        return ResponseEntity.ok(Map.of(
                "success", success,
                "responseCode", responseCode,
                "orderId", txnRef
        ));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip != null && !ip.isEmpty()) ? ip.split(",")[0].trim() : request.getRemoteAddr();
    }
}
