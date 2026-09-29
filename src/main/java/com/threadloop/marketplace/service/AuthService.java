package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.LoginRequest;
import com.threadloop.marketplace.dto.RegisterRequest;
import com.threadloop.marketplace.dto.UserDto;
import com.threadloop.marketplace.model.Badge;
import com.threadloop.marketplace.model.Location;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.BadgeRepository;
import com.threadloop.marketplace.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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

    public AuthService(UserRepository userRepository,
                       BadgeRepository badgeRepository,
                       UserService userService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.badgeRepository = badgeRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

        session.setAttribute("userId", savedUser.getId());
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

        session.setAttribute("userId", user.getId());
        return userService.toDto(user);
    }

    public Optional<UserDto> getCurrentUser(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return Optional.empty();
        }
        String userId = (String) userIdObj;
        return userRepository.findById(userId).map(userService::toDto);
    }

    public void logout(HttpSession session) {
        session.removeAttribute("userId");
        try {
            session.invalidate();
        } catch (IllegalStateException ignored) {
            // Already invalidated
        }
    }
}
