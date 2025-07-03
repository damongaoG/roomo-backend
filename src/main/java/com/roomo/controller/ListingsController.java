package com.roomo.controller;

import com.roomo.dto.PropertyInformationRequest;
import com.roomo.dto.PropertyInformationResponse;
import com.roomo.service.PropertyInformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('LISTER')")
public class ListingsController {

    private final PropertyInformationService propertyService;

    @PostMapping
    public ResponseEntity<PropertyInformationResponse> createListing(
            @Valid @RequestBody PropertyInformationRequest request) {
        PropertyInformationResponse response = propertyService.createProperty(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyInformationResponse> updateListing(
            @PathVariable Long id,
            @Valid @RequestBody PropertyInformationRequest request) {
        PropertyInformationResponse response = propertyService.updateProperty(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyInformationResponse> getListing(@PathVariable Long id) {
        PropertyInformationResponse response = propertyService.getProperty(id);
        return ResponseEntity.ok(response);
    }
}