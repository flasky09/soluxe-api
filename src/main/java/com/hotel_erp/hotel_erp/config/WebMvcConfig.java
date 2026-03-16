package com.hotel_erp.hotel_erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                    "https://soluxe-erp-frontend-production.up.railway.app",
                    "https://*.up.railway.app",
                    "http://localhost:[*]",
                    "http://127.0.0.1:[*]"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "X-Auth-Token", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
