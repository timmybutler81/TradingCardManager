package com.butlert.tradingcardmanager.utils;

import com.butlert.tradingcardmanager.model.CardDTO;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import com.butlert.tradingcardmanager.utils.validation.ValidatorResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

/**
 * Parses individual lines from an imported card data file into {@link CardDTO} objects.
 *
 * <p>This utility class provides a centralized location for parsing and validating
 * card data fields before transformation and persistence. By abstracting the logic,
 * future adjustments to format or validation are localized here.</p>
 *
 * <p>Expected file format per line:</p>
 * <pre>
 * cardNumber - cardGame - cardName - rarity - datePurchased - dateSetPublished - purchasePrice - isFoiled
 * </pre>
 *
 * @author Timothy Butler
 * @version 1.0
 * @since June 18, 2025
 */
@Component
public class CardParser {
    private final DateParser dateParser;
    private final CardValidator cardValidator;

    /**
     * Constructs a CardParser with the provided date parser and card validator.
     *
     * @param dateParser the utility to parse date strings
     * @param validator the validator used for each field
     */
    public CardParser(DateParser dateParser, CardValidator validator) {
        this.dateParser = dateParser;
        this.cardValidator = validator;
    }

    /**
     * Parses a single line of text into a {@link CardDTO} object after validating all fields.
     *
     * <p>Each field is trimmed, validated, and converted to its appropriate type. If any field fails
     * validation, an {@link IllegalArgumentException} is thrown with an appropriate message.</p>
     *
     * @param line a string representing a line from the import file
     * @return an Optional containing the constructed {@link CardDTO}, or empty if invalid
     * @throws IllegalArgumentException if the line format is incorrect or any field fails validation
     */
    public Optional<CardDTO> parseLine(String line) {
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

        return Optional.of(new CardDTO(
                null,
                cardNumber,
                cardGame,
                cardName,
                rarity.name(),
                datePurchased.toString(),
                dateSetPublished.toString(),
                purchasePrice.toPlainString(),
                isFoiled
        ));
    }
}

