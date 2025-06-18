/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardParser.java
 * This class is used to parse through the file attempting to be imported. It is separate to provide some abstraction
 * for easier changes in the future.
 */
package main.java.utils;

import main.java.db.Card;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

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
            throw new IllegalArgumentException("File must have all 8 fields");
        }

        if (!cardValidator.validateCardNumber(parts[0])
                || !cardValidator.stringValidator(parts[1])
                || !cardValidator.stringValidator(parts[2])
                || !cardValidator.validateCardRarity(parts[3])
                || !cardValidator.dateValidator(parts[4], dateParser.getFormatter())
                || !cardValidator.dateValidator(parts[5], dateParser.getFormatter())
                || !cardValidator.validatePurchasePrice(parts[6])
                || !cardValidator.validateIsFoiled(parts[7])) {
            throw new IllegalArgumentException("Validation failed for line: " + line);
        }

        int cardNumber = Integer.parseInt(parts[0].trim());
        String cardGame = parts[1].trim();
        String cardName = parts[2].trim();
        CardRarity rarity = CardRarity.valueOf(parts[3].trim().toUpperCase());
        LocalDate datePurchased = dateParser.parse(parts[4].trim());
        LocalDate datePublished = dateParser.parse(parts[5].trim());
        BigDecimal purchasePrice = new BigDecimal(parts[6].trim());
        boolean isFoiled = Boolean.parseBoolean(parts[7].trim());

        return Optional.of(new Card(cardNumber, cardGame, cardName, rarity, datePurchased, datePublished, purchasePrice, isFoiled));
    }
}
