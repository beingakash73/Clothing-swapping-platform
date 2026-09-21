package com.threadloop.marketplace.repository;

import com.threadloop.marketplace.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySwapIdOrderByTimestampAsc(String swapId);
}
