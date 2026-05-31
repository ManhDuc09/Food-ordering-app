package com.example.backend.controller;

import com.example.backend.service.GroqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final GroqService groqService;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> messages = (List<Map<String, String>>) body.get("messages");
        String reply = groqService.chat(messages);
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
