package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.LoginRequest;
import com.threadloop.marketplace.dto.RegisterRequest;
import com.threadloop.marketplace.dto.UserDto;
import com.threadloop.marketplace.model.Badge;
import com.threadloop.marketplace.model.Location;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.BadgeRepository;
import com.threadloop.marketplace.repository.UserRepository;
import com.threadloop.marketplace.security.JwtTokenProvider;
import com.threadloop.marketplace.security.UserPrincipal;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository,
                       BadgeRepository badgeRepository,
                       UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.badgeRepository = badgeRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthService(UserRepository userRepository,
                       BadgeRepository badgeRepository,
                       UserService userService,
                       PasswordEncoder passwordEncoder) {
        this(userRepository, badgeRepository, userService, passwordEncoder, null);
    }

    @Transactional
    public UserDto register(RegisterRequest request, HttpSession session) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException("An account with this email address already exists.");
        }

        String userId = "user_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String avatar = (request.getAvatar() != null && !request.getAvatar().isBlank())
                ? request.getAvatar()
                : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80";

        String bio = (request.getBio() != null && !request.getBio().isBlank())
                ? request.getBio()
                : "Passionate about circular fashion & zero waste garment exchange.";

        Location location = new Location(
                request.getCity() != null && !request.getCity().isBlank() ? request.getCity() : "New York",
                request.getState() != null && !request.getState().isBlank() ? request.getState() : "NY",
                request.getZip() != null && !request.getZip().isBlank() ? request.getZip() : "10001",
                40.7128,
                -74.0060
        );

        User user = new User(
                userId,
                request.getName().trim(),
                email,
                "user",
                avatar,
                bio,
                location,
                5.0,
                0,
                0,
                100,
                0,
                0.0,
                0.0,
                LocalDate.now().toString()
        );
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        // Assign a welcome badge
        Badge welcomeBadge = new Badge(
                "badge_" + UUID.randomUUID().toString().substring(0, 8),
                savedUser,
                "Circular Pioneer",
                "Sparkles",
                "Joined the zero-waste garment exchange community.",
                LocalDate.now().toString()
        );
        badgeRepository.save(welcomeBadge);

        if (session != null) {
            session.setAttribute("userId", savedUser.getId());
        }
        return userService.toDto(savedUser);
    }

    public UserDto login(LoginRequest request, HttpSession session) {
        String query = request.getEmailOrUsername().trim();
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(query);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByNameIgnoreCase(query);
        }
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findById(query);
        }

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        User user = userOpt.get();
        boolean matches = false;
        if (user.getPassword() != null) {
            matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        } else {
            // Support initial seeded users without set passwords
            if ("password123".equals(request.getPassword())) {
                matches = true;
                user.setPassword(passwordEncoder.encode("password123"));
                userRepository.save(user);
            }
        }

        if (!matches) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        if (session != null) {
            session.setAttribute("userId", user.getId());
        }
        return userService.toDto(user);
    }

    public String generateTokenForUser(String userId) {
        if (jwtTokenProvider == null) {
            return "mock-jwt-token";
        }
        return userRepository.findById(userId)
                .map(u -> jwtTokenProvider.generateToken(u.getId(), u.getEmail(), u.getName()))
                .orElseGet(() -> jwtTokenProvider.generateToken(userId, "", ""));
    }

    public Optional<UserDto> getCurrentUser(HttpSession session) {
        // 1. Check SecurityContext (populated by JWT filter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return userRepository.findById(principal.getId()).map(userService::toDto);
        }

        // 2. Check Session fallback
        if (session != null) {
            Object userIdObj = session.getAttribute("userId");
            if (userIdObj != null) {
                String userId = (String) userIdObj;
                return userRepository.findById(userId).map(userService::toDto);
            }
        }

        return Optional.empty();
    }

    public void logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.removeAttribute("userId");
            try {
                session.invalidate();
            } catch (IllegalStateException ignored) {
                // Already invalidated
            }
        }
    }
}
