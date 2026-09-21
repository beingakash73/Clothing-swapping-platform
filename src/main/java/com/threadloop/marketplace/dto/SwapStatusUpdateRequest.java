package com.threadloop.marketplace.dto;

public class SwapStatusUpdateRequest {
    private String action; // 'accept' | 'reject' | 'confirmAgreement' | 'ship' | 'complete'
    private String userId;
    private String trackingNumber;
    private String carrierName;

    public SwapStatusUpdateRequest() {}

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }
}
