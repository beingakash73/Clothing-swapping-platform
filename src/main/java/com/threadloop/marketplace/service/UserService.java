package com.threadloop.marketplace.service;

import com.threadloop.marketplace.dto.BadgeDto;
import com.threadloop.marketplace.dto.LocationDto;
import com.threadloop.marketplace.dto.UpdateUserRequest;
import com.threadloop.marketplace.dto.UserDto;
import com.threadloop.marketplace.model.Badge;
import com.threadloop.marketplace.model.ClothingItem;
import com.threadloop.marketplace.model.Location;
import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.BadgeRepository;
import com.threadloop.marketplace.repository.ClothingItemRepository;
import com.threadloop.marketplace.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final ClothingItemRepository clothingItemRepository;

    public UserService(UserRepository userRepository, BadgeRepository badgeRepository, ClothingItemRepository clothingItemRepository) {
        this.userRepository = userRepository;
        this.badgeRepository = badgeRepository;
        this.clothingItemRepository = clothingItemRepository;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAllByOrderByCompletedSwapsDesc();
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(String id) {
        return userRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public Optional<UserDto> updateUser(String id, UpdateUserRequest request) {
        return userRepository.findById(id).map(user -> {
            if (request.getName() != null) user.setName(request.getName());
            if (request.getBio() != null) user.setBio(request.getBio());
            if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
            if (request.getLocation() != null) {
                Location loc = user.getLocation();
                if (loc == null) loc = new Location();
                if (request.getLocation().getCity() != null) loc.setCity(request.getLocation().getCity());
                if (request.getLocation().getState() != null) loc.setState(request.getLocation().getState());
                if (request.getLocation().getZip() != null) loc.setZip(request.getLocation().getZip());
                if (request.getLocation().getLat() != null) loc.setLat(request.getLocation().getLat());
                if (request.getLocation().getLng() != null) loc.setLng(request.getLocation().getLng());
                user.setLocation(loc);
            }
            User saved = userRepository.save(user);
            return toDto(saved);
        });
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setAvatar(user.getAvatar());
        dto.setBio(user.getBio());

        if (user.getLocation() != null) {
            dto.setLocation(new LocationDto(
                    user.getLocation().getCity(),
                    user.getLocation().getState(),
                    user.getLocation().getZip(),
                    user.getLocation().getLat(),
                    user.getLocation().getLng()
            ));
        }

        dto.setRating(user.getRating());
        dto.setReviewCount(user.getReviewCount());
        dto.setCompletedSwaps(user.getCompletedSwaps());
        dto.setEcoScore(user.getEcoScore());
        dto.setWaterSavedLiters(user.getWaterSavedLiters());
        dto.setCo2SavedKg(user.getCo2SavedKg());
        dto.setWasteDivertedKg(user.getWasteDivertedKg());
        dto.setJoinedDate(user.getJoinedDate());

        List<Badge> badges = badgeRepository.findByUserId(user.getId());
        dto.setBadges(badges.stream().map(b -> new BadgeDto(
                b.getId(),
                b.getName(),
                b.getIcon(),
                b.getDescription(),
                b.getUnlockedAt()
        )).collect(Collectors.toList()));

        List<ClothingItem> items = clothingItemRepository.findByOwnerId(user.getId());
        dto.setClosetItemIds(items.stream().map(ClothingItem::getId).collect(Collectors.toList()));

        return dto;
    }
}
