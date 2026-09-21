package com.threadloop.marketplace.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.threadloop.marketplace.dto.ClothesUploadDto;
import com.threadloop.marketplace.model.ClothesUpload;
import com.threadloop.marketplace.model.ClothingItem;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.ClothesUploadRepository;
import com.threadloop.marketplace.repository.ClothingItemRepository;
import com.threadloop.marketplace.repository.UserRepository;
import com.threadloop.marketplace.util.EcoCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service
public class UploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadService.class);
    private static final String DEFAULT_IMAGE = "https://images.unsplash.com/photo-1523381210434-271e8be1f52b?auto=format&fit=crop&w=1000&q=80";

    private final ClothesUploadRepository clothesUploadRepository;
    private final ClothingItemRepository clothingItemRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    public UploadService(ClothesUploadRepository clothesUploadRepository,
                         ClothingItemRepository clothingItemRepository,
                         UserRepository userRepository,
                         Cloudinary cloudinary) {
        this.clothesUploadRepository = clothesUploadRepository;
        this.clothingItemRepository = clothingItemRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }

    /**
     * Upload images to Cloudinary and persist listing to both clothes_upload
     * and the central clothing_items repository so the entire platform stays in sync.
     */
    @Transactional
    public ClothesUploadDto uploadClothesListing(
            ClothesUploadDto dto,
            List<MultipartFile> files,
            String ownerId) {

        List<String> imageUrls = new ArrayList<>();
        List<String> publicIds = new ArrayList<>();

        // 1) Upload images to Cloudinary if provided
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> result = cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap("folder", "threadloop/clothes")
                    );
                    String url = (String) result.get("secure_url");
                    String publicId = (String) result.get("public_id");
                    if (url != null) {
                        imageUrls.add(url);
                    }
                    if (publicId != null) {
                        publicIds.add(publicId);
                    }
                } catch (Exception e) {
                    log.error("Cloudinary upload failed for file {}: {}", file.getOriginalFilename(), e.getMessage());
                }
            }
        }

        // Retain any existing images passed in the DTO
        if (imageUrls.isEmpty() && dto.getImages() != null && !dto.getImages().isEmpty()) {
            imageUrls.addAll(dto.getImages());
        }
        if (imageUrls.isEmpty() && dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            imageUrls.add(dto.getImageUrl());
        }
        // Fallback default image if none provided
        if (imageUrls.isEmpty()) {
            imageUrls.add(DEFAULT_IMAGE);
        }

        // 2) Resolve Owner
        User owner = null;
        if (ownerId != null && !ownerId.isBlank()) {
            owner = userRepository.findById(ownerId).orElse(null);
        }
        if (owner == null) {
            owner = userRepository.findById("user_maya").orElseGet(() -> {
                List<User> users = userRepository.findAll();
                return users.isEmpty() ? null : users.get(0);
            });
        }

        // 3) Calculate Eco Impact and Values
        Double originalPrice = dto.getOriginalPrice() != null && dto.getOriginalPrice() > 0
                ? dto.getOriginalPrice()
                : 100.0;
        Double estimatedSwapValue = dto.getEstimatedSwapValue() != null
                ? dto.getEstimatedSwapValue()
                : Math.round(originalPrice * 0.6 * 10.0) / 10.0;

        EcoCalculator.EcoMetrics eco = EcoCalculator.calculateForPrice(originalPrice);
        Double co2 = dto.getEcoSavedKgCo2() != null ? dto.getEcoSavedKgCo2() : eco.getCo2SavedKg();
        Double water = dto.getEcoSavedLitersWater() != null ? dto.getEcoSavedLitersWater() : eco.getWaterSavedLiters();

        String itemId = (dto.getId() != null && !dto.getId().isBlank())
                ? dto.getId()
                : "item_" + System.currentTimeMillis();
        String createdAt = dto.getCreatedAt() != null ? dto.getCreatedAt() : Instant.now().toString();

        List<String> tags = (dto.getTags() != null && !dto.getTags().isEmpty())
                ? dto.getTags()
                : List.of("Pre-loved", "Sustainable");

        // 4) Build and Save ClothesUpload Entity
        ClothesUpload entity = new ClothesUpload();
        entity.setId(itemId);
        entity.setTitle(dto.getTitle() != null && !dto.getTitle().isBlank() ? dto.getTitle() : "Untitled Garment");
        entity.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        entity.setBrand(dto.getBrand() != null ? dto.getBrand() : "Unbranded");
        entity.setBrandTier(dto.getBrandTier() != null ? dto.getBrandTier() : "high_street");
        entity.setCategory(dto.getCategory() != null ? dto.getCategory() : "Tops & Shirts");
        entity.setSubcategory(dto.getSubcategory());
        entity.setSize(dto.getSize() != null ? dto.getSize() : "M");
        entity.setGender(dto.getGender() != null ? dto.getGender() : "Unisex");
        entity.setCondition(dto.getCondition() != null ? dto.getCondition() : "gently_used");
        entity.setConditionNotes(dto.getConditionNotes());
        entity.setMaterial(dto.getMaterial() != null ? dto.getMaterial() : "Cotton blend");
        entity.setColor(dto.getColor() != null ? dto.getColor() : "Neutral");
        entity.setOriginalPrice(originalPrice);
        entity.setEstimatedSwapValue(estimatedSwapValue);
        entity.setTags(tags);
        entity.setImages(imageUrls);
        entity.setImageUrl(imageUrls.get(0));
        entity.setImagePublicId(publicIds.isEmpty() ? null : publicIds.get(0));
        entity.setOwner(owner);
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : "available");
        entity.setEcoSavedKgCo2(co2);
        entity.setEcoSavedLitersWater(water);
        entity.setCreatedAt(createdAt);

        ClothesUpload saved = clothesUploadRepository.save(entity);

        // 5) Synchronize with central ClothingItem repository for full platform integration
        ClothingItem item = new ClothingItem(
                itemId,
                entity.getTitle(),
                entity.getDescription(),
                entity.getBrand(),
                entity.getBrandTier(),
                entity.getCategory(),
                entity.getSubcategory(),
                entity.getSize(),
                entity.getGender(),
                entity.getCondition(),
                entity.getConditionNotes(),
                entity.getMaterial(),
                entity.getColor(),
                originalPrice,
                estimatedSwapValue,
                imageUrls,
                owner,
                entity.getStatus(),
                tags,
                co2,
                water,
                createdAt
        );
        clothingItemRepository.save(item);

        return toDto(saved);
    }

    public List<ClothesUploadDto> getAllClothes() {
        return clothesUploadRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<ClothesUploadDto> getClothesById(String id) {
        return clothesUploadRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public boolean deleteClothes(String id) {
        Optional<ClothesUpload> optional = clothesUploadRepository.findById(id);
        if (optional.isPresent()) {
            ClothesUpload item = optional.get();
            if (item.getImagePublicId() != null && !item.getImagePublicId().isBlank()) {
                try {
                    cloudinary.uploader().destroy(item.getImagePublicId(), ObjectUtils.emptyMap());
                } catch (Exception e) {
                    log.warn("Failed to delete image {} from Cloudinary: {}", item.getImagePublicId(), e.getMessage());
                }
            }
            clothesUploadRepository.deleteById(id);
            clothingItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ClothesUploadDto toDto(ClothesUpload item) {
        ClothesUploadDto dto = new ClothesUploadDto();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setBrand(item.getBrand());
        dto.setBrandTier(item.getBrandTier());
        dto.setCategory(item.getCategory());
        dto.setSubcategory(item.getSubcategory());
        dto.setSize(item.getSize());
        dto.setGender(item.getGender());
        dto.setCondition(item.getCondition());
        dto.setConditionNotes(item.getConditionNotes());
        dto.setMaterial(item.getMaterial());
        dto.setColor(item.getColor());
        dto.setOriginalPrice(item.getOriginalPrice());
        dto.setEstimatedSwapValue(item.getEstimatedSwapValue());
        dto.setImages(item.getImages() != null ? item.getImages() : new ArrayList<>());
        dto.setImageUrl(item.getImageUrl());
        dto.setImagePublicId(item.getImagePublicId());
        dto.setStatus(item.getStatus());
        dto.setTags(item.getTags() != null ? item.getTags() : new ArrayList<>());
        dto.setEcoSavedKgCo2(item.getEcoSavedKgCo2());
        dto.setEcoSavedLitersWater(item.getEcoSavedLitersWater());
        dto.setCreatedAt(item.getCreatedAt());

        if (item.getOwner() != null) {
            User o = item.getOwner();
            dto.setOwnerId(o.getId());
            dto.setOwnerName(o.getName());
            dto.setOwnerAvatar(o.getAvatar());
            if (o.getLocation() != null) {
                dto.setOwnerCity(o.getLocation().getCity());
                dto.setOwnerState(o.getLocation().getState());
                Map<String, Double> coords = new HashMap<>();
                coords.put("lat", o.getLocation().getLat() != null ? o.getLocation().getLat() : 40.7128);
                coords.put("lng", o.getLocation().getLng() != null ? o.getLocation().getLng() : -74.006);
                dto.setCoordinates(coords);
            }
            dto.setOwnerRating(o.getRating());
            dto.setOwnerSwapsCount(o.getCompletedSwaps());
        }

        return dto;
    }
}