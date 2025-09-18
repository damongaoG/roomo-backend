package com.roomo.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UserInfoResponse {

  Long id;
  String auth0UserId;
  String email;
  String name;
  String pictureUrl;
  String role;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
  LocalDateTime lastLoginAt;
}


