package com.roomo.service;

public interface Auth0ManagementService {

    /**
     * Update user's role in app_metadata
     *
     * @param userId The Auth0 user ID
     * @param role   The role to assign (lister or looker)
     * @throws com.roomo.exception.RoleUpdateException if the update fails
     */
    void updateUserRole(String userId, String role);

    /**
     * Get user's current role from app_metadata
     *
     * @param userId The Auth0 user ID
     * @return The current role, or null if not set
     */
    String getUserRole(String userId);
}
