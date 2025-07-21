package com.butlert.tradingcardmanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for transferring card data between layers of the application.
 * <p>
 * Used primarily for client-server communication to encapsulate card-related fields
 * as simple strings and primitives to simplify validation and serialization.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 13, 2025</p>
 */
public class CardDTO {
    private Long id;
    private int cardNumber;
    private String cardGame;
    private String cardName;
    private String rarity;
    private String datePurchased;
    private String dateSetPublished;
    private String purchasePrice;
    private boolean foiled;

    public CardDTO() {

    }

    /**
     * Constructs a fully initialized CardDTO.
     *
     * @param id                the unique database ID of the card
     * @param cardNumber        the card number within the collection
     * @param cardGame          the name of the card game (e.g., Pokémon, Magic)
     * @param cardName          the name of the card
     * @param rarity            the rarity as a string (e.g., "COMMON", "RARE")
     * @param datePurchased     the purchase date as a string (yyyy-MM-dd)
     * @param dateSetPublished  the set publication date as a string (yyyy-MM-dd)
     * @param purchasePrice     the purchase price as a string (e.g., "3.50")
     * @param foiled            true if the card is foiled, false otherwise
     */
    public CardDTO(Long id, int cardNumber, String cardGame, String cardName, String rarity, String datePurchased, String dateSetPublished, String purchasePrice, boolean foiled) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardGame = cardGame;
        this.cardName = cardName;
        this.rarity = rarity;
        this.datePurchased = datePurchased;
        this.dateSetPublished = dateSetPublished;
        this.purchasePrice = purchasePrice;
        this.foiled = foiled;
    }

    public CardDTO(int cardNumber, String cardName, String cardGame, String rarity, String purchasePrice, String datePurchased, String dateSetPublished, boolean foiled) {
        this.cardNumber = cardNumber;
        this.cardGame = cardGame;
        this.cardName = cardName;
        this.rarity = rarity;
        this.datePurchased = datePurchased;
        this.dateSetPublished = dateSetPublished;
        this.purchasePrice = purchasePrice;
        this.foiled = foiled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardGame() {
        return cardGame;
    }

    public void setCardGame(String cardGame) {
        this.cardGame = cardGame;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getDateSetPublished() {
        return dateSetPublished;
    }

    public void setDateSetPublished(String dateSetPublished) {
        this.dateSetPublished = dateSetPublished;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public boolean isFoiled() {
        return foiled;
    }

    public void setFoiled(boolean foiled) {
        this.foiled = foiled;
    }

    @Override
    public String toString() {
        return "CardDTO{" +
                "id=" + id +
                ", cardNumber=" + cardNumber +
                ", cardGame='" + cardGame + '\'' +
                ", cardName='" + cardName + '\'' +
                ", rarity='" + rarity + '\'' +
                ", datePurchased=" + datePurchased +
                ", dateSetPublished=" + dateSetPublished +
                ", purchasePrice=" + purchasePrice +
                ", foiled=" + foiled +
                '}';
    }
}
