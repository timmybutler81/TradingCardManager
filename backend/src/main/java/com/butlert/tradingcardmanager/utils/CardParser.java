/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardParser.java
 * This class is used to parse through the file attempting to be imported. It is separate to provide some abstraction
 * for easier changes in the future.
 */
package com.butlert.tradingcardmanager.utils;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import com.butlert.tradingcardmanager.utils.validation.ValidatorResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CardParser {
    private final DateParser dateParser;
    private final CardValidator cardValidator;

    public CardParser(DateParser dateParser, CardValidator validator) {
        this.dateParser = dateParser;
        this.cardValidator = validator;
    }

    /**
     * method: parseLine
     * parameters: line
     * return: Optional Card
     * purpose: parses and separates the lines into pieces to be validated and processed
     */
    public Optional<Card> parseLine(String line) {
        String[] parts = Arrays.stream(line.split(" - "))
                .map(String::trim)
                .toArray(String[]::new);

        if (parts.length != 8) {
            throw new IllegalArgumentException("File must have all 8 fields. Found: " + parts.length);
        }

        ValidatorResult cardNumberResult = cardValidator.validateCardNumber(parts[0]);
        if (!cardNumberResult.isValid()) {
            throw new IllegalArgumentException("Card ID error: " + cardNumberResult.getMessage());
        }

        ValidatorResult gameResult = cardValidator.stringValidator(parts[1]);
        if (!gameResult.isValid()) {
            throw new IllegalArgumentException("Card Game error: " + gameResult.getMessage());
        }

        ValidatorResult nameResult = cardValidator.stringValidator(parts[2]);
        if (!nameResult.isValid()) {
            throw new IllegalArgumentException("Card Name error: " + nameResult.getMessage());
        }

        ValidatorResult rarityResult = cardValidator.validateCardRarity(parts[3]);
        if (!rarityResult.isValid()) {
            throw new IllegalArgumentException("Rarity error: " + rarityResult.getMessage());
        }

        ValidatorResult purchaseDateResult = cardValidator.dateValidator(parts[4], dateParser.getFormatter());
        if (!purchaseDateResult.isValid()) {
            throw new IllegalArgumentException("Date Purchased error: " + purchaseDateResult.getMessage());
        }

        ValidatorResult setDateResult = cardValidator.dateValidator(parts[5], dateParser.getFormatter());
        if (!setDateResult.isValid()) {
            throw new IllegalArgumentException("Date Set Published error: " + setDateResult.getMessage());
        }

        ValidatorResult priceResult = cardValidator.validatePurchasePrice(parts[6]);
        if (!priceResult.isValid()) {
            throw new IllegalArgumentException("Purchase Price error: " + priceResult.getMessage());
        }

        ValidatorResult foiledResult = cardValidator.validateIsFoiled(parts[7]);
        if (!foiledResult.isValid()) {
            throw new IllegalArgumentException("Is Foiled error: " + foiledResult.getMessage());
        }

        int cardNumber = Integer.parseInt(parts[0]);
        String cardGame = parts[1];
        String cardName = parts[2];
        CardRarity rarity = CardRarity.valueOf(parts[3].toUpperCase());
        LocalDate datePurchased = dateParser.parse(parts[4]);
        LocalDate dateSetPublished = dateParser.parse(parts[5]);
        BigDecimal purchasePrice = new BigDecimal(parts[6]);
        boolean isFoiled = Boolean.parseBoolean(parts[7]);

        return Optional.of(new Card(cardNumber, cardGame, cardName, rarity, datePurchased, dateSetPublished, purchasePrice, isFoiled));
    }
}

