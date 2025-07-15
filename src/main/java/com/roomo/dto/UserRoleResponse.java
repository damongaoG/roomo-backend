package com.roomo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponse {

    /**
     * Indicates if the operation was successful
     */
    private boolean success;

    /**
     * Message describing the result
     */
    private String message;

    /**
     * The user ID (Auth0 subject)
     */
    private String userId;

    /**
     * The assigned role
     */
    private String role;
}
