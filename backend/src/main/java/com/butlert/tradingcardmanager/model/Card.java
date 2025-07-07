package com.butlert.tradingcardmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    @Column(name = "card_number")
    private int cardNumber;

    @NotBlank
    @Column(name = "card_game")
    private String cardGame;

    @NotBlank
    @Column(name = "card_name")
    private String cardName;

    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "rarity")
    private CardRarity rarity;

    @PastOrPresent
    @Column(name = "date_purchased")
    private LocalDate datePurchased;

    @PastOrPresent
    @Column(name = "date_set_published")
    private LocalDate dateSetPublished;

    @Column(name = "purchase_Price", precision = 12, scale = 2)
    @Positive
    private BigDecimal purchasePrice;

    @Column(name = "foiled")
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
