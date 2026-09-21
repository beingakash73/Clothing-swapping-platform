
package com.threadloop.marketplace.model;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clothes_upload")
public class ClothesUpload {

    @Id
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
    private String imageUrl;
    private String imagePublicId;

    private List<String> images = new ArrayList<>();

    @DBRef
    private User owner;

    private String status = "available"; // 'available' | 'in_negotiation' | 'swapped' | 'reserved'
    private List<String> tags = new ArrayList<>();
    private Double ecoSavedKgCo2 = 0.0;
    private Double ecoSavedLitersWater = 0.0;
    private String createdAt;

    public ClothesUpload() {
    }

    public ClothesUpload(String id, String title, String imageUrl, String imagePublicId, String description,
            String brand, String brandTier,
            String category, String subcategory, String size, String gender, String condition,
            String conditionNotes, String material, String color, Double originalPrice,
            Double estimatedSwapValue, List<String> images, User owner, String status,
            List<String> tags, Double ecoSavedKgCo2, Double ecoSavedLitersWater, String createdAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.imagePublicId = imagePublicId;
        this.description = description;
        this.brand = brand;
        this.brandTier = brandTier;
        this.category = category;
        this.subcategory = subcategory;
        this.size = size;
        this.gender = gender;
        this.condition = condition;
        this.conditionNotes = conditionNotes;
        this.material = material;
        this.color = color;
        this.originalPrice = originalPrice;
        this.estimatedSwapValue = estimatedSwapValue;
        this.images = images != null ? images : new ArrayList<>();
        this.owner = owner;
        this.status = status != null ? status : "available";
        this.tags = tags != null ? tags : new ArrayList<>();
        this.ecoSavedKgCo2 = ecoSavedKgCo2 != null ? ecoSavedKgCo2 : 0.0;
        this.ecoSavedLitersWater = ecoSavedLitersWater != null ? ecoSavedLitersWater : 0.0;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandTier() {
        return brandTier;
    }

    public void setBrandTier(String brandTier) {
        this.brandTier = brandTier;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditionNotes() {
        return conditionNotes;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getEstimatedSwapValue() {
        return estimatedSwapValue;
    }

    public void setEstimatedSwapValue(Double estimatedSwapValue) {
        this.estimatedSwapValue = estimatedSwapValue;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Double getEcoSavedKgCo2() {
        return ecoSavedKgCo2;
    }

    public void setEcoSavedKgCo2(Double ecoSavedKgCo2) {
        this.ecoSavedKgCo2 = ecoSavedKgCo2;
    }

    public Double getEcoSavedLitersWater() {
        return ecoSavedLitersWater;
    }

    public void setEcoSavedLitersWater(Double ecoSavedLitersWater) {
        this.ecoSavedLitersWater = ecoSavedLitersWater;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
