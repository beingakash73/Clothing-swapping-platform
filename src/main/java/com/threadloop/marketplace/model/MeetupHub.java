package com.threadloop.marketplace.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "meetup_hubs")
public class MeetupHub {

    @Id
    private String id;

    private String name;
    private String address;
    private String type; // 'safe_hub' | 'cafe' | 'community_center' | 'transit_hub'
    private Double lat;
    private Double lng;

    public MeetupHub() {}

    public MeetupHub(String id, String name, String address, String type, Double lat, Double lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }
}
