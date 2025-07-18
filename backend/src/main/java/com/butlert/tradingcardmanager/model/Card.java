package com.butlert.tradingcardmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a trading card entity stored in the database.
 * <p>
 * This class is mapped to a database table and contains fields for metadata like
 * game, name, rarity, price, and publication dates. It is annotated for use with
 * JPA (Jakarta Persistence) and includes validation constraints.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 13, 2025</p>
 */
@Entity
public class Card {

    /**
     * Primary key for the card, auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique card number within the collection.
     */
    @Positive
    @Column(name = "card_number")
    private int cardNumber;

    /**
     * The name of the game this card belongs to (e.g., Magic, Pokémon).
     */
    @NotBlank
    @Column(name = "card_game")
    private String cardGame;

    /**
     * The name of the card.
     */
    @NotBlank
    @Column(name = "card_name")
    private String cardName;

    /**
     * The rarity classification of the card (e.g., COMMON, LEGENDARY).
     */
    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "rarity")
    private CardRarity rarity;

    /**
     * The date the card was purchased.
     * Must be in the past or present.
     */
    @PastOrPresent
    @Column(name = "date_purchased")
    private LocalDate datePurchased;

    /**
     * The official publication date of the card’s set.
     */
    @PastOrPresent
    @Column(name = "date_set_published")
    private LocalDate dateSetPublished;

    /**
     * The price the user paid for the card.
     */
    @Column(name = "purchase_Price", precision = 12, scale = 2)
    @Positive
    private BigDecimal purchasePrice;

    /**
     * Indicates whether the card is foiled (shiny).
     */
    @Column(name = "foiled")
    private boolean foiled;

    /**
     * Default constructor required by JPA.
     */
    public Card() {

    }

    /**
     * Constructs a Card without an ID, for insert operations.
     *
     * @param cardNumber         the unique card number
     * @param cardGame           the game name
     * @param cardName           the card's name
     * @param rarity             the card's rarity
     * @param datePurchased      the purchase date
     * @param dateSetPublished   the set's publication date
     * @param purchasePrice      the price paid
     * @param foiled             whether the card is foiled
     */
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

    /**
     * Constructs a Card with an ID, used for updating or querying existing entries.
     *
     * @param id                 the database ID
     * @param cardNumber         the unique card number
     * @param cardGame           the game name
     * @param cardName           the card's name
     * @param rarity             the card's rarity
     * @param datePurchased      the purchase date
     * @param dateSetPublished   the set's publication date
     * @param purchasePrice      the price paid
     * @param foiled             whether the card is foiled
     */
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
