package com.threadloop.marketplace.dto;

public class CreateDisputeRequest {
    private String swapId;
    private String reporterId;
    private String reportedUserId;
    private String reason;
    private String description;

    public CreateDisputeRequest() {}

    public String getSwapId() { return swapId; }
    public void setSwapId(String swapId) { this.swapId = swapId; }

    public String getReporterId() { return reporterId; }
    public void setReporterId(String reporterId) { this.reporterId = reporterId; }

    public String getReportedUserId() { return reportedUserId; }
    public void setReportedUserId(String reportedUserId) { this.reportedUserId = reportedUserId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
