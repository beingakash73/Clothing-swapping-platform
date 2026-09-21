package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.ClothingItemDto;
import com.threadloop.marketplace.dto.CreateItemRequest;
import com.threadloop.marketplace.dto.UpdateItemRequest;
import com.threadloop.marketplace.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ClothingItemDto>> getItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String brandTier,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ownerId
    ) {
        List<ClothingItemDto> items = itemService.getItems(category, condition, brandTier, gender, status, search, ownerId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothingItemDto> getItemById(@PathVariable String id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClothingItemDto> createItem(@RequestBody CreateItemRequest request) {
        ClothingItemDto created = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClothingItemDto> updateItem(@PathVariable String id, @RequestBody UpdateItemRequest request) {
        return itemService.updateItem(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable String id) {
        boolean deleted = itemService.deleteItem(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("success", true);
            response.put("message", "Item deleted successfully");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "Item not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
