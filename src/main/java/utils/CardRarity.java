package main.java.utils;

public enum CardRarity {
    COMMON(-0.05, -0.02),     // Decreases quickly in market, slowly after purchase
    RARE(-0.02, -0.01),
    HERO(0.03, 0.015),
    LEGENDARY(0.05, 0.025);   // Appreciates faster in the market

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
}