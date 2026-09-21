package com.threadloop.marketplace.dto;

public class DisputeDto {
    private String id;
    private String swapId;
    private String reporterId;
    private String reportedUserId;
    private String reason;
    private String description;
    private String status;
    private String resolutionNotes;
    private String createdAt;

    private UserDto reporter;
    private UserDto reportedUser;
    private SwapProposalDto swap;

    public DisputeDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public UserDto getReporter() { return reporter; }
    public void setReporter(UserDto reporter) { this.reporter = reporter; }

    public UserDto getReportedUser() { return reportedUser; }
    public void setReportedUser(UserDto reportedUser) { this.reportedUser = reportedUser; }

    public SwapProposalDto getSwap() { return swap; }
    public void setSwap(SwapProposalDto swap) { this.swap = swap; }
}
