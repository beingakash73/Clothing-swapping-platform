package com.threadloop.marketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String role = "user"; // 'user' | 'admin'

    private String avatar;

    private String bio;

    private Location location = new Location();

    private Double rating = 5.0;
    private Integer reviewCount = 0;
    private Integer completedSwaps = 0;
    private Integer ecoScore = 100;
    private Integer waterSavedLiters = 0;
    private Double co2SavedKg = 0.0;
    private Double wasteDivertedKg = 0.0;

    private String joinedDate;

    @DBRef(lazy = true)
    private List<Badge> badges = new ArrayList<>();

    @DBRef(lazy = true)
    @JsonIgnore
    private List<ClothingItem> clothingItems = new ArrayList<>();

    public User() {}

    public User(String id, String name, String email, String role, String avatar, String bio,
                Location location, Double rating, Integer reviewCount, Integer completedSwaps,
                Integer ecoScore, Integer waterSavedLiters, Double co2SavedKg, Double wasteDivertedKg,
                String joinedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role != null ? role : "user";
        this.avatar = avatar;
        this.bio = bio;
        this.location = location != null ? location : new Location();
        this.rating = rating != null ? rating : 5.0;
        this.reviewCount = reviewCount != null ? reviewCount : 0;
        this.completedSwaps = completedSwaps != null ? completedSwaps : 0;
        this.ecoScore = ecoScore != null ? ecoScore : 100;
        this.waterSavedLiters = waterSavedLiters != null ? waterSavedLiters : 0;
        this.co2SavedKg = co2SavedKg != null ? co2SavedKg : 0.0;
        this.wasteDivertedKg = wasteDivertedKg != null ? wasteDivertedKg : 0.0;
        this.joinedDate = joinedDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public Integer getCompletedSwaps() { return completedSwaps; }
    public void setCompletedSwaps(Integer completedSwaps) { this.completedSwaps = completedSwaps; }

    public Integer getEcoScore() { return ecoScore; }
    public void setEcoScore(Integer ecoScore) { this.ecoScore = ecoScore; }

    public Integer getWaterSavedLiters() { return waterSavedLiters; }
    public void setWaterSavedLiters(Integer waterSavedLiters) { this.waterSavedLiters = waterSavedLiters; }

    public Double getCo2SavedKg() { return co2SavedKg; }
    public void setCo2SavedKg(Double co2SavedKg) { this.co2SavedKg = co2SavedKg; }

    public Double getWasteDivertedKg() { return wasteDivertedKg; }
    public void setWasteDivertedKg(Double wasteDivertedKg) { this.wasteDivertedKg = wasteDivertedKg; }

    public String getJoinedDate() { return joinedDate; }
    public void setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }

    public List<Badge> getBadges() { return badges; }
    public void setBadges(List<Badge> badges) { this.badges = badges; }

    public List<ClothingItem> getClothingItems() { return clothingItems; }
    public void setClothingItems(List<ClothingItem> clothingItems) { this.clothingItems = clothingItems; }
}
