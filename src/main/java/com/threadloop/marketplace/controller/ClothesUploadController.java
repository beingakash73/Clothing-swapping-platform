package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.ClothesUploadDto;
import com.threadloop.marketplace.service.UploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clothes")
public class ClothesUploadController {

    private final UploadService uploadService;

    public ClothesUploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping
    public ResponseEntity<List<ClothesUploadDto>> getAllClothes() {
        return ResponseEntity.ok(uploadService.getAllClothes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClothesUploadDto> getClothesById(@PathVariable String id) {
        return uploadService.getClothesById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = { "", "/list", "/upload" }, consumes = { "multipart/form-data" })
    public ResponseEntity<ClothesUploadDto> listClothesMultipart(
            @RequestParam(value = "title", required = false, defaultValue = "Untitled Garment") String title,
            @RequestParam(value = "description", required = false, defaultValue = "") String description,
            @RequestParam(value = "category", required = false, defaultValue = "Tops & Shirts") String category,
            @RequestParam(value = "size", required = false, defaultValue = "M") String size,
            @RequestParam(value = "gender", required = false, defaultValue = "Unisex") String gender,
            @RequestParam(value = "condition", required = false, defaultValue = "gently_used") String condition,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "brandTier", required = false) String brandTier,
            @RequestParam(value = "subcategory", required = false) String subcategory,
            @RequestParam(value = "conditionNotes", required = false) String conditionNotes,
            @RequestParam(value = "material", required = false) String material,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "originalPrice", required = false) Double originalPrice,
            @RequestParam(value = "estimatedSwapValue", required = false) Double estimatedSwapValue,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "file", required = false) MultipartFile singleFile,
            @RequestParam(value = "ownerId", required = false) String ownerId) throws IOException {

        ClothesUploadDto dto = new ClothesUploadDto();
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setCategory(category);
        dto.setSize(size);
        dto.setGender(gender);
        dto.setCondition(condition);
        dto.setBrand(brand);
        dto.setBrandTier(brandTier);
        dto.setSubcategory(subcategory);
        dto.setConditionNotes(conditionNotes);
        dto.setMaterial(material);
        dto.setColor(color);
        dto.setOriginalPrice(originalPrice);
        dto.setEstimatedSwapValue(estimatedSwapValue);
        dto.setTags(tags);

        List<MultipartFile> allFiles = new ArrayList<>();
        if (files != null) {
            allFiles.addAll(files);
        }
        if (singleFile != null && !singleFile.isEmpty()) {
            allFiles.add(singleFile);
        }

        ClothesUploadDto result = uploadService.uploadClothesListing(dto, allFiles, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ClothesUploadDto> listClothesJson(@RequestBody ClothesUploadDto dto) {
        ClothesUploadDto result = uploadService.uploadClothesListing(dto, null, dto.getOwnerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteClothes(@PathVariable String id) {
        boolean deleted = uploadService.deleteClothes(id);
        Map<String, Object> response = new HashMap<>();
        if (deleted) {
            response.put("success", true);
            response.put("message", "Listing deleted successfully");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "Listing not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}