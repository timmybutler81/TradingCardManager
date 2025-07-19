package com.butlert.tradingcardmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * TradingCardManagerApplication.java
 *
 * This is the main entry point of the Trading Card Manager application.
 * It bootstraps the Spring Boot framework and launches the application context.
 */
@SpringBootApplication
public class TradingCardManagerApplication {

    /**
     * The main method used to launch the Spring Boot application.
     *
     * @param args command-line arguments passed at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(TradingCardManagerApplication.class, args);
    }
}
