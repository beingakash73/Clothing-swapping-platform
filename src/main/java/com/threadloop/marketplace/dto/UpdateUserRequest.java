package com.threadloop.marketplace.dto;

public class UpdateUserRequest {
    private String name;
    private String bio;
    private String avatar;
    private LocationDto location;

    public UpdateUserRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public LocationDto getLocation() { return location; }
    public void setLocation(LocationDto location) { this.location = location; }
}
