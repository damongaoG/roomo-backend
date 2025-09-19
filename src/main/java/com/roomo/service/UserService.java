package com.roomo.service;

import com.roomo.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface UserService {

  /**
   * Create or update user based on JWT information
   * This method is called during authentication to ensure user exists locally
   *
   * @param jwt the JWT token containing user information
   * @return the created or updated user
   */
  User createOrUpdateUser(Jwt jwt);

  /**
   * Update user's role
   *
   * @param userId The Auth0 user ID
   * @param role   The role to assign (LISTER or LOOKER)
   * @return the updated user
   */
  User updateUserRole(String userId, User.UserRole role);

  /**
   * Get user's current role
   *
   * @param userId The Auth0 user ID
   * @return The current role, or null if user not found
   */
  User.UserRole getUserRole(String userId);

  /**
   * Get user by Auth0 user ID
   *
   * @param userId The Auth0 user ID
   * @return Optional user
   */
  Optional<User> getUserByAuth0UserId(String userId);

  /**
   * Get user by email address
   *
   * @param email The user's email
   * @return Optional user
   */
  Optional<User> getUserByEmail(String email);

  /**
   * Check if a user exists for a given Auth0 user ID
   *
   * @param userId The Auth0 user ID
   * @return true if exists, false otherwise
   */
  boolean userExists(String userId);

  /**
   * Update last login timestamp for a user
   *
   * @param userId The Auth0 user ID
   */
  void updateLastLogin(String userId);

  /**
   * Update user's registration step
   *
   * @param userId           Auth0 user ID
   * @param registrationStep new registration step value
   * @return updated user
   */
  User updateRegistrationStep(String userId, String registrationStep);
}
