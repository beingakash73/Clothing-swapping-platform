package com.threadloop.marketplace.controller;

import com.threadloop.marketplace.dto.AuthResponse;
import com.threadloop.marketplace.dto.LoginRequest;
import com.threadloop.marketplace.dto.RegisterRequest;
import com.threadloop.marketplace.dto.UserDto;
import com.threadloop.marketplace.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpSession session) {
        try {
            UserDto userDto = authService.register(request, session);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AuthResponse.success("Account created successfully", userDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(AuthResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        try {
            UserDto userDto = authService.login(request, session);
            return ResponseEntity.ok(AuthResponse.success("Login successful", userDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(HttpSession session) {
        Optional<UserDto> userDto = authService.getCurrentUser(session);
        return userDto
                .map(user -> ResponseEntity.ok(AuthResponse.success("Authenticated user retrieved", user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthResponse.error("No active session")));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(AuthResponse.success("Logged out successfully", null));
    }
}
