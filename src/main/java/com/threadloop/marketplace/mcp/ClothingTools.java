package com.threadloop.marketplace.mcp;

import com.threadloop.marketplace.model.ClothingItem;
import com.threadloop.marketplace.repository.ClothingItemRepository;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class ClothingTools {

    private final ClothingItemRepository clothingItemRepository;
    private final MongoTemplate mongoTemplate;

    public ClothingTools(
            ClothingItemRepository clothingItemRepository,
            MongoTemplate mongoTemplate) {
        this.clothingItemRepository = clothingItemRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Tool(name = "search_clothes", description = "Search available ThreadLoop clothing listings using optional category, size, color, brand, condition, and maximum swap value filters. Only available listings are returned.")
    public List<String> searchClothes(
            @ToolParam(description = "Category, such as Tops & Shirts or jeans", required = false) String category,

            @ToolParam(description = "Size, such as S, M, L, or XL", required = false) String size,

            @ToolParam(description = "Color, such as black, white, or blue", required = false) String color,

            @ToolParam(description = "Brand name, such as Nike or Zara", required = false) String brand,

            @ToolParam(description = "Condition, such as like_new or gently_used", required = false) String condition,

            @ToolParam(description = "Maximum estimated swap value", required = false) Double maxSwapValue) {

        Query query = new Query();

        // Only return clothing that is available for swapping.
        query.addCriteria(Criteria.where("status").is("available"));

        // Add a filter only when the user provides a value.
        addTextFilter(query, "category", category);
        addTextFilter(query, "size", size);
        addTextFilter(query, "color", color);
        addTextFilter(query, "brand", brand);
        addTextFilter(query, "condition", condition);

        if (maxSwapValue != null) {
            query.addCriteria(
                    Criteria.where("estimatedSwapValue").lte(maxSwapValue));
        }

        List<ClothingItem> clothes = mongoTemplate.find(query, ClothingItem.class);

        return clothes.stream()
                .map(this::formatClothingItem)
                .toList();
    }

    private void addTextFilter(Query query, String field, String value) {
        if (value != null && !value.isBlank()) {
            String escapedValue = Pattern.quote(value.trim());

            query.addCriteria(
                    Criteria.where(field)
                            .regex(escapedValue, "i"));
        }
    }

    @Tool(name = "get_clothing_details", description = "Retrieve the details of a specific ThreadLoop clothing listing using its ID")
    public String getClothingDetails(
            @ToolParam(description = "The exact MongoDB ID of the clothing listing") String clothingId) {

        Optional<ClothingItem> optionalItem = clothingItemRepository.findById(clothingId);

        if (optionalItem.isEmpty()) {
            return "No clothing listing found with ID: " + clothingId;
        }

        ClothingItem item = optionalItem.get();

        return formatClothingItem(item)
                + " | Description: " + item.getDescription()
                + " | Brand tier: " + item.getBrandTier()
                + " | Gender: " + item.getGender()
                + " | Material: " + item.getMaterial()
                + " | Subcategory: " + item.getSubcategory()
                + " | Original price: " + item.getOriginalPrice()
                + " | Estimated swap value: " + item.getEstimatedSwapValue()
                + " | Tags: " + item.getTags();
    }

    private String formatClothingItem(ClothingItem item) {
        return "ID: " + item.getId()
                + " | Title: " + item.getTitle()
                + " | Category: " + item.getCategory()
                + " | Size: " + item.getSize()
                + " | Brand: " + item.getBrand()
                + " | Color: " + item.getColor()
                + " | Condition: " + item.getCondition()
                + " | Status: " + item.getStatus()
                + " | Images: " + item.getImages();
    }
}