package com.example.backend.service;

import com.example.backend.model.Branch;
import com.example.backend.model.Product;
import com.example.backend.repository.BranchRepository;
import com.example.backend.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroqService {

    private static final Logger log = LoggerFactory.getLogger(GroqService.class);

    @Value("${groq.api-key}")
    private String apiKey;

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";

    private static final String BASE_PROMPT = """
        Bạn là trợ lý chăm sóc khách hàng thân thiện của KFC Việt Nam.
        Trả lời ngắn gọn (tối đa 3 câu), thân thiện.
        Trả lời bằng ngôn ngữ của khách hàng (tiếng Việt hoặc tiếng Anh).
        Phương thức thanh toán: COD (tiền mặt khi nhận), VNPay, MoMo.
        Thời gian giao hàng: 30-45 phút.
        Theo dõi đơn hàng: hướng dẫn khách vào trang Hồ sơ.
        """;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private volatile String cachedContext = null;
    private volatile long cacheTime = 0;
    private static final long CACHE_TTL_MS = 5 * 60 * 1000;

    private String buildSystemPrompt() {
        long now = System.currentTimeMillis();
        if (cachedContext != null && (now - cacheTime) < CACHE_TTL_MS) {
            return cachedContext;
        }
        StringBuilder sb = new StringBuilder(BASE_PROMPT);

        try {
            List<Product> products = productRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsAvailable()))
                .collect(Collectors.toList());

            if (!products.isEmpty()) {
                sb.append("\n=== THỰC ĐƠN HIỆN TẠI ===\n");
                NumberFormat fmt = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"));
                for (Product p : products) {
                    sb.append("- ").append(p.getName());
                    if (p.getPrice() != null) {
                        sb.append(": ").append(fmt.format(p.getPrice())).append("đ");
                    }
                    if (p.getDescription() != null && !p.getDescription().isBlank()) {
                        sb.append(" (").append(p.getDescription().trim()).append(")");
                    }
                    sb.append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Could not load products for context: {}", e.getMessage());
        }

        try {
            List<Branch> branches = branchRepository.findAll();
            if (!branches.isEmpty()) {
                sb.append("\n=== CHI NHÁNH ===\n");
                for (Branch b : branches) {
                    sb.append("- ").append(b.getName());
                    if (b.getAddress() != null) sb.append(": ").append(b.getAddress());
                    sb.append(Boolean.TRUE.equals(b.getIsOpen()) ? " [Đang mở]" : " [Đóng cửa]");
                    sb.append("\n");
                }
            }
        } catch (Exception e) {
            log.warn("Could not load branches for context: {}", e.getMessage());
        }

        cachedContext = sb.toString();
        cacheTime = System.currentTimeMillis();
        return cachedContext;
    }

    public String chat(List<Map<String, String>> messages) {
        try {
            List<Map<String, String>> groqMessages = new ArrayList<>();
            groqMessages.add(Map.of("role", "system", "content", buildSystemPrompt()));
            groqMessages.addAll(
                messages.stream()
                    .map(m -> Map.of(
                        "role", "assistant".equals(m.get("role")) ? "assistant" : "user",
                        "content", m.get("content")
                    ))
                    .collect(Collectors.toList())
            );

            Map<String, Object> body = Map.of(
                "model", MODEL,
                "messages", groqMessages,
                "max_tokens", 350,
                "temperature", 0.7
            );

            String response = restClient.post()
                .uri(GROQ_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .body(body)
                .retrieve()
                .body(String.class);

            var root = objectMapper.readTree(response);
            return root.path("choices").get(0)
                       .path("message").path("content").asText("Xin lỗi, tôi không thể trả lời lúc này.");

        } catch (Exception e) {
            log.error("Groq API error: {}", e.getMessage(), e);
            return "Xin lỗi, có lỗi xảy ra. Vui lòng thử lại sau.";
        }
    }
}
