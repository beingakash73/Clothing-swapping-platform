package com.threadloop.marketplace.dto;

import java.util.Map;

public class ChatMessageDto {
    private String id;
    private String swapId;
    private String senderId;
    private String text;
    private String timestamp;
    private Boolean isSystem;
    private Map<String, Object> actionData;

    public ChatMessageDto() {}

    public ChatMessageDto(String id, String swapId, String senderId, String text, String timestamp, Boolean isSystem, Map<String, Object> actionData) {
        this.id = id;
        this.swapId = swapId;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
        this.isSystem = isSystem != null ? isSystem : false;
        this.actionData = actionData;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSwapId() { return swapId; }
    public void setSwapId(String swapId) { this.swapId = swapId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Boolean getIsSystem() { return isSystem; }
    public void setIsSystem(Boolean isSystem) { this.isSystem = isSystem; }

    public Map<String, Object> getActionData() { return actionData; }
    public void setActionData(Map<String, Object> actionData) { this.actionData = actionData; }
}
