package com.threadloop.marketplace.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private String id;
    private String name;
    private String email;
    private String role;
    private String avatar;
    private String bio;
    private LocationDto location;
    private Double rating;
    private Integer reviewCount;
    private Integer completedSwaps;
    private Integer ecoScore;
    private Integer waterSavedLiters;
    private Double co2SavedKg;
    private Double wasteDivertedKg;
    private List<BadgeDto> badges = new ArrayList<>();
    private List<String> closetItemIds = new ArrayList<>();
    private String joinedDate;

    public UserDto() {}

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

    public LocationDto getLocation() { return location; }
    public void setLocation(LocationDto location) { this.location = location; }

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

    public List<BadgeDto> getBadges() { return badges; }
    public void setBadges(List<BadgeDto> badges) { this.badges = badges; }

    public List<String> getClosetItemIds() { return closetItemIds; }
    public void setClosetItemIds(List<String> closetItemIds) { this.closetItemIds = closetItemIds; }

    public String getJoinedDate() { return joinedDate; }
    public void setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }
}
