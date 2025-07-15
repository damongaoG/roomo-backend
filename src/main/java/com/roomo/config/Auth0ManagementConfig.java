package com.roomo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth0.management")
@Data
public class Auth0ManagementConfig {

    /**
     * Auth0 domain
     */
    private String domain;

    /**
     * Client ID for the Management API application
     */
    private String clientId;

    /**
     * Client Secret for the Management API application
     */
    private String clientSecret;

    /**
     * Management API audience
     */
    private String audience;

    /**
     * Management API token cache duration in seconds
     */
    private int tokenCacheDuration = 3600;

    /**
     * Get the full Auth0 domain URL
     */
    public String getAuth0Url() {
        return "https://" + domain;
    }

    /**
     * Get the Management API audience URL
     */
    public String getManagementAudience() {
        if (audience != null && !audience.isEmpty()) {
            return audience;
        }
        return getAuth0Url() + "/api/v2/";
    }
}
