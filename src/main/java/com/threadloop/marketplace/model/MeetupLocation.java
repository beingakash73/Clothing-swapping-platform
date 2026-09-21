package com.threadloop.marketplace.model;

public class MeetupLocation {

    private String meetupName;
    private String meetupAddress;
    private String meetupType;
    private Double meetupLat;
    private Double meetupLng;

    public MeetupLocation() {}

    public MeetupLocation(String meetupName, String meetupAddress, String meetupType, Double meetupLat, Double meetupLng) {
        this.meetupName = meetupName;
        this.meetupAddress = meetupAddress;
        this.meetupType = meetupType;
        this.meetupLat = meetupLat;
        this.meetupLng = meetupLng;
    }

    public String getMeetupName() { return meetupName; }
    public void setMeetupName(String meetupName) { this.meetupName = meetupName; }

    public String getMeetupAddress() { return meetupAddress; }
    public void setMeetupAddress(String meetupAddress) { this.meetupAddress = meetupAddress; }

    public String getMeetupType() { return meetupType; }
    public void setMeetupType(String meetupType) { this.meetupType = meetupType; }

    public Double getMeetupLat() { return meetupLat; }
    public void setMeetupLat(Double meetupLat) { this.meetupLat = meetupLat; }

    public Double getMeetupLng() { return meetupLng; }
    public void setMeetupLng(Double meetupLng) { this.meetupLng = meetupLng; }
}
