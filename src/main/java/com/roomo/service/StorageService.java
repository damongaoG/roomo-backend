package com.roomo.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * Upload an avatar image to Supabase Storage
     *
     * @param file   the image file to upload
     * @param userId the user ID to associate with the file
     * @return the public URL of the uploaded file
     * @throws Exception if upload fails
     */
    String uploadAvatar(MultipartFile file, String userId) throws Exception;

    /**
     * Delete an avatar from Supabase Storage
     *
     * @param avatarUrl the URL of the avatar to delete
     * @param userId    the user ID for authorization
     * @throws Exception if deletion fails
     */
    void deleteAvatar(String avatarUrl, String userId) throws Exception;

    /**
     * Validate if the file meets the requirements
     *
     * @param file the file to validate
     * @throws IllegalArgumentException if validation fails
     */
    void validateFile(MultipartFile file);
}
