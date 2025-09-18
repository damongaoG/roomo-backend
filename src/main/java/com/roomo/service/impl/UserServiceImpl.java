package com.roomo.service.impl;

import com.roomo.entity.User;
import com.roomo.exception.ResourceNotFoundException;
import com.roomo.repository.UserRepository;
import com.roomo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createOrUpdateUser(Jwt jwt) {
        String auth0UserId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String name = jwt.getClaimAsString("name");
        String pictureUrl = jwt.getClaimAsString("picture");

        Optional<User> existingUser = userRepository.findByAuth0UserId(auth0UserId);

        if (existingUser.isPresent()) {
            // Update existing user
            User user = existingUser.get();

            if (email != null && !email.equals(user.getEmail())) {
                user.setEmail(email);
                
            }
            if (name != null && !name.equals(user.getName())) {
                user.setName(name);
                
            }
            if (pictureUrl != null && !pictureUrl.equals(user.getPictureUrl())) {
                user.setPictureUrl(pictureUrl);
                
            }

            // Update last login
            user.setLastLoginAt(LocalDateTime.now());

          user = userRepository.save(user);
          log.info("Updated user information for: {}", auth0UserId);

          return user;
        } else {
            // Create new user
            User newUser = User.builder()
                    .auth0UserId(auth0UserId)
                    .email(email)
                    .name(name)
                    .pictureUrl(pictureUrl)
                    .lastLoginAt(LocalDateTime.now())
                    .build();

            newUser = userRepository.save(newUser);
            log.info("Created new user: {}", auth0UserId);

            return newUser;
        }
    }

    @Override
    public User updateUserRole(String userId, User.UserRole role) {
        User user = userRepository.findByAuth0UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Auth0 ID: " + userId));

        user.setRole(role);
        user = userRepository.save(user);

        log.info("Updated user {} role to {}", userId, role);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User.UserRole getUserRole(String userId) {
        Optional<User> user = userRepository.findByAuth0UserId(userId);
        return user.map(User::getRole).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserByAuth0UserId(String userId) {
        return userRepository.findByAuth0UserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean userExists(String userId) {
        return userRepository.existsByAuth0UserId(userId);
    }

    @Override
    public void updateLastLogin(String userId) {
        userRepository.updateLastLoginAt(userId, LocalDateTime.now());
        log.debug("Updated last login for user: {}", userId);
    }
}
