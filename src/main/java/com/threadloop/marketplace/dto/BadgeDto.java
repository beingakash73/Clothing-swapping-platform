package com.threadloop.marketplace.dto;

public class BadgeDto {
    private String id;
    private String name;
    private String icon;
    private String description;
    private String unlockedAt;

    public BadgeDto() {}

    public BadgeDto(String id, String name, String icon, String description, String unlockedAt) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.unlockedAt = unlockedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(String unlockedAt) { this.unlockedAt = unlockedAt; }
}
