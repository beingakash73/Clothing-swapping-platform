package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.MeetupLocationDto;
import com.threadloop.marketplace.service.HubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hubs")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    @GetMapping
    public ResponseEntity<List<MeetupLocationDto>> getHubs() {
        return ResponseEntity.ok(hubService.getAllHubs());
    }
}
