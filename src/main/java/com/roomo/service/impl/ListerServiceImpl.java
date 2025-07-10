package com.roomo.service.impl;

import com.roomo.dto.ListerRequest;
import com.roomo.dto.ListerResponse;
import com.roomo.entity.Lister;
import com.roomo.exception.ResourceNotFoundException;
import com.roomo.repository.ListerRepository;
import com.roomo.service.ListerService;
import com.roomo.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ListerServiceImpl implements ListerService {

    private final ListerRepository listerRepository;
    private final StorageService storageService;

    @Override
    public ListerResponse createLister(ListerRequest request, String userId) {
        // Check if lister already exists for this user
        if (listerRepository.existsByUserId(userId)) {
            throw new IllegalStateException("Lister profile already exists for this user");
        }

        // Create new lister
        Lister lister = Lister.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .introduction(request.getIntroduction())
                .userId(userId)
                .build();

        lister = listerRepository.save(lister);
        log.info("Created new lister profile for user: {}", userId);

        return mapToResponse(lister);
    }

    @Override
    public ListerResponse updateLister(Long id, ListerRequest request, String userId) {
        Lister lister = listerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lister not found with id: " + id));

        // Verify ownership
        if (!lister.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to update this lister profile");
        }

        // Update fields
        lister.setFirstName(request.getFirstName());
        lister.setLastName(request.getLastName());
        lister.setIntroduction(request.getIntroduction());

        lister = listerRepository.save(lister);
        log.info("Updated lister profile {} for user: {}", id, userId);

        return mapToResponse(lister);
    }

    @Override
    @Transactional(readOnly = true)
    public ListerResponse getLister(Long id) {
        Lister lister = listerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lister not found with id: " + id));

        return mapToResponse(lister);
    }

    @Override
    @Transactional(readOnly = true)
    public ListerResponse getListerByUserId(String userId) {
        Lister lister = listerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lister not found for user: " + userId));

        return mapToResponse(lister);
    }

    @Override
    public ListerResponse uploadAvatar(Long id, MultipartFile file, String userId) {
        Lister lister = listerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lister not found with id: " + id));

        // Verify ownership
        if (!lister.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to update this lister profile");
        }

        try {
            // Delete old avatar if exists
            if (lister.getAvatarUrl() != null) {
                storageService.deleteAvatar(lister.getAvatarUrl(), userId);
            }

            // Upload new avatar
            String avatarUrl = storageService.uploadAvatar(file, userId);
            lister.setAvatarUrl(avatarUrl);

            lister = listerRepository.save(lister);
            log.info("Uploaded avatar for lister {} by user: {}", id, userId);

            return mapToResponse(lister);
        } catch (Exception e) {
            log.error("Failed to upload avatar for lister {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    @Override
    public ListerResponse deleteAvatar(Long id, String userId) {
        Lister lister = listerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lister not found with id: " + id));

        // Verify ownership
        if (!lister.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to update this lister profile");
        }

        try {
            // Delete avatar from storage
            if (lister.getAvatarUrl() != null) {
                storageService.deleteAvatar(lister.getAvatarUrl(), userId);
                lister.setAvatarUrl(null);

                lister = listerRepository.save(lister);
                log.info("Deleted avatar for lister {} by user: {}", id, userId);
            }

            return mapToResponse(lister);
        } catch (Exception e) {
            log.error("Failed to delete avatar for lister {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete avatar", e);
        }
    }

    private ListerResponse mapToResponse(Lister lister) {
        return ListerResponse.builder()
                .id(lister.getId())
                .firstName(lister.getFirstName())
                .lastName(lister.getLastName())
                .introduction(lister.getIntroduction())
                .avatarUrl(lister.getAvatarUrl())
                .createdAt(lister.getCreatedAt())
                .updatedAt(lister.getUpdatedAt())
                .build();
    }
}
