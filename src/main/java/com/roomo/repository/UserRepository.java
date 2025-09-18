package com.roomo.repository;

import com.roomo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Find a user by Auth0 user ID
   *
   * @param auth0UserId the Auth0 user ID from JWT
   * @return Optional containing the user if found
   */
  Optional<User> findByAuth0UserId(String auth0UserId);

  /**
   * Find a user by email address
   *
   * @param email the user's email address
   * @return Optional containing the user if found
   */
  Optional<User> findByEmail(String email);

  /**
   * Check if a user exists for a given Auth0 user ID
   *
   * @param auth0UserId the Auth0 user ID from JWT
   * @return true if exists, false otherwise
   */
  boolean existsByAuth0UserId(String auth0UserId);


  /**
   * Update the last login timestamp for a user
   *
   * @param auth0UserId the Auth0 user ID
   * @param lastLoginAt the last login timestamp
   */
  @Modifying
  @Query("UPDATE User u SET u.lastLoginAt = :lastLoginAt WHERE u.auth0UserId = :auth0UserId")
  void updateLastLoginAt(@Param("auth0UserId") String auth0UserId, @Param("lastLoginAt") LocalDateTime lastLoginAt);
}
