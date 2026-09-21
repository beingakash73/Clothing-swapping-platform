package com.threadloop.marketplace.dto;

import java.util.List;

public class CreateItemRequest {
    private String id;
    private String title;
    private String description;
    private String brand;
    private String brandTier;
    private String category;
    private String subcategory;
    private String size;
    private String gender;
    private String condition;
    private String conditionNotes;
    private String material;
    private String color;
    private Double originalPrice;
    private Double estimatedSwapValue;
    private List<String> images;
    private String ownerId;
    private String status;
    private List<String> tags;
    private Double ecoSavedKgCo2;
    private Double ecoSavedLitersWater;
    private String createdAt;

    public CreateItemRequest() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getBrandTier() { return brandTier; }
    public void setBrandTier(String brandTier) { this.brandTier = brandTier; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getConditionNotes() { return conditionNotes; }
    public void setConditionNotes(String conditionNotes) { this.conditionNotes = conditionNotes; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Double originalPrice) { this.originalPrice = originalPrice; }

    public Double getEstimatedSwapValue() { return estimatedSwapValue; }
    public void setEstimatedSwapValue(Double estimatedSwapValue) { this.estimatedSwapValue = estimatedSwapValue; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Double getEcoSavedKgCo2() { return ecoSavedKgCo2; }
    public void setEcoSavedKgCo2(Double ecoSavedKgCo2) { this.ecoSavedKgCo2 = ecoSavedKgCo2; }

    public Double getEcoSavedLitersWater() { return ecoSavedLitersWater; }
    public void setEcoSavedLitersWater(Double ecoSavedLitersWater) { this.ecoSavedLitersWater = ecoSavedLitersWater; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
