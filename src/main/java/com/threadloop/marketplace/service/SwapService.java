package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.*;
import com.threadloop.marketplace.model.*;
import com.threadloop.marketplace.repository.*;
import com.threadloop.marketplace.util.FairnessCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SwapService {

    private final SwapProposalRepository swapProposalRepository;
    private final ClothingItemRepository clothingItemRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PlatformKPIRepository platformKPIRepository;
    private final UserService userService;
    private final ItemService itemService;

    public SwapService(SwapProposalRepository swapProposalRepository,
                       ClothingItemRepository clothingItemRepository,
                       UserRepository userRepository,
                       ChatMessageRepository chatMessageRepository,
                       PlatformKPIRepository platformKPIRepository,
                       UserService userService,
                       ItemService itemService) {
        this.swapProposalRepository = swapProposalRepository;
        this.clothingItemRepository = clothingItemRepository;
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.platformKPIRepository = platformKPIRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    public List<SwapProposalDto> getSwaps(String userId, String status) {
        List<SwapProposal> list;
        if (userId != null && !userId.trim().isEmpty()) {
            if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
                list = swapProposalRepository.findByUserInvolvedAndStatus(userId.trim(), status.trim());
            } else {
                list = swapProposalRepository.findByUserInvolved(userId.trim());
            }
        } else if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("all")) {
            list = swapProposalRepository.findByStatusOrderByUpdatedAtDesc(status.trim());
        } else {
            list = swapProposalRepository.findAllByOrderByUpdatedAtDesc();
        }

        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<SwapProposalDto> getSwapById(String id) {
        return swapProposalRepository.findById(id).map(this::toDtoWithDetails);
    }

    @Transactional
    public SwapProposalDto proposeSwap(ProposeSwapRequest request) {
        ClothingItem requestedItem = clothingItemRepository.findById(request.getRequestedItemId())
                .orElseThrow(() -> new RuntimeException("Requested item not found: " + request.getRequestedItemId()));

        User requester = userRepository.findById(request.getRequesterId())
                .orElseThrow(() -> new RuntimeException("Requester user not found: " + request.getRequesterId()));

        User receiver = requestedItem.getOwner();

        List<String> offeredIds = request.getOfferedItemIds() != null ? request.getOfferedItemIds() : new ArrayList<>();
        List<ClothingItem> offeredItems = clothingItemRepository.findAllById(offeredIds);

        double offeredTotalValue = offeredItems.stream()
                .mapToDouble(ClothingItem::getEstimatedSwapValue)
                .sum();

        FairnessCalculator.FairnessResult fairness = FairnessCalculator.calculate(
                requestedItem.getEstimatedSwapValue(),
                offeredTotalValue
        );

        String swapId = "swap_" + System.currentTimeMillis();
        String now = Instant.now().toString();

        MeetupLocation meetupLoc = null;
        if (request.getMeetupLocation() != null) {
            meetupLoc = new MeetupLocation(
                    request.getMeetupLocation().getName(),
                    request.getMeetupLocation().getAddress(),
                    request.getMeetupLocation().getType(),
                    request.getMeetupLocation().getLat(),
                    request.getMeetupLocation().getLng()
            );
        }

        SwapProposal proposal = new SwapProposal(
                swapId,
                requester,
                receiver,
                requestedItem,
                offeredIds,
                "pending",
                request.getExchangeMethod() != null ? request.getExchangeMethod() : "local_meetup",
                meetupLoc,
                null,
                null,
                request.getInitialMessage() != null ? request.getInitialMessage() : "",
                fairness.getScore(),
                fairness.getDifference(),
                null,
                null,
                now,
                now
        );

        SwapProposal saved = swapProposalRepository.save(proposal);

        // Create initial chat message
        String msgText = (request.getInitialMessage() != null && !request.getInitialMessage().trim().isEmpty())
                ? request.getInitialMessage().trim()
                : "Hi! I'd love to swap my items for your " + requestedItem.getTitle() + ".";

        ChatMessage initialMsg = new ChatMessage(
                "msg_" + System.currentTimeMillis(),
                swapId,
                requester.getId(),
                msgText,
                now,
                false,
                null
        );
        chatMessageRepository.save(initialMsg);

        // Mark offered items as in_negotiation
        for (ClothingItem item : offeredItems) {
            item.setStatus("in_negotiation");
            clothingItemRepository.save(item);
        }

        return toDtoWithDetails(saved);
    }

    @Transactional
    public Optional<SwapProposalDto> updateSwapStatus(String id, SwapStatusUpdateRequest request) {
        return swapProposalRepository.findById(id).map(swap -> {
            String action = request.getAction();
            String now = Instant.now().toString();
            swap.setUpdatedAt(now);

            if ("accept".equalsIgnoreCase(action)) {
                swap.setStatus("negotiating");
                ChatMessage msg = new ChatMessage(
                        "msg_" + System.currentTimeMillis(),
                        id,
                        request.getUserId() != null ? request.getUserId() : swap.getReceiver().getId(),
                        "Accepted the swap request! Let's finalize the exchange details.",
                        now,
                        true,
                        null
                );
                chatMessageRepository.save(msg);
            } else if ("reject".equalsIgnoreCase(action)) {
                swap.setStatus("rejected");
                // Revert offered items to available
                if (swap.getOfferedItemIds() != null) {
                    List<ClothingItem> offered = clothingItemRepository.findAllById(swap.getOfferedItemIds());
                    for (ClothingItem item : offered) {
                        item.setStatus("available");
                        clothingItemRepository.save(item);
                    }
                }
            } else if ("confirmAgreement".equalsIgnoreCase(action)) {
                boolean isRequester = swap.getRequester().getId().equals(request.getUserId());
                if (isRequester) {
                    swap.setRequesterConfirmedAt(now);
                } else {
                    swap.setReceiverConfirmedAt(now);
                }
                swap.setStatus("accepted");
                ChatMessage msg = new ChatMessage(
                        "msg_" + System.currentTimeMillis(),
                        id,
                        request.getUserId() != null ? request.getUserId() : swap.getReceiver().getId(),
                        "🤝 Swap agreement confirmed! Items are locked in for exchange.",
                        now,
                        true,
                        null
                );
                chatMessageRepository.save(msg);
            } else if ("ship".equalsIgnoreCase(action)) {
                swap.setStatus("shipped");
                if (request.getTrackingNumber() != null) swap.setTrackingNumber(request.getTrackingNumber());
                if (request.getCarrierName() != null) swap.setCarrierName(request.getCarrierName());

                String carrier = request.getCarrierName() != null ? request.getCarrierName() : "Courier";
                String tracking = request.getTrackingNumber() != null ? request.getTrackingNumber() : "TL-EXCHANGE-NYC";

                ChatMessage msg = new ChatMessage(
                        "msg_" + System.currentTimeMillis(),
                        id,
                        request.getUserId() != null ? request.getUserId() : swap.getRequester().getId(),
                        "📦 Garments dispatched via " + carrier + ". Tracking: " + tracking,
                        now,
                        true,
                        null
                );
                chatMessageRepository.save(msg);
            } else if ("complete".equalsIgnoreCase(action)) {
                swap.setStatus("completed");

                // Mark both requested and offered items as swapped
                ClothingItem requested = swap.getRequestedItem();
                if (requested != null) {
                    requested.setStatus("swapped");
                    clothingItemRepository.save(requested);
                }
                if (swap.getOfferedItemIds() != null) {
                    List<ClothingItem> offered = clothingItemRepository.findAllById(swap.getOfferedItemIds());
                    for (ClothingItem item : offered) {
                        item.setStatus("swapped");
                        clothingItemRepository.save(item);
                    }
                }

                // Reward both users with eco metrics
                User requester = swap.getRequester();
                if (requester != null) {
                    requester.setCompletedSwaps(requester.getCompletedSwaps() + 1);
                    requester.setEcoScore(requester.getEcoScore() + 50);
                    requester.setWaterSavedLiters(requester.getWaterSavedLiters() + 2700);
                    requester.setCo2SavedKg(Math.round((requester.getCo2SavedKg() + 5.5) * 10.0) / 10.0);
                    requester.setWasteDivertedKg(Math.round((requester.getWasteDivertedKg() + 1.2) * 10.0) / 10.0);
                    userRepository.save(requester);
                }

                User receiver = swap.getReceiver();
                if (receiver != null) {
                    receiver.setCompletedSwaps(receiver.getCompletedSwaps() + 1);
                    receiver.setEcoScore(receiver.getEcoScore() + 50);
                    receiver.setWaterSavedLiters(receiver.getWaterSavedLiters() + 2700);
                    receiver.setCo2SavedKg(Math.round((receiver.getCo2SavedKg() + 5.5) * 10.0) / 10.0);
                    receiver.setWasteDivertedKg(Math.round((receiver.getWasteDivertedKg() + 1.2) * 10.0) / 10.0);
                    userRepository.save(receiver);
                }

                // Update PlatformKPIs
                platformKPIRepository.findById("global").ifPresent(kpi -> {
                    kpi.setCompletedSwaps(kpi.getCompletedSwaps() + 1);
                    kpi.setTotalKgWasteDiverted(Math.round((kpi.getTotalKgWasteDiverted() + 2.4) * 10.0) / 10.0);
                    kpi.setTotalLitersWaterSaved(kpi.getTotalLitersWaterSaved() + 5400.0);
                    kpi.setTotalKgCo2Avoided(Math.round((kpi.getTotalKgCo2Avoided() + 11.0) * 10.0) / 10.0);
                    platformKPIRepository.save(kpi);
                });

                ChatMessage msg = new ChatMessage(
                        "msg_" + System.currentTimeMillis(),
                        id,
                        request.getUserId() != null ? request.getUserId() : swap.getReceiver().getId(),
                        "🎉 Swap completed! Garments diverted from landfills and water conserved. Don't forget to leave a review!",
                        now,
                        true,
                        null
                );
                chatMessageRepository.save(msg);
            }

            SwapProposal updated = swapProposalRepository.save(swap);
            return toDtoWithDetails(updated);
        });
    }

    public SwapProposalDto toDto(SwapProposal s) {
        SwapProposalDto dto = new SwapProposalDto();
        dto.setId(s.getId());
        dto.setRequesterId(s.getRequester() != null ? s.getRequester().getId() : null);
        dto.setReceiverId(s.getReceiver() != null ? s.getReceiver().getId() : null);
        dto.setRequestedItemId(s.getRequestedItem() != null ? s.getRequestedItem().getId() : null);
        dto.setOfferedItemIds(s.getOfferedItemIds() != null ? s.getOfferedItemIds() : new ArrayList<>());
        dto.setStatus(s.getStatus());
        dto.setExchangeMethod(s.getExchangeMethod());

        if (s.getMeetupLocation() != null) {
            dto.setMeetupLocation(new MeetupLocationDto(
                    s.getMeetupLocation().getMeetupName(),
                    s.getMeetupLocation().getMeetupAddress(),
                    s.getMeetupLocation().getMeetupType(),
                    s.getMeetupLocation().getMeetupLat(),
                    s.getMeetupLocation().getMeetupLng()
            ));
        }

        dto.setTrackingNumber(s.getTrackingNumber());
        dto.setCarrierName(s.getCarrierName());
        dto.setInitialMessage(s.getInitialMessage());
        dto.setFairnessScore(s.getFairnessScore());
        dto.setValueDifference(s.getValueDifference());
        dto.setRequesterConfirmedAt(s.getRequesterConfirmedAt());
        dto.setReceiverConfirmedAt(s.getReceiverConfirmedAt());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());

        if (s.getRequester() != null) dto.setRequester(userService.toDto(s.getRequester()));
        if (s.getReceiver() != null) dto.setReceiver(userService.toDto(s.getReceiver()));
        if (s.getRequestedItem() != null) dto.setRequestedItem(itemService.toDto(s.getRequestedItem()));

        return dto;
    }

    public SwapProposalDto toDtoWithDetails(SwapProposal s) {
        SwapProposalDto dto = toDto(s);
        List<ChatMessage> msgs = chatMessageRepository.findBySwapIdOrderByTimestampAsc(s.getId());
        dto.setMessages(msgs.stream().map(m -> new ChatMessageDto(
                m.getId(),
                m.getSwapId(),
                m.getSenderId(),
                m.getText(),
                m.getTimestamp(),
                m.getIsSystem(),
                m.getActionData()
        )).collect(Collectors.toList()));
        return dto;
    }
}
