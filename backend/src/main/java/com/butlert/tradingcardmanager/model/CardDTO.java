/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 13, 2025
 * CardDTO.java
 * This dto is used to pass card related data between different layers of the application. It is focused on
 * client-server communication.
 */
package com.butlert.tradingcardmanager.model;

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
