package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.ChatMessageDto;
import com.threadloop.marketplace.dto.SendMessageRequest;
import com.threadloop.marketplace.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<ChatMessageDto>> getMessages(@RequestParam(required = false) String swapId) {
        List<ChatMessageDto> messages = messageService.getMessages(swapId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<ChatMessageDto> sendMessage(@RequestBody SendMessageRequest request) {
        ChatMessageDto sent = messageService.sendMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sent);
    }
}
