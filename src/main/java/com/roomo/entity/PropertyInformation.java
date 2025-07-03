package com.roomo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "property_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accommodation_type", nullable = false)
    private String accommodationType;

    @Column(name = "property_type", nullable = false)
    private String propertyType;

    @Column(name = "bedrooms_number", nullable = false)
    private Short bedroomsNumber;

    @Column(name = "bathrooms_number", nullable = false)
    private Short bathroomsNumber;

    @Column(nullable = false)
    private String parking;

    @Column(name = "accessibility_features", nullable = false)
    private String accessibilityFeatures;

    @Column(name = "number_of_people_living", nullable = false)
    private Short numberOfPeopleLiving;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_furnishings", nullable = false)
    private String roomFurnishings;

    @Column(nullable = false)
    private String bathroom;

    @Column(name = "bed_size", nullable = false)
    private String bedSize;

    @Column(name = "room_furnishings_features", nullable = false)
    private String roomFurnishingsFeatures;

    @Column(name = "weekly_rent", nullable = false)
    private Float weeklyRent;

    @Column(name = "bills_included", nullable = false)
    private String billsIncluded;

    @Column(nullable = false)
    private String suburb;

    @Column(name = "room_available_date", nullable = false)
    private LocalDate roomAvailableDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
