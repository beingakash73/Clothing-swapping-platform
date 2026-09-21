package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.ClothingItemDto;
import com.threadloop.marketplace.dto.CreateItemRequest;
import com.threadloop.marketplace.dto.UpdateItemRequest;
import com.threadloop.marketplace.model.ClothingItem;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.ClothingItemRepository;
import com.threadloop.marketplace.repository.UserRepository;
import com.threadloop.marketplace.util.EcoCalculator;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ClothingItemRepository clothingItemRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;

    public ItemService(ClothingItemRepository clothingItemRepository,
                       UserRepository userRepository,
                       MongoTemplate mongoTemplate) {
        this.clothingItemRepository = clothingItemRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<ClothingItemDto> getItems(String category, String condition, String brandTier,
                                          String gender, String status, String search, String ownerId) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("all")) {
            criteriaList.add(Criteria.where("category").is(category.trim()));
        }
        if (condition != null && !condition.trim().isEmpty() && !condition.equalsIgnoreCase("all")) {
            criteriaList.add(Criteria.where("condition").is(condition.trim()));
        }
        if (brandTier != null && !brandTier.trim().isEmpty() && !brandTier.equalsIgnoreCase("all")) {
            criteriaList.add(Criteria.where("brandTier").is(brandTier.trim()));
        }
        if (gender != null && !gender.trim().isEmpty() && !gender.equalsIgnoreCase("all")) {
            criteriaList.add(Criteria.where("gender").is(gender.trim()));
        }
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
            criteriaList.add(Criteria.where("status").is(status.trim()));
        }
        if (ownerId != null && !ownerId.trim().isEmpty()) {
            criteriaList.add(Criteria.where("owner.$id").is(ownerId.trim()));
        }
        if (search != null && !search.trim().isEmpty()) {
            String sanitized = Pattern.quote(search.trim());
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("title").regex(sanitized, "i"),
                    Criteria.where("brand").regex(sanitized, "i"),
                    Criteria.where("description").regex(sanitized, "i")
            );
            criteriaList.add(searchCriteria);
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));

        List<ClothingItem> items = mongoTemplate.find(query, ClothingItem.class);
        return items.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<ClothingItemDto> getItemById(String id) {
        return clothingItemRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public ClothingItemDto createItem(CreateItemRequest request) {
        String itemId = (request.getId() != null && !request.getId().isEmpty())
                ? request.getId()
                : "item_" + System.currentTimeMillis();

        String ownerId = (request.getOwnerId() != null && !request.getOwnerId().isEmpty())
                ? request.getOwnerId()
                : "user_maya";

        User owner = userRepository.findById(ownerId).orElseGet(() -> {
            List<User> all = userRepository.findAll();
            return all.isEmpty() ? null : all.get(0);
        });

        if (owner == null) {
            throw new RuntimeException("No valid owner found for item creation");
        }

        Double originalPrice = request.getOriginalPrice() != null ? request.getOriginalPrice() : 100.0;
        Double estimatedSwapValue = request.getEstimatedSwapValue() != null ? request.getEstimatedSwapValue() : (originalPrice * 0.6);

        EcoCalculator.EcoMetrics eco = EcoCalculator.calculateForPrice(originalPrice);
        Double co2 = request.getEcoSavedKgCo2() != null ? request.getEcoSavedKgCo2() : eco.getCo2SavedKg();
        Double water = request.getEcoSavedLitersWater() != null ? request.getEcoSavedLitersWater() : eco.getWaterSavedLiters();

        List<String> images = (request.getImages() != null && !request.getImages().isEmpty())
                ? request.getImages()
                : List.of("https://images.unsplash.com/photo-1523381210434-271e8be1f52b?auto=format&fit=crop&w=1000&q=80");

        List<String> tags = (request.getTags() != null && !request.getTags().isEmpty())
                ? request.getTags()
                : List.of("Pre-loved", "Sustainable");

        String createdAt = request.getCreatedAt() != null ? request.getCreatedAt() : Instant.now().toString();

        ClothingItem item = new ClothingItem(
                itemId,
                request.getTitle() != null ? request.getTitle() : "Untitled Garment",
                request.getDescription() != null ? request.getDescription() : "",
                request.getBrand() != null ? request.getBrand() : "Unbranded",
                request.getBrandTier() != null ? request.getBrandTier() : "high_street",
                request.getCategory() != null ? request.getCategory() : "Tops & Shirts",
                request.getSubcategory(),
                request.getSize() != null ? request.getSize() : "M",
                request.getGender() != null ? request.getGender() : "Unisex",
                request.getCondition() != null ? request.getCondition() : "gently_used",
                request.getConditionNotes(),
                request.getMaterial() != null ? request.getMaterial() : "Cotton blend",
                request.getColor() != null ? request.getColor() : "Neutral",
                originalPrice,
                estimatedSwapValue,
                images,
                owner,
                request.getStatus() != null ? request.getStatus() : "available",
                tags,
                co2,
                water,
                createdAt
        );

        ClothingItem saved = clothingItemRepository.save(item);
        return toDto(saved);
    }

    @Transactional
    public Optional<ClothingItemDto> updateItem(String id, UpdateItemRequest request) {
        return clothingItemRepository.findById(id).map(item -> {
            if (request.getTitle() != null) item.setTitle(request.getTitle());
            if (request.getDescription() != null) item.setDescription(request.getDescription());
            if (request.getBrand() != null) item.setBrand(request.getBrand());
            if (request.getBrandTier() != null) item.setBrandTier(request.getBrandTier());
            if (request.getCategory() != null) item.setCategory(request.getCategory());
            if (request.getSubcategory() != null) item.setSubcategory(request.getSubcategory());
            if (request.getSize() != null) item.setSize(request.getSize());
            if (request.getGender() != null) item.setGender(request.getGender());
            if (request.getCondition() != null) item.setCondition(request.getCondition());
            if (request.getConditionNotes() != null) item.setConditionNotes(request.getConditionNotes());
            if (request.getMaterial() != null) item.setMaterial(request.getMaterial());
            if (request.getColor() != null) item.setColor(request.getColor());
            if (request.getOriginalPrice() != null) item.setOriginalPrice(request.getOriginalPrice());
            if (request.getEstimatedSwapValue() != null) item.setEstimatedSwapValue(request.getEstimatedSwapValue());
            if (request.getImages() != null) item.setImages(request.getImages());
            if (request.getStatus() != null) item.setStatus(request.getStatus());
            if (request.getTags() != null) item.setTags(request.getTags());

            ClothingItem updated = clothingItemRepository.save(item);
            return toDto(updated);
        });
    }

    @Transactional
    public boolean deleteItem(String id) {
        if (clothingItemRepository.existsById(id)) {
            clothingItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ClothingItemDto toDto(ClothingItem item) {
        ClothingItemDto dto = new ClothingItemDto();
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
