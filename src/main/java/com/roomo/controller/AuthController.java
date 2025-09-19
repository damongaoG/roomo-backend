package com.roomo.controller;

import com.roomo.dto.UserInfoResponse;
import com.roomo.entity.User;
import com.roomo.exception.ResourceNotFoundException;
import com.roomo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint. No authentication required.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
      String userId = jwt.getSubject();

      User user = userService.getUserByAuth0UserId(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with Auth0 ID: " + userId));

      UserInfoResponse response = UserInfoResponse.builder()
        .id(user.getId())
        .auth0UserId(user.getAuth0UserId())
        .email(user.getEmail())
        .name(user.getName())
        .pictureUrl(user.getPictureUrl())
        .role(user.getRole() != null ? user.getRole().name() : null)
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .lastLoginAt(user.getLastLoginAt())
        .registrationStep(user.getRegistrationStep())
        .build();

      return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> status = new HashMap<>();
        status.put("authenticated", jwt != null);
        if (jwt != null) {
            status.put("userId", jwt.getSubject());
            status.put("expiresAt", jwt.getExpiresAt());
        }
        return ResponseEntity.ok(status);
    }
}
