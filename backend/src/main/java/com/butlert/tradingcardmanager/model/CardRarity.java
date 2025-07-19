package com.butlert.tradingcardmanager.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing the different rarities a card can have.
 * <p>
 * Each rarity constant includes two adjustment rates used in card value calculations:
 * one for market value and one for owner value. These rates influence how a card’s
 * value is adjusted over time based on its rarity tier.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> June 18, 2025</p>
 */
public enum CardRarity {
    COMMON(-0.05, -0.03),
    UNCOMMON(-0.03, -0.02),
    RARE(-0.02, -0.01),
    HERO(0.03, 0.015),
    LEGENDARY(0.05, 0.025);

    private final double marketRate;
    private final double ownerRate;

    /**
     * Constructs a CardRarity enum value with the specified market and owner adjustment rates.
     *
     * @param marketRate the adjustment rate for market value calculations
     * @param ownerRate  the adjustment rate for owner value calculations
     */
    CardRarity(double marketRate, double ownerRate) {
        this.marketRate = marketRate;
        this.ownerRate = ownerRate;
    }

    /**
     * Returns the market value adjustment rate for this rarity.
     *
     * @return the market rate adjustment as a double
     */
    public double getMarketRate() {
        return marketRate;
    }

    /**
     * Returns the owner value adjustment rate for this rarity.
     *
     * @return the owner rate adjustment as a double
     */
    public double getOwnerRate() {
        return ownerRate;
    }

    /**
     * Returns a formatted label for this rarity (e.g., "Legendary").
     * This is used for JSON serialization when sending to clients.
     *
     * @return the rarity name in title case
     */
    @JsonValue
    public String getLabel() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}