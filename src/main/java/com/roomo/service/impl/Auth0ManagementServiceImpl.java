package com.roomo.service.impl;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import com.auth0.net.Response;
import com.auth0.net.client.Auth0HttpClient;
import com.auth0.net.client.DefaultHttpClient;
import com.roomo.config.Auth0ManagementConfig;
import com.roomo.exception.RoleUpdateException;
import com.roomo.service.Auth0ManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class Auth0ManagementServiceImpl implements Auth0ManagementService {

    private final Auth0ManagementConfig auth0Config;

    // Cache for management API token
    private String cachedToken;
    private Instant tokenExpiresAt;

    /**
     * Get or refresh the Management API access token
     */
    private synchronized String getManagementApiToken() {
        // Check if we have a valid cached token
        if (cachedToken != null && tokenExpiresAt != null && Instant.now().isBefore(tokenExpiresAt)) {
            return cachedToken;
        }

        try {
            // Create Auth0 HTTP client
            Auth0HttpClient httpClient = DefaultHttpClient.newBuilder().withConnectTimeout(10).withReadTimeout(10).build();

            // Create AuthAPI client
            AuthAPI authAPI = AuthAPI.newBuilder(auth0Config.getDomain(), auth0Config.getClientId(), auth0Config.getClientSecret()).withHttpClient(httpClient).build();

            // Request token for Management API
            Request<TokenHolder> tokenRequest = authAPI.requestToken(auth0Config.getManagementAudience());
            Response<TokenHolder> response = tokenRequest.execute();
            TokenHolder tokenHolder = response.getBody();

            // Cache the token
            cachedToken = tokenHolder.getAccessToken();
            // Set expiration time
            tokenExpiresAt = Instant.now().plusSeconds(auth0Config.getTokenCacheDuration() - 300);

            log.info("Successfully obtained Management API token");
            return cachedToken;

        } catch (Auth0Exception e) {
            log.error("Failed to obtain Management API token", e);
            throw new RoleUpdateException("Failed to obtain Management API token", e);
        }
    }

    /**
     * Create Management API client with current token
     */
    private ManagementAPI getManagementAPI() {
        String token = getManagementApiToken();
        return ManagementAPI.newBuilder(auth0Config.getDomain(), token).build();
    }

    @Override
    public void updateUserRole(String userId, String role) {
        try {
            ManagementAPI mgmt = getManagementAPI();

            // Create app_metadata with role
            Map<String, Object> appMetadata = new HashMap<>();
            appMetadata.put("role", role);

            // Create user update object
            User userUpdate = new User();
            userUpdate.setAppMetadata(appMetadata);

            // Update user
            Request<User> request = mgmt.users().update(userId, userUpdate);
            Response<User> response = request.execute();

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("Successfully updated user {} role to {}", userId, role);
            } else {
                throw new RoleUpdateException("Failed to update user role. Status code: " + response.getStatusCode());
            }

        } catch (Auth0Exception e) {
            log.error("Error updating user role for user {}", userId, e);
            throw new RoleUpdateException("Failed to update user role", e);
        }
    }

    @Override
    public String getUserRole(String userId) {
        try {
            ManagementAPI mgmt = getManagementAPI();

            // Get user details
            Request<User> request = mgmt.users().get(userId, new UserFilter());
            Response<User> response = request.execute();
            User user = response.getBody();

            // Extract role from app_metadata
            if (user != null && user.getAppMetadata() != null) {
                Object roleObj = user.getAppMetadata().get("role");
                if (roleObj instanceof String) {
                    return (String) roleObj;
                }
            }

            return null;

        } catch (Auth0Exception e) {
            log.error("Error getting user role for user {}", userId, e);
            throw new RoleUpdateException("Failed to get user role", e);
        }
    }
}
