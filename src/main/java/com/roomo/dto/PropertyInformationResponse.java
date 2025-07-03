package com.roomo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInformationResponse {

    private Long id;
    private String accommodationType;
    private String propertyType;
    private Short bedroomsNumber;
    private Short bathroomsNumber;
    private String parking;
    private String accessibilityFeatures;
    private Short numberOfPeopleLiving;
    private String roomName;
    private String roomType;
    private String roomFurnishings;
    private String bathroom;
    private String bedSize;
    private String roomFurnishingsFeatures;
    private Float weeklyRent;
    private String billsIncluded;
    private String suburb;
    private LocalDate roomAvailableDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
