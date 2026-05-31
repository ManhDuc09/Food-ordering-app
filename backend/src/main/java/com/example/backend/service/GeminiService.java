package com.example.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private static final String GEMINI_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    private static final String SYSTEM_PROMPT = """
        Bạn là trợ lý chăm sóc khách hàng thân thiện của KFC Việt Nam.
        Nhiệm vụ của bạn là giúp khách hàng với:
        - Thực đơn và sản phẩm (gà rán, burger, combo, đồ uống)
        - Quy trình đặt hàng trên website
        - Phương thức thanh toán: COD (tiền mặt khi nhận hàng), VNPay, MoMo
        - Thời gian giao hàng khoảng 30-45 phút
        - Chi nhánh và địa điểm
        - Theo dõi đơn hàng (hướng dẫn khách vào trang Hồ sơ để kiểm tra)
        Trả lời ngắn gọn, thân thiện (tối đa 3 câu).
        Trả lời bằng ngôn ngữ của khách hàng (tiếng Việt hoặc tiếng Anh).
        Nếu không biết thông tin cụ thể, hãy gợi ý khách xem trang thực đơn hoặc liên hệ trực tiếp.
        """;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String chat(List<Map<String, String>> messages) {
        try {
            List<Map<String, Object>> contents = messages.stream()
                .map(msg -> Map.<String, Object>of(
                    "role", "assistant".equals(msg.get("role")) ? "model" : "user",
                    "parts", List.of(Map.of("text", msg.get("content")))
                ))
                .collect(Collectors.toList());

            Map<String, Object> body = Map.of(
                "system_instruction", Map.of(
                    "parts", List.of(Map.of("text", SYSTEM_PROMPT))
                ),
                "contents", contents,
                "generationConfig", Map.of(
                    "maxOutputTokens", 350,
                    "temperature", 0.7
                )
            );

            String response = restClient.post()
                .uri(GEMINI_URL + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

            var root = objectMapper.readTree(response);
            return root.path("candidates").get(0)
                       .path("content").path("parts").get(0)
                       .path("text").asText("Xin lỗi, tôi không thể trả lời lúc này.");

        } catch (Exception e) {
            return "Xin lỗi, có lỗi xảy ra. Vui lòng thử lại sau.";
        }
    }
}
