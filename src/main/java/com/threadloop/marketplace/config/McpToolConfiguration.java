package com.threadloop.marketplace.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.threadloop.marketplace.mcp.ClothingTools;

@Configuration
public class McpToolConfiguration {

    @Bean
    public ToolCallbackProvider threadLoopTools(ClothingTools clothingTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(clothingTools)
                .build();
    }
}
