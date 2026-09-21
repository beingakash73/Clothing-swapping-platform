package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.ProposeSwapRequest;
import com.threadloop.marketplace.dto.SwapProposalDto;
import com.threadloop.marketplace.dto.SwapStatusUpdateRequest;
import com.threadloop.marketplace.service.SwapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swaps")
public class SwapController {

    private final SwapService swapService;

    public SwapController(SwapService swapService) {
        this.swapService = swapService;
    }

    @GetMapping
    public ResponseEntity<List<SwapProposalDto>> getSwaps(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status
    ) {
        List<SwapProposalDto> swaps = swapService.getSwaps(userId, status);
        return ResponseEntity.ok(swaps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SwapProposalDto> getSwapById(@PathVariable String id) {
        return swapService.getSwapById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SwapProposalDto> proposeSwap(@RequestBody ProposeSwapRequest request) {
        SwapProposalDto created = swapService.proposeSwap(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SwapProposalDto> updateSwapStatus(
            @PathVariable String id,
            @RequestBody SwapStatusUpdateRequest request
    ) {
        return swapService.updateSwapStatus(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
