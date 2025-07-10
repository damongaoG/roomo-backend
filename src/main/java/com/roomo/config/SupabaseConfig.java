package com.roomo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "supabase")
@Data
public class SupabaseConfig {

    private String url;
    private String anonKey;
    private Storage storage = new Storage();

    @Data
    public static class Storage {
        private String bucketName = "roomo-images";
        private long maxFileSize = 5 * 1024 * 1024; // 5MB default
        private String[] allowedMimeTypes = {"image/jpeg", "image/png", "image/webp"};
    }
}