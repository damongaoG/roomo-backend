package com.roomo.controller;

import com.roomo.dto.PropertyInformationRequest;
import com.roomo.dto.PropertyInformationResponse;
import com.roomo.service.PropertyInformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingsController {

    private final PropertyInformationService propertyService;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_listings:write')")
    public ResponseEntity<PropertyInformationResponse> createListing(
            @Valid @RequestBody PropertyInformationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        PropertyInformationResponse response = propertyService.createProperty(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_listings:write')")
    public ResponseEntity<PropertyInformationResponse> updateListing(
            @PathVariable Long id,
            @Valid @RequestBody PropertyInformationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        PropertyInformationResponse response = propertyService.updateProperty(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_listings:read')")
    public ResponseEntity<PropertyInformationResponse> getListing(@PathVariable Long id) {
        PropertyInformationResponse response = propertyService.getProperty(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_listings:read')")
    public ResponseEntity<Map<String, Object>> getAllListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int pageSize) {
        Map<String, Object> response = new HashMap<>();
        response.put("page", page);
        response.put("pageSize", pageSize);
        return ResponseEntity.ok(response);
    }
}
