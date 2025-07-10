package com.roomo.controller;

import com.roomo.dto.ListerRequest;
import com.roomo.dto.ListerResponse;
import com.roomo.service.ListerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/listers")
@RequiredArgsConstructor
public class ListerController {

    private final ListerService listerService;

    /**
     * Create a new lister profile
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_listers:write')")
    public ResponseEntity<ListerResponse> createLister(
            @Valid @RequestBody ListerRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        ListerResponse response = listerService.createLister(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing lister profile
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_listers:write')")
    public ResponseEntity<ListerResponse> updateLister(
            @PathVariable Long id,
            @Valid @RequestBody ListerRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        ListerResponse response = listerService.updateLister(id, request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a lister by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_listers:read')")
    public ResponseEntity<ListerResponse> getLister(@PathVariable Long id) {
        ListerResponse response = listerService.getLister(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get current user's lister profile
     */
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_listers:read')")
    public ResponseEntity<ListerResponse> getMyListerProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        ListerResponse response = listerService.getListerByUserId(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Upload avatar for a lister
     */
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_listers:write')")
    public ResponseEntity<ListerResponse> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        ListerResponse response = listerService.uploadAvatar(id, file, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete avatar for a lister
     */
    @DeleteMapping("/{id}/avatar")
    @PreAuthorize("hasAuthority('SCOPE_listers:write')")
    public ResponseEntity<ListerResponse> deleteAvatar(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        ListerResponse response = listerService.deleteAvatar(id, userId);
        return ResponseEntity.ok(response);
    }
}
