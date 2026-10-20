package com.threadloop.marketplace;

import com.threadloop.marketplace.dto.ChatMessageDto;
import com.threadloop.marketplace.dto.SendMessageRequest;
import com.threadloop.marketplace.model.ChatMessage;
import com.threadloop.marketplace.model.SwapProposal;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.ChatMessageRepository;
import com.threadloop.marketplace.repository.SwapProposalRepository;
import com.threadloop.marketplace.repository.UserRepository;
import com.threadloop.marketplace.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTests {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private SwapProposalRepository swapProposalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(
                chatMessageRepository,
                swapProposalRepository,
                userRepository,
                messagingTemplate
        );
    }

    @Test
    void testSendMessageBroadcastsToWebSocketTopics() {
        String swapId = "swap_123";
        String senderId = "user_abc";
        String text = "Hello via WebSocket STOMP!";

        SendMessageRequest request = new SendMessageRequest();
        request.setSwapId(swapId);
        request.setSenderId(senderId);
        request.setText(text);

        User requester = new User();
        requester.setId(senderId);
        requester.setName("Alice");

        User receiver = new User();
        receiver.setId("user_xyz");
        receiver.setName("Bob");

        SwapProposal swap = new SwapProposal();
        swap.setId(swapId);
        swap.setRequester(requester);
        swap.setReceiver(receiver);

        when(swapProposalRepository.findById(swapId)).thenReturn(Optional.of(swap));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatMessageDto result = messageService.sendMessage(request);

        assertNotNull(result);
        assertEquals(swapId, result.getSwapId());
        assertEquals(senderId, result.getSenderId());
        assertEquals(text, result.getText());

        // Verify WebSocket STOMP broadcast
        verify(messagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/messages/" + swapId), any(ChatMessageDto.class));
        verify(messagingTemplate, atLeastOnce()).convertAndSend(eq("/topic/messages"), any(ChatMessageDto.class));
    }

    @Test
    void testSendMessageThrowsWhenTextEmpty() {
        SendMessageRequest request = new SendMessageRequest();
        request.setSwapId("swap_1");
        request.setSenderId("user_1");
        request.setText("   ");

        assertThrows(IllegalArgumentException.class, () -> messageService.sendMessage(request));
    }
}
