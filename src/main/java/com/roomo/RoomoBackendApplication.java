package com.roomo;

import com.roomo.config.Auth0ManagementConfig;
import com.roomo.config.SupabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main Spring Boot application class
 */
@SpringBootApplication
@EnableConfigurationProperties({SupabaseConfig.class, Auth0ManagementConfig.class})
public class RoomoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomoBackendApplication.class, args);
    }
}