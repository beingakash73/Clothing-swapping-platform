package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.PlatformKPIsDto;
import com.threadloop.marketplace.service.KpiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kpis")
public class KpiController {

    private final KpiService kpiService;

    public KpiController(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    @GetMapping
    public ResponseEntity<PlatformKPIsDto> getKPIs() {
        return ResponseEntity.ok(kpiService.getKPIs());
    }
}
