package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.ClothesUploadDto;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/clothes")
public class ClothesUploadController {

    private final UploadService uploadService;

    public ClothesUploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/list")
    public ResponseEntity<ClothesUploadDto> listClothes(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("size") String size,
            @RequestParam("gender") String gender,
            @RequestParam("condition") String condition,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "brandTier", required = false) String brandTier,
            @RequestParam(value = "subcategory", required = false) String subcategory,
            @RequestParam(value = "conditionNotes", required = false) String conditionNotes,
            @RequestParam(value = "material", required = false) String material,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "originalPrice", required = false) Double originalPrice,
            @RequestParam(value = "estimatedSwapValue", required = false) Double estimatedSwapValue,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestPart("files") List<MultipartFile> files,

            // For now, pass ownerId from frontend; later replace with JWT/auth
            @RequestParam("ownerId") String ownerId) throws IOException {

        // Build DTO from request
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

        // Temporary user object; later get from security context
        User owner = new User();
        owner.setId(ownerId);
        // set name, city, etc. if you have them

        ClothesUploadDto result = uploadService.uploadClothesListing(dto, files, owner);
        return ResponseEntity.ok(result);
    }
}