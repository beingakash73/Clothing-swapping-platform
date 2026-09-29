package com.threadloop.marketplace;

import com.threadloop.marketplace.dto.LoginRequest;
import com.threadloop.marketplace.dto.RegisterRequest;
import com.threadloop.marketplace.dto.UserDto;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.BadgeRepository;
import com.threadloop.marketplace.repository.UserRepository;
import com.threadloop.marketplace.service.AuthService;
import com.threadloop.marketplace.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTests {

    private UserRepository userRepository;
    private BadgeRepository badgeRepository;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        badgeRepository = Mockito.mock(BadgeRepository.class);
        userService = Mockito.mock(UserService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, badgeRepository, userService, passwordEncoder);
    }

    @Test
    void testRegisterHashesPasswordAndBindsSession() {
        RegisterRequest req = new RegisterRequest(
                "Jane Doe", "jane@example.com", "secret123",
                "Brooklyn", "NY", "11201", "Eco enthusiast", null
        );

        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDto mockDto = new UserDto();
        mockDto.setId("user_123");
        mockDto.setEmail("jane@example.com");
        mockDto.setName("Jane Doe");
        when(userService.toDto(any(User.class))).thenReturn(mockDto);

        MockHttpSession session = new MockHttpSession();
        UserDto result = authService.register(req, session);

        assertNotNull(result);
        assertEquals("jane@example.com", result.getEmail());
        assertNotNull(session.getAttribute("userId"));

        // Verify password was hashed (not plain text)
        verify(userRepository).save(argThat(user ->
                passwordEncoder.matches("secret123", user.getPassword()) &&
                !user.getPassword().equals("secret123")
        ));
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setId("user_1");
        user.setEmail("jane@example.com");
        user.setName("Jane");
        user.setPassword(passwordEncoder.encode("secret123"));

        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.of(user));

        UserDto mockDto = new UserDto();
        mockDto.setId("user_1");
        mockDto.setEmail("jane@example.com");
        when(userService.toDto(user)).thenReturn(mockDto);

        MockHttpSession session = new MockHttpSession();
        LoginRequest loginReq = new LoginRequest("jane@example.com", "secret123");
        UserDto loggedIn = authService.login(loginReq, session);

        assertNotNull(loggedIn);
        assertEquals("user_1", session.getAttribute("userId"));
    }

    @Test
    void testLoginFailureInvalidPassword() {
        User user = new User();
        user.setId("user_1");
        user.setEmail("jane@example.com");
        user.setPassword(passwordEncoder.encode("correct_password"));

        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.of(user));

        MockHttpSession session = new MockHttpSession();
        LoginRequest loginReq = new LoginRequest("jane@example.com", "wrong_password");

        assertThrows(IllegalArgumentException.class, () -> authService.login(loginReq, session));
        assertNull(session.getAttribute("userId"));
    }

    @Test
    void testLogoutClearsSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", "user_1");

        authService.logout(session);
        assertTrue(session.isInvalid());
    }
}
