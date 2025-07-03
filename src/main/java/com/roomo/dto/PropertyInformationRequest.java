package com.roomo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInformationRequest {

    @NotBlank(message = "Accommodation type is required")
    private String accommodationType;

    @NotBlank(message = "Property type is required")
    private String propertyType;

    @NotNull(message = "Number of bedrooms is required")
    @Positive(message = "Number of bedrooms must be positive")
    private Short bedroomsNumber;

    @NotNull(message = "Number of bathrooms is required")
    @Positive(message = "Number of bathrooms must be positive")
    private Short bathroomsNumber;

    @NotBlank(message = "Parking information is required")
    private String parking;

    @NotBlank(message = "Accessibility features are required")
    private String accessibilityFeatures;

    @NotNull(message = "Number of people living is required")
    @Positive(message = "Number of people living must be positive")
    private Short numberOfPeopleLiving;

    @NotBlank(message = "Room name is required")
    private String roomName;

    @NotBlank(message = "Room type is required")
    private String roomType;

    @NotBlank(message = "Room furnishings are required")
    private String roomFurnishings;

    @NotBlank(message = "Bathroom information is required")
    private String bathroom;

    @NotBlank(message = "Bed size is required")
    private String bedSize;

    @NotBlank(message = "Room furnishings features are required")
    private String roomFurnishingsFeatures;

    @NotNull(message = "Weekly rent is required")
    @Positive(message = "Weekly rent must be positive")
    private Float weeklyRent;

    @NotBlank(message = "Bills included information is required")
    private String billsIncluded;

    @NotBlank(message = "Suburb is required")
    private String suburb;

    @NotNull(message = "Room available date is required")
    private LocalDate roomAvailableDate;
}
