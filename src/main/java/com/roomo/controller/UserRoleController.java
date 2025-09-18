package com.roomo.controller;

import com.roomo.dto.UserRoleRequest;
import com.roomo.dto.UserRoleResponse;
import com.roomo.entity.User;
import com.roomo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserRoleController {

    private final UserService userService;


    @PostMapping("/role")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserRoleResponse> updateUserRole(
            @Valid @RequestBody UserRoleRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        String role = request.getRole();

        log.info("Updating role for user {} to {}", userId, role);

        try {
            // Ensure user exists in local database
            userService.createOrUpdateUser(jwt);

            // Check if user already has a role
            User.UserRole currentRole = userService.getUserRole(userId);
            if (currentRole != null) {
                String currentRoleStr = currentRole.name().toLowerCase();
                log.warn("User {} already has role: {}", userId, currentRoleStr);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(UserRoleResponse.builder()
                                .success(false)
                                .message("User already has a role assigned: " + currentRoleStr)
                                .userId(userId)
                                .role(currentRoleStr)
                                .build());
            }

            // Convert string role to enum
            User.UserRole userRole = User.UserRole.valueOf(role.toUpperCase());

            // Update user role
            userService.updateUserRole(userId, userRole);

            return ResponseEntity.ok(UserRoleResponse.builder()
                    .success(true)
                    .message("User role updated successfully")
                    .userId(userId)
                    .role(role)
                    .build());

        } catch (IllegalArgumentException e) {
            log.error("Invalid role for user {}: {}", userId, role, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserRoleResponse.builder()
                            .success(false)
                            .message("Invalid role: " + role)
                            .userId(userId)
                            .role(null)
                            .build());
        } catch (Exception e) {
            log.error("Failed to update role for user {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserRoleResponse.builder()
                            .success(false)
                            .message("Failed to update user role: " + e.getMessage())
                            .userId(userId)
                            .role(null)
                            .build());
        }
    }

    /**
     * Get the current user's role
     */
    @GetMapping("/role")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserRoleResponse> getUserRole(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();

        try {
            // Ensure user exists in local database
            userService.createOrUpdateUser(jwt);

            User.UserRole userRole = userService.getUserRole(userId);

            if (userRole != null) {
                String roleStr = userRole.name().toLowerCase();
                return ResponseEntity.ok(UserRoleResponse.builder()
                        .success(true)
                        .message("User role retrieved successfully")
                        .userId(userId)
                        .role(roleStr)
                        .build());
            } else {
                return ResponseEntity.ok(UserRoleResponse.builder()
                        .success(true)
                        .message("User has no role assigned")
                        .userId(userId)
                        .role(null)
                        .build());
            }

        } catch (Exception e) {
            log.error("Failed to get role for user {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(UserRoleResponse.builder()
                            .success(false)
                            .message("Failed to retrieve user role: " + e.getMessage())
                            .userId(userId)
                            .role(null)
                            .build());
        }
    }
}
