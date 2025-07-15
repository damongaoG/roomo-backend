package com.roomo.controller;

import com.roomo.dto.UserRoleRequest;
import com.roomo.dto.UserRoleResponse;
import com.roomo.exception.RoleUpdateException;
import com.roomo.service.Auth0ManagementService;
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

    private final Auth0ManagementService auth0ManagementService;


    @PostMapping("/role")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserRoleResponse> updateUserRole(
            @Valid @RequestBody UserRoleRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        String role = request.getRole();

        log.info("Updating role for user {} to {}", userId, role);

        try {
            // Check if user already has a role
            String currentRole = auth0ManagementService.getUserRole(userId);
            if (currentRole != null && !currentRole.isEmpty()) {
                log.warn("User {} already has role: {}", userId, currentRole);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(UserRoleResponse.builder()
                                .success(false)
                                .message("User already has a role assigned: " + currentRole)
                                .userId(userId)
                                .role(currentRole)
                                .build());
            }

            // Update user role
            auth0ManagementService.updateUserRole(userId, role);

            return ResponseEntity.ok(UserRoleResponse.builder()
                    .success(true)
                    .message("User role updated successfully")
                    .userId(userId)
                    .role(role)
                    .build());

        } catch (RoleUpdateException e) {
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
            String role = auth0ManagementService.getUserRole(userId);

            if (role != null) {
                return ResponseEntity.ok(UserRoleResponse.builder()
                        .success(true)
                        .message("User role retrieved successfully")
                        .userId(userId)
                        .role(role)
                        .build());
            } else {
                return ResponseEntity.ok(UserRoleResponse.builder()
                        .success(true)
                        .message("User has no role assigned")
                        .userId(userId)
                        .role(null)
                        .build());
            }

        } catch (RoleUpdateException e) {
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
