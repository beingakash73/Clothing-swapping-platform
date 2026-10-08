package com.threadloop.marketplace.security;

import com.threadloop.marketplace.model.User;
import com.threadloop.marketplace.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrId) throws UsernameNotFoundException {
        String query = usernameOrEmailOrId.trim();
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(query);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByNameIgnoreCase(query);
        }
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findById(query);
        }

        User user = userOpt.orElseThrow(() ->
                new UsernameNotFoundException("User not found with identifier: " + usernameOrEmailOrId));

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserPrincipal.create(user);
    }
}
