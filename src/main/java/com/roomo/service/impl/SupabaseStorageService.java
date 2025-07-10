package com.roomo.service.impl;

import com.roomo.config.SupabaseConfig;
import com.roomo.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseStorageService implements StorageService {

    private final SupabaseConfig supabaseConfig;
    private final RestTemplate restTemplate;

    @Override
    public String uploadAvatar(MultipartFile file, String userId) throws Exception {
        // Validate file
        validateFile(file);

        // Generate unique filename
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = String.format("%s_%s_%s.%s",
                userId,
                Instant.now().toEpochMilli(),
                UUID.randomUUID().toString().substring(0, 8),
                fileExtension
        );

        // Prepare upload URL
        String uploadUrl = String.format("%s/storage/v1/object/%s/%s",
                supabaseConfig.getUrl(),
                supabaseConfig.getStorage().getBucketName(),
                fileName
        );

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        headers.set("Authorization", "Bearer " + supabaseConfig.getAnonKey());
        headers.set("x-upsert", "true"); // Allow overwriting if exists

        // Create request entity
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

        try {
            // Upload file
            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                // Return public URL
                return String.format("%s/storage/v1/object/public/%s/%s",
                        supabaseConfig.getUrl(),
                        supabaseConfig.getStorage().getBucketName(),
                        fileName
                );
            } else {
                throw new Exception("Failed to upload file: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error uploading avatar for user {}: {}", userId, e.getMessage());
            throw new Exception("Failed to upload avatar", e);
        }
    }

    @Override
    public void deleteAvatar(String avatarUrl, String userId) throws Exception {
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            return;
        }

        try {
            // Extract file name from URL
            String fileName = extractFileNameFromUrl(avatarUrl);

            // Prepare delete URL
            String deleteUrl = String.format("%s/storage/v1/object/%s/%s",
                    supabaseConfig.getUrl(),
                    supabaseConfig.getStorage().getBucketName(),
                    fileName
            );

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseConfig.getAnonKey());

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // Delete file
            ResponseEntity<String> response = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Failed to delete avatar for user {}: {}", userId, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error deleting avatar for user {}: {}", userId, e.getMessage());
        }
    }

    @Override
    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        // Check file size
        if (file.getSize() > supabaseConfig.getStorage().getMaxFileSize()) {
            throw new IllegalArgumentException(String.format("File size exceeds maximum allowed size of %d MB",
                    supabaseConfig.getStorage().getMaxFileSize() / (1024 * 1024)));
        }

        // Check MIME type
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(supabaseConfig.getStorage().getAllowedMimeTypes()).contains(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Allowed types: " +
                    String.join(", ", supabaseConfig.getStorage().getAllowedMimeTypes()));
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "jpg"; // Default extension
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        // Extract file name from URL
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }
}
