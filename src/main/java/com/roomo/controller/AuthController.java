package com.roomo.controller;

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
public class AuthController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint. No authentication required.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new HashMap<>();

        // Extract user information from JWT claims
        userInfo.put("sub", jwt.getSubject());
        userInfo.put("email", jwt.getClaimAsString("email"));
        userInfo.put("name", jwt.getClaimAsString("name"));
        userInfo.put("picture", jwt.getClaimAsString("picture"));
        userInfo.put("scopes", jwt.getClaimAsString("scope"));

        userInfo.put("roles", jwt.getClaimAsStringList("https://roomo.com/roles"));

        return ResponseEntity.ok(userInfo);
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
