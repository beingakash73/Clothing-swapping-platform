package com.threadloop.marketplace.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "swap_proposals")
public class SwapProposal {

    @Id
    private String id;

    @DBRef
    private User requester;

    @DBRef
    private User receiver;

    @DBRef
    private ClothingItem requestedItem;

    private List<String> offeredItemIds = new ArrayList<>();
    private String status = "pending"; // 'pending' | 'negotiating' | 'accepted' | 'shipped' | 'completed' | 'rejected' | 'cancelled'
    private String exchangeMethod; // 'local_meetup' | 'courier_shipping'

    private MeetupLocation meetupLocation;

    private String trackingNumber;
    private String carrierName;
    private String initialMessage;
    private Double fairnessScore = 100.0;
    private Double valueDifference = 0.0;

    private String requesterConfirmedAt;
    private String receiverConfirmedAt;
    private String createdAt;
    private String updatedAt;

    public SwapProposal() {}

    public SwapProposal(String id, User requester, User receiver, ClothingItem requestedItem,
                        List<String> offeredItemIds, String status, String exchangeMethod,
                        MeetupLocation meetupLocation, String trackingNumber, String carrierName,
                        String initialMessage, Double fairnessScore, Double valueDifference,
                        String requesterConfirmedAt, String receiverConfirmedAt,
                        String createdAt, String updatedAt) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.requestedItem = requestedItem;
        this.offeredItemIds = offeredItemIds != null ? offeredItemIds : new ArrayList<>();
        this.status = status != null ? status : "pending";
        this.exchangeMethod = exchangeMethod;
        this.meetupLocation = meetupLocation;
        this.trackingNumber = trackingNumber;
        this.carrierName = carrierName;
        this.initialMessage = initialMessage;
        this.fairnessScore = fairnessScore != null ? fairnessScore : 100.0;
        this.valueDifference = valueDifference != null ? valueDifference : 0.0;
        this.requesterConfirmedAt = requesterConfirmedAt;
        this.receiverConfirmedAt = receiverConfirmedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public ClothingItem getRequestedItem() { return requestedItem; }
    public void setRequestedItem(ClothingItem requestedItem) { this.requestedItem = requestedItem; }

    public List<String> getOfferedItemIds() { return offeredItemIds; }
    public void setOfferedItemIds(List<String> offeredItemIds) { this.offeredItemIds = offeredItemIds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getExchangeMethod() { return exchangeMethod; }
    public void setExchangeMethod(String exchangeMethod) { this.exchangeMethod = exchangeMethod; }

    public MeetupLocation getMeetupLocation() { return meetupLocation; }
    public void setMeetupLocation(MeetupLocation meetupLocation) { this.meetupLocation = meetupLocation; }

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
}
