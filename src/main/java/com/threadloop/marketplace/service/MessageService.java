package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.ChatMessageDto;
import com.threadloop.marketplace.dto.SendMessageRequest;
import com.threadloop.marketplace.model.ChatMessage;
import com.threadloop.marketplace.model.SwapProposal;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.ChatMessageRepository;
import com.threadloop.marketplace.repository.SwapProposalRepository;
import com.threadloop.marketplace.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final SwapProposalRepository swapProposalRepository;
    private final UserRepository userRepository;

    private static final String[] AUTO_REPLIES = {
            "Sounds great! I'll prepare the garment carefully and steam it before we trade.",
            "Checked my schedule, meeting at the local swap hub works perfectly for me.",
            "I appreciate the transparent value trade! Excited to give this piece a second life."
    };

    public MessageService(ChatMessageRepository chatMessageRepository,
                          SwapProposalRepository swapProposalRepository,
                          UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.swapProposalRepository = swapProposalRepository;
        this.userRepository = userRepository;
    }

    public List<ChatMessageDto> getMessages(String swapId) {
        List<ChatMessage> list;
        if (swapId != null && !swapId.trim().isEmpty()) {
            list = chatMessageRepository.findBySwapIdOrderByTimestampAsc(swapId.trim());
        } else {
            list = chatMessageRepository.findAll();
        }
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageDto sendMessage(SendMessageRequest request) {
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Message text is required");
        }

        String swapId = request.getSwapId();
        SwapProposal swap = swapProposalRepository.findById(swapId)
                .orElseThrow(() -> new RuntimeException("Swap not found: " + swapId));

        String now = Instant.now().toString();
        String msgId = "msg_" + System.currentTimeMillis();

        ChatMessage msg = new ChatMessage(
                msgId,
                swapId,
                request.getSenderId(),
                request.getText().trim(),
                now,
                false,
                request.getActionData()
        );

        ChatMessage saved = chatMessageRepository.save(msg);

        // Async auto reply simulation from counterparty if applicable
        String senderId = request.getSenderId();
        String counterpartyId = swap.getRequester().getId().equals(senderId)
                ? swap.getReceiver().getId()
                : swap.getRequester().getId();

        Optional<User> counterparty = userRepository.findById(counterpartyId);
        if (counterparty.isPresent() && !"admin".equalsIgnoreCase(counterparty.get().getRole())) {
            CompletableFuture.delayedExecutor(1200, TimeUnit.MILLISECONDS).execute(() -> {
                try {
                    String randomReply = AUTO_REPLIES[new Random().nextInt(AUTO_REPLIES.length)];
                    ChatMessage reply = new ChatMessage(
                            "msg_" + System.currentTimeMillis(),
                            swapId,
                            counterpartyId,
                            randomReply,
                            Instant.now().toString(),
                            false,
                            null
                    );
                    chatMessageRepository.save(reply);
                } catch (Exception e) {
                    // Ignore background reply simulation error
                }
            });
        }

        return toDto(saved);
    }

    public ChatMessageDto toDto(ChatMessage m) {
        return new ChatMessageDto(
                m.getId(),
                m.getSwapId(),
                m.getSenderId(),
                m.getText(),
                m.getTimestamp(),
                m.getIsSystem(),
                m.getActionData()
        );
    }
}
