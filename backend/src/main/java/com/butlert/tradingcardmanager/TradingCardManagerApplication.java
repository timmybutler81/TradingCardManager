package com.butlert.tradingcardmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication   // <— turns on component scan, auto-config, etc.
public class TradingCardManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradingCardManagerApplication.class, args);
    }
}
