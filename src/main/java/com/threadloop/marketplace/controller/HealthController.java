package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.HealthCheckDto;
import com.threadloop.marketplace.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final UserRepository userRepository;

    public HealthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<HealthCheckDto> checkHealth() {
        long count = userRepository.count();
        HealthCheckDto response = new HealthCheckDto(
                "ok",
                "connected",
                "Spring Data MongoDB",
                count,
                Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }
}
