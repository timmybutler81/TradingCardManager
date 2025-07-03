package com.butlert.tradingcardmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Positive
    private int cardNumber;

    @NotBlank
    private String cardGame;

    @NotBlank
    private String cardName;

    @Enumerated(EnumType.STRING)
    private CardRarity rarity;

    @PastOrPresent
    private LocalDate datePurchased;

    @PastOrPresent
    private LocalDate dateSetPublished;

    @Column(precision = 12, scale = 2)
    @Positive
    private BigDecimal purchasePrice;

    private boolean foiled;

    public Card() {

    }

    public Card(int cardNumber, String cardGame, String cardName,
                CardRarity rarity, LocalDate datePurchased,
                LocalDate dateSetPublished, BigDecimal purchasePrice,
                boolean foiled) {
        this.cardNumber = cardNumber;
        this.cardGame = cardGame;
        this.cardName = cardName;
        this.rarity = rarity;
        this.datePurchased = datePurchased;
        this.dateSetPublished = dateSetPublished;
        this.purchasePrice = purchasePrice;
        this.foiled = foiled;
    }

    public Card(Long id, int cardNumber, String cardGame, String cardName,
                CardRarity rarity, LocalDate datePurchased,
                LocalDate dateSetPublished, BigDecimal purchasePrice,
                boolean foiled) {
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
        return foiled;
    }

    public void setFoiled(boolean foiled) {
        this.foiled = foiled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return cardNumber == card.cardNumber &&
                Objects.equals(cardGame, card.cardGame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, cardGame);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber=" + cardNumber +
                ", cardGame='" + cardGame + '\'' +
                ", cardName='" + cardName + '\'' +
                ", rarity=" + rarity +
                ", datePurchased=" + datePurchased +
                ", dateSetPublished=" + dateSetPublished +
                ", purchasePrice=" + purchasePrice +
                ", foiled=" + foiled +
                '}';
    }
}
