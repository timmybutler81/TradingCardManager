/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 5, 2025
 * WebConfig.java
 * This class handles the cors configuration for the application. It allows request from any origin to prevent
 * restriction during testing.
 */

package com.butlert.tradingcardmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    /**
     * method: corsConfigurer
     * parameters: none
     * return: WebMvcConfigurer
     * purpose: defines global cors configuration
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
