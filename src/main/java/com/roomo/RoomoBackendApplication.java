package com.roomo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main Spring Boot application class
 */
@SpringBootApplication
@EnableConfigurationProperties
public class RoomoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomoBackendApplication.class, args);
    }
}