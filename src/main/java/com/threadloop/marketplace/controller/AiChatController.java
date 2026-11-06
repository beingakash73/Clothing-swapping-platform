package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.ai.services.AiChatService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ThreadLoop Spring AI Assistant",
                "model", "qwen2.5:3b",
                "provider", "ollama",
                "mcpToolsEnabled", true
        ));
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(
            @RequestBody Map<String, String> request) {

        String message = request.get("message");

        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Message cannot be empty"));
        }

        String answer = aiChatService.chat(message);

        return ResponseEntity.ok(
                Map.of("answer", answer));
    }
}
