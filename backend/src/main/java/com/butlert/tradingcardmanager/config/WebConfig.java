package com.butlert.tradingcardmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for global CORS settings.
 * <p>
 * Allows requests from any origin to all API endpoints, which helps prevent
 * cross-origin errors during local development and testing.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 5, 2025</p>
 */
@Configuration
public class WebConfig {

    /**
     * Default constructor for {@code WebConfig}.
     * Required for Spring to instantiate the configuration class.
     */
    public WebConfig() {
    }

    /**
     * Defines the global CORS configuration for the application.
     *
     * @return a WebMvcConfigurer that allows all origins, methods, and headers on API endpoints
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
