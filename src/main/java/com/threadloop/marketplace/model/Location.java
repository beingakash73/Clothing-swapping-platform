package com.threadloop.marketplace.model;

public class Location {

    private String city;
    private String state;
    private String zip;
    private Double lat;
    private Double lng;

    public Location() {}

    public Location(String city, String state, String zip, Double lat, Double lng) {
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }
}
