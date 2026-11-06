
package com.threadloop.marketplace.ai.services;

import com.threadloop.marketplace.mcp.ClothingTools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiChatService {

    private static final Logger log = LoggerFactory.getLogger(AiChatService.class);

    private final ChatClient chatClient;
    private final ClothingTools clothingTools;

    public AiChatService(
            ChatClient.Builder chatClientBuilder,
            ClothingTools clothingTools) {

        this.clothingTools = clothingTools;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are ThreadLoop AI, an intelligent
                        assistant for a clothing-swapping marketplace.

                        Your responsibilities:
                        1. Help users discover available clothing.
                        2. Understand clothing preferences.
                        3. Search clothing using the available tools.
                        4. Provide accurate clothing information.

                        Rules:
                        - Use search_clothes when users ask to find clothes.
                        - Use get_clothing_details when users ask about
                          a specific listing and provide its ID.
                        - Never invent products, prices, or availability.
                        - Only provide information returned by the tools.
                        - If no matching clothes are found, say so clearly.
                        - Respond in simple, friendly language.
                        """)
                .defaultTools(clothingTools)
                .build();
    }

    public String chat(String message) {
        try {
            return chatClient.prompt()
                    .user(message)
                    .call()
                    .content();
        } catch (Exception e) {
            return fallbackSearch(message, e);
        }
    }

    private String fallbackSearch(String message, Exception ex) {
        log.warn("AI LLM service error or offline: {}. Performing graceful inventory fallback search.", ex.getMessage());

        List<String> results;
        try {
            results = clothingTools.searchClothes(null, null, null, null, null, null);
        } catch (Exception dbEx) {
            log.error("Failed to query clothing inventory in fallback: {}", dbEx.getMessage());
            return "ThreadLoop AI is currently resting. Please ensure the backend and MongoDB database connection are active. Details: " + ex.getMessage();
        }

        if (results == null || results.isEmpty()) {
            return "Hello! I am ThreadLoop AI. Currently the local Ollama AI model is not running on localhost:11434, and no clothing listings were found in the database. Run `ollama run qwen2.5:3b` to activate full conversational AI!";
        }

        String lower = message.toLowerCase();
        String[] keywords = lower.split("\\s+");
        List<String> matched = results.stream().filter(item -> {
            String itemLower = item.toLowerCase();
            for (String kw : keywords) {
                if (kw.length() > 2 && itemLower.contains(kw)) {
                    return true;
                }
            }
            return false;
        }).limit(5).toList();

        StringBuilder sb = new StringBuilder();
        sb.append("👋 Hi! I'm ThreadLoop AI. *(Note: Ollama local AI is currently offline, so I'm retrieving items directly from the marketplace inventory)*\n\n");

        if (!matched.isEmpty()) {
            sb.append("Here are available garments matching your search:\n\n");
            for (String itemStr : matched) {
                sb.append("• ").append(itemStr).append("\n\n");
            }
        } else {
            sb.append("Here are some available items currently in the closet:\n\n");
            for (int i = 0; i < Math.min(3, results.size()); i++) {
                sb.append("• ").append(results.get(i)).append("\n\n");
            }
        }
        sb.append("💡 *Tip: Run `ollama run qwen2.5:3b` on your machine to activate full natural language conversation!*");
        return sb.toString();
    }
}
