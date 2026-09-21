package com.threadloop.marketplace.dto;

import java.util.List;

public class ProposeSwapRequest {
    private String requesterId;
    private String requestedItemId;
    private List<String> offeredItemIds;
    private String exchangeMethod;
    private String initialMessage;
    private MeetupLocationDto meetupLocation;

    public ProposeSwapRequest() {}

    public String getRequesterId() { return requesterId; }
    public void setRequesterId(String requesterId) { this.requesterId = requesterId; }

    public String getRequestedItemId() { return requestedItemId; }
    public void setRequestedItemId(String requestedItemId) { this.requestedItemId = requestedItemId; }

    public List<String> getOfferedItemIds() { return offeredItemIds; }
    public void setOfferedItemIds(List<String> offeredItemIds) { this.offeredItemIds = offeredItemIds; }

    public String getExchangeMethod() { return exchangeMethod; }
    public void setExchangeMethod(String exchangeMethod) { this.exchangeMethod = exchangeMethod; }

    public String getInitialMessage() { return initialMessage; }
    public void setInitialMessage(String initialMessage) { this.initialMessage = initialMessage; }

    public MeetupLocationDto getMeetupLocation() { return meetupLocation; }
    public void setMeetupLocation(MeetupLocationDto meetupLocation) { this.meetupLocation = meetupLocation; }
}
