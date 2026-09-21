package com.threadloop.marketplace.dto;

import java.util.ArrayList;
import java.util.List;

public class SwapProposalDto {
    private String id;
    private String requesterId;
    private String receiverId;
    private String requestedItemId;
    private List<String> offeredItemIds = new ArrayList<>();
    private String status;
    private String exchangeMethod;
    private MeetupLocationDto meetupLocation;
    private String trackingNumber;
    private String carrierName;
    private String initialMessage;
    private Double fairnessScore;
    private Double valueDifference;
    private String requesterConfirmedAt;
    private String receiverConfirmedAt;
    private String createdAt;
    private String updatedAt;

    // Optional included nested objects for rich UI
    private UserDto requester;
    private UserDto receiver;
    private ClothingItemDto requestedItem;
    private List<ChatMessageDto> messages = new ArrayList<>();

    public SwapProposalDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRequesterId() { return requesterId; }
    public void setRequesterId(String requesterId) { this.requesterId = requesterId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getRequestedItemId() { return requestedItemId; }
    public void setRequestedItemId(String requestedItemId) { this.requestedItemId = requestedItemId; }

    public List<String> getOfferedItemIds() { return offeredItemIds; }
    public void setOfferedItemIds(List<String> offeredItemIds) { this.offeredItemIds = offeredItemIds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getExchangeMethod() { return exchangeMethod; }
    public void setExchangeMethod(String exchangeMethod) { this.exchangeMethod = exchangeMethod; }

    public MeetupLocationDto getMeetupLocation() { return meetupLocation; }
    public void setMeetupLocation(MeetupLocationDto meetupLocation) { this.meetupLocation = meetupLocation; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }

    public String getInitialMessage() { return initialMessage; }
    public void setInitialMessage(String initialMessage) { this.initialMessage = initialMessage; }

    public Double getFairnessScore() { return fairnessScore; }
    public void setFairnessScore(Double fairnessScore) { this.fairnessScore = fairnessScore; }

    public Double getValueDifference() { return valueDifference; }
    public void setValueDifference(Double valueDifference) { this.valueDifference = valueDifference; }

    public String getRequesterConfirmedAt() { return requesterConfirmedAt; }
    public void setRequesterConfirmedAt(String requesterConfirmedAt) { this.requesterConfirmedAt = requesterConfirmedAt; }

    public String getReceiverConfirmedAt() { return receiverConfirmedAt; }
    public void setReceiverConfirmedAt(String receiverConfirmedAt) { this.receiverConfirmedAt = receiverConfirmedAt; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public UserDto getRequester() { return requester; }
    public void setRequester(UserDto requester) { this.requester = requester; }

    public UserDto getReceiver() { return receiver; }
    public void setReceiver(UserDto receiver) { this.receiver = receiver; }

    public ClothingItemDto getRequestedItem() { return requestedItem; }
    public void setRequestedItem(ClothingItemDto requestedItem) { this.requestedItem = requestedItem; }

    public List<ChatMessageDto> getMessages() { return messages; }
    public void setMessages(List<ChatMessageDto> messages) { this.messages = messages; }
}
