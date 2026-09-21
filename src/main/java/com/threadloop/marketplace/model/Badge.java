package com.threadloop.marketplace.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "badges")
public class Badge {

    @Id
    private String id;

    @DBRef
    @JsonBackReference
    private User user;

    private String name;
    private String icon;
    private String description;
    private String unlockedAt;

    public Badge() {}

    public Badge(String id, User user, String name, String icon, String description, String unlockedAt) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.unlockedAt = unlockedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(String unlockedAt) { this.unlockedAt = unlockedAt; }
}
