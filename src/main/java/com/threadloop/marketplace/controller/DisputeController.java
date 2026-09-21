package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.CreateDisputeRequest;
import com.threadloop.marketplace.dto.DisputeDto;
import com.threadloop.marketplace.dto.ResolveDisputeRequest;
import com.threadloop.marketplace.service.DisputeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disputes")
public class DisputeController {

    private final DisputeService disputeService;

    public DisputeController(DisputeService disputeService) {
        this.disputeService = disputeService;
    }

    @GetMapping
    public ResponseEntity<List<DisputeDto>> getAllDisputes() {
        return ResponseEntity.ok(disputeService.getAllDisputes());
    }

    @PostMapping
    public ResponseEntity<DisputeDto> createDispute(@RequestBody CreateDisputeRequest request) {
        DisputeDto created = disputeService.createDispute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DisputeDto> resolveDispute(
            @PathVariable String id,
            @RequestBody ResolveDisputeRequest request
    ) {
        return disputeService.resolveDispute(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
