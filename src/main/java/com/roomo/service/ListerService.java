package com.roomo.service;

import com.roomo.dto.ListerRequest;
import com.roomo.dto.ListerResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ListerService {

    /**
     * Create a new lister profile
     *
     * @param request the lister information
     * @param userId  the user ID from JWT
     * @return the created lister response
     */
    ListerResponse createLister(ListerRequest request, String userId);

    /**
     * Update an existing lister profile
     *
     * @param id      the lister ID
     * @param request the updated lister information
     * @param userId  the user ID from JWT
     * @return the updated lister response
     */
    ListerResponse updateLister(Long id, ListerRequest request, String userId);

    /**
     * Get a lister by ID
     *
     * @param id the lister ID
     * @return the lister response
     */
    ListerResponse getLister(Long id);

    /**
     * Get a lister by user ID
     *
     * @param userId the user ID from JWT
     * @return the lister response
     */
    ListerResponse getListerByUserId(String userId);

    /**
     * Upload avatar for a lister
     *
     * @param id     the lister ID
     * @param file   the avatar file
     * @param userId the user ID from JWT
     * @return the updated lister response with avatar URL
     */
    ListerResponse uploadAvatar(Long id, MultipartFile file, String userId);

    /**
     * Delete avatar for a lister
     *
     * @param id     the lister ID
     * @param userId the user ID from JWT
     * @return the updated lister response without avatar URL
     */
    ListerResponse deleteAvatar(Long id, String userId);
}
