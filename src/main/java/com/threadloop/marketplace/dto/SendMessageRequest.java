package com.threadloop.marketplace.dto;

import java.util.Map;

public class SendMessageRequest {
    private String swapId;
    private String senderId;
    private String text;
    private Map<String, Object> actionData;

    public SendMessageRequest() {}

    public String getSwapId() { return swapId; }
    public void setSwapId(String swapId) { this.swapId = swapId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Map<String, Object> getActionData() { return actionData; }
    public void setActionData(Map<String, Object> actionData) { this.actionData = actionData; }
}
