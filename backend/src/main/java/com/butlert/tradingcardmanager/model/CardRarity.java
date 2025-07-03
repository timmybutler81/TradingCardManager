/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardRarity.java
 * This enum stores the rarities for cards. It enforces specific rarities to be used within the application and
 * provides some additional storage for calculations based on rarity.
 */
package com.butlert.tradingcardmanager.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardRarity {
    COMMON(-0.05, -0.02),
    RARE(-0.02, -0.01),
    HERO(0.03, 0.015),
    LEGENDARY(0.05, 0.025);

    private final double marketRate;
    private final double ownerRate;


    CardRarity(double marketRate, double ownerRate) {
        this.marketRate = marketRate;
        this.ownerRate = ownerRate;
    }

    public double getMarketRate() {
        return marketRate;
    }

    public double getOwnerRate() {
        return ownerRate;
    }

    @JsonValue
    public String getLabel() {
        return name().substring(0,1) + name().substring(1).toLowerCase();
    }
}