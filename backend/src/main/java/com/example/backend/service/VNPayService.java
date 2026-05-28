package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {

    @Value("${vnpay.tmn-code}")
    private String tmnCode;

    @Value("${vnpay.hash-secret}")
    private String hashSecret;

    @Value("${vnpay.url}")
    private String vnpUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    public String createPaymentUrl(String orderId, long amount, String ipAddr) {
        String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String orderInfo = "Thanh toan don hang KFC " + orderId.substring(0, 8).toUpperCase();

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", String.valueOf(amount * 100));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", ipAddr);
        params.put("vnp_CreateDate", createDate);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> e = it.next();
            String encodedValue = URLEncoder.encode(e.getValue(), StandardCharsets.US_ASCII);
            hashData.append(e.getKey()).append('=').append(encodedValue);
            query.append(URLEncoder.encode(e.getKey(), StandardCharsets.US_ASCII)).append('=').append(encodedValue);
            if (it.hasNext()) { hashData.append('&'); query.append('&'); }
        }

        String secureHash = hmacSHA512(hashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);
        return vnpUrl + "?" + query;
    }

    public boolean verifyReturn(Map<String, String> params) {
        String secureHash = params.get("vnp_SecureHash");
        if (secureHash == null) return false;

        Map<String, String> verifyParams = new TreeMap<>(params);
        verifyParams.remove("vnp_SecureHash");
        verifyParams.remove("vnp_SecureHashType");

        StringBuilder hashData = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = verifyParams.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> e = it.next();
            if (e.getValue() != null && !e.getValue().isEmpty()) {
                hashData.append(e.getKey()).append('=')
                        .append(URLEncoder.encode(e.getValue(), StandardCharsets.US_ASCII));
                if (it.hasNext()) hashData.append('&');
            }
        }

        return hmacSHA512(hashSecret, hashData.toString()).equalsIgnoreCase(secureHash);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA512 error", e);
        }
    }
}
