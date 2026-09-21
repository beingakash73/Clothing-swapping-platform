package com.threadloop.marketplace.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "disputes")
public class Dispute {

    @Id
    private String id;

    private String swapId;

    @DBRef
    private User reporter;

    @DBRef
    private User reportedUser;

    private String reason; // 'item_condition_mismatch' | 'non_delivery' | 'counterfeit' | 'unresponsive_user' | 'other'
    private String description;
    private String status = "open"; // 'open' | 'under_review' | 'resolved' | 'dismissed'
    private String resolutionNotes;
    private String createdAt;

    public Dispute() {}

    public Dispute(String id, String swapId, User reporter, User reportedUser, String reason,
                   String description, String status, String resolutionNotes, String createdAt) {
        this.id = id;
        this.swapId = swapId;
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.reason = reason;
        this.description = description;
        this.status = status != null ? status : "open";
        this.resolutionNotes = resolutionNotes;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSwapId() { return swapId; }
    public void setSwapId(String swapId) { this.swapId = swapId; }

    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }

    public User getReportedUser() { return reportedUser; }
    public void setReportedUser(User reportedUser) { this.reportedUser = reportedUser; }

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
}
