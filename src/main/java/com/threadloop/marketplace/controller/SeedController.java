package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.service.DataSeederService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final DataSeederService dataSeederService;

    public SeedController(DataSeederService dataSeederService) {
        this.dataSeederService = dataSeederService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> resetAndSeed() {
        dataSeederService.seedDatabase();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Database successfully reset and seeded to initial mock data.");
        return ResponseEntity.ok(response);
    }
}
