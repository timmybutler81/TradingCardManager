/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * Card.java
 * This class is the main data storage for each card object. All cards will have this object created and used
 * to pass around the application.
 */
package main.java.db;

import main.java.utils.CardRarity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Card {
    private int cardNumber;
    private String cardGame;
    private String cardName;
    private CardRarity rarity;
    private LocalDate datePurchased;
    private LocalDate dateSetPublished;
    private BigDecimal purchasePrice;
    private boolean isFoiled;

    public Card() {
    }

    public Card(int cardNumber, String cardGame, String cardName, CardRarity rarity, LocalDate datePurchased, LocalDate dateSetPublished, BigDecimal purchasePrice, boolean isFoiled) {
        this.cardNumber = cardNumber;
        this.cardGame = cardGame;
        this.cardName = cardName;
        this.rarity = rarity;
        this.datePurchased = datePurchased;
        this.dateSetPublished = dateSetPublished;
        this.purchasePrice = purchasePrice;
        this.isFoiled = isFoiled;
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

    public CardRarity getRarity() {
        return rarity;
    }

    public void setRarity(CardRarity rarity) {
        this.rarity = rarity;
    }

    public LocalDate getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(LocalDate datePurchased) {
        this.datePurchased = datePurchased;
    }

    public LocalDate getDateSetPublished() {
        return dateSetPublished;
    }

    public void setDateSetPublished(LocalDate dateSetPublished) {
        this.dateSetPublished = dateSetPublished;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public boolean isFoiled() {
        return isFoiled;
    }

    public void setFoiled(boolean foiled) {
        isFoiled = foiled;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber=" + cardNumber +
                ", cardGame='" + cardGame + '\'' +
                ", cardName='" + cardName + '\'' +
                ", rarity=" + rarity +
                ", datePurchased=" + datePurchased +
                ", dateSetPublished=" + dateSetPublished +
                ", purchasePrice=" + purchasePrice +
                ", isFoiled=" + isFoiled +
                '}';
    }
}
