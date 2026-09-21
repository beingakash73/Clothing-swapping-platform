package com.threadloop.marketplace.dto;

public class ResolveDisputeRequest {
    private String status;
    private String resolutionNotes;

    public ResolveDisputeRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }
}
