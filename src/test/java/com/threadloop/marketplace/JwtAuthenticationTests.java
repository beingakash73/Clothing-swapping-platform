package com.threadloop.marketplace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threadloop.marketplace.dto.LoginRequest;
import com.threadloop.marketplace.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testJwtTokenProviderGenerationAndValidation() {
        String userId = "user_maya";
        String email = "maya@threadloop.org";
        String name = "Maya Lin";

        String token = jwtTokenProvider.generateToken(userId, email, name);
        assertNotNull(token);
        assertFalse(token.isBlank());

        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(userId, jwtTokenProvider.getUserIdFromToken(token));

        // Invalid token should not validate
        assertFalse(jwtTokenProvider.validateToken("invalid.token.string"));
    }

    @Test
    void testLoginIssuesJwtTokenAndMeEndpointAcceptsBearerToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("maya.lin@threadloop.org", "password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.id").value("user_maya"))
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).get("token").asText();
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Call /api/auth/me with Bearer token
        mockMvc.perform(get("/api/auth/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.id").value("user_maya"))
                .andExpect(jsonPath("$.user.name").value("Maya Lin"));
    }

    @Test
    void testMeEndpointWithoutTokenOrSessionFails() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}
