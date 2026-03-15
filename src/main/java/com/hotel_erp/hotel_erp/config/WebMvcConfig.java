package com.hotel_erp.hotel_erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")           // supports allowCredentials + wildcard
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")                  // allow all headers to prevent preflight mismatches
                .exposedHeaders("Authorization", "X-Auth-Token")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
