package com.threadloop.marketplace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threadloop.marketplace.dto.CreateItemRequest;
import com.threadloop.marketplace.dto.ProposeSwapRequest;
import com.threadloop.marketplace.dto.SendMessageRequest;
import com.threadloop.marketplace.dto.SwapStatusUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MarketplaceApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.database").value("connected"))
                .andExpect(jsonPath("$.totalUsers", greaterThanOrEqualTo(4)));
    }

    @Test
    void testGetUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].badges", notNullValue()));
    }

    @Test
    void testGetItemsWithFilter() throws Exception {
        mockMvc.perform(get("/api/items").param("category", "Jackets & Coats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].category").value("Jackets & Coats"));
    }

    @Test
    void testCreateItem() throws Exception {
        CreateItemRequest request = new CreateItemRequest();
        request.setTitle("Vintage Oversized Wool Trench");
        request.setBrand("Burberry Vintage");
        request.setCategory("Jackets & Coats");
        request.setCondition("like_new");
        request.setOriginalPrice(350.0);
        request.setEstimatedSwapValue(220.0);
        request.setOwnerId("user_maya");
        request.setSize("L");
        request.setGender("Unisex");
        request.setColor("Beige");
        request.setBrandTier("luxury");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title").value("Vintage Oversized Wool Trench"))
                .andExpect(jsonPath("$.ownerName").value("Maya Lin"))
                .andExpect(jsonPath("$.ecoSavedKgCo2", greaterThan(0.0)));
    }

    @Test
    void testGetSwapsAndProposeSwap() throws Exception {
        mockMvc.perform(get("/api/swaps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        ProposeSwapRequest req = new ProposeSwapRequest();
        req.setRequesterId("user_sofia");
        req.setRequestedItemId("item_1");
        req.setOfferedItemIds(List.of("item_7"));
        req.setExchangeMethod("local_meetup");
        req.setInitialMessage("Would love to swap for this fleece!");

        mockMvc.perform(post("/api/swaps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.status").value("pending"))
                .andExpect(jsonPath("$.fairnessScore", greaterThan(0.0)));
    }

    @Test
    void testSwapStatusTransition() throws Exception {
        SwapStatusUpdateRequest updateReq = new SwapStatusUpdateRequest();
        updateReq.setAction("accept");
        updateReq.setUserId("user_leo");

        mockMvc.perform(patch("/api/swaps/swap_01/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("negotiating"));
    }

    @Test
    void testMessagesApi() throws Exception {
        mockMvc.perform(get("/api/messages?swapId=swap_01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        SendMessageRequest sendReq = new SendMessageRequest();
        sendReq.setSwapId("swap_01");
        sendReq.setSenderId("user_maya");
        sendReq.setText("Looking forward to meeting at the Eco Hub!");

        mockMvc.perform(post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Looking forward to meeting at the Eco Hub!"));
    }

    @Test
    void testDisputesApi() throws Exception {
        mockMvc.perform(get("/api/disputes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].reason").value("item_condition_mismatch"));
    }

    @Test
    void testKPIsEndpoint() throws Exception {
        mockMvc.perform(get("/api/kpis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers", greaterThan(14800)))
                .andExpect(jsonPath("$.swapSuccessRate", greaterThan(90.0)));
    }

    @Test
    void testHubsEndpoint() throws Exception {
        mockMvc.perform(get("/api/hubs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    @Test
    void testClothesUploadEndpoints() throws Exception {
        // 1. Get all clothes uploads
        mockMvc.perform(get("/api/clothes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        // 2. Upload clothes via multipart mock
        mockMvc.perform(multipart("/api/clothes/list")
                        .param("title", "Eco Recycled Denim Jacket")
                        .param("category", "Jackets & Coats")
                        .param("size", "L")
                        .param("originalPrice", "120.0")
                        .param("ownerId", "user_maya"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Eco Recycled Denim Jacket"))
                .andExpect(jsonPath("$.ownerName").value("Maya Lin"))
                .andExpect(jsonPath("$.ecoSavedKgCo2", greaterThan(0.0)))
                .andExpect(jsonPath("$.images", not(empty())));

        // 3. Verify item also synchronized to main marketplace items endpoint
        mockMvc.perform(get("/api/items").param("search", "Eco Recycled Denim Jacket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
}

