/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025,
 * CardValidator.java
 * This class is the main validator of data. Any class needing to validate data will need to pass through this
 * class to ensure all data is formatted correctly.
 */
package com.butlert.tradingcardmanager.utils.validation;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CardValidator {

    public ValidatorResult validateCard(Card card) {
        StringBuilder errors = new StringBuilder();

        ValidatorResult numberResult = validateCardNumber(String.valueOf(card.getCardNumber()));
        if (!numberResult.isValid()) errors.append(numberResult.getMessage()).append(" ");

        ValidatorResult nameResult = stringValidator(card.getCardName());
        if (!nameResult.isValid()) errors.append(nameResult.getMessage()).append(" ");

        ValidatorResult gameResult = stringValidator(card.getCardGame());
        if (!gameResult.isValid()) errors.append(gameResult.getMessage()).append(" ");

        ValidatorResult rarityResult = validateCardRarity(card.getRarity().name());
        if (!rarityResult.isValid()) errors.append(rarityResult.getMessage()).append(" ");

        ValidatorResult priceResult = validatePurchasePrice(card.getPurchasePrice().toPlainString());
        if (!priceResult.isValid()) errors.append(priceResult.getMessage()).append(" ");

        LocalDate pub = card.getDateSetPublished();
        LocalDate buy = card.getDatePurchased();

        LocalDate earliest = LocalDate.of(1900, 1, 1);
        LocalDate latest   = LocalDate.now().plusYears(5);

        if (pub == null) {
            errors.append("Set-published date is required. ");
        } else if (pub.isBefore(earliest) || pub.isAfter(latest)) {
            errors.append("Set-published date out of range (1900-").append(latest.getYear()).append("). ");
        }

        if (buy == null) {
            errors.append("Purchase date is required. ");
        } else if (buy.isBefore(earliest) || buy.isAfter(latest)) {
            errors.append("Purchase date out of range (1900-").append(latest.getYear()).append("). ");
        }

        if (pub != null && buy != null && buy.isBefore(pub)) {
            errors.append("Purchase date cannot be before set-published date. ");
        }

        if (errors.isEmpty()) {
            return ValidatorResult.success();
        }
        return ValidatorResult.fail(errors.toString().trim());
    }

    /**
     * method: validateCardNumber
     * parameters: cardNumberString
     * return: boolean
     * purpose: validates the card ID follows expected formatting
     */
    public ValidatorResult validateCardNumber(String cardNumberString) {
        if (cardNumberString == null || cardNumberString.isBlank()) {
            return ValidatorResult.fail("Card ID cannot be empty");
        }

        try {
            int id = Integer.parseInt(cardNumberString);
            if (id <= 0) {
                return ValidatorResult.fail("Card ID must be a positive integer.");
            }
            if (id >= 999999) {
                return ValidatorResult.fail("Card ID must only be six digits.");
            }
            return ValidatorResult.success();
        } catch (NumberFormatException e) {
            return ValidatorResult.fail("Card ID must be numeric");
        }
    }

    /**
     * method: stringValidator
     * parameters: string
     * return: boolean
     * purpose: validates the string for names are formatted in the expected way
     */
    public ValidatorResult stringValidator(String string) {
        if (string == null || string.trim().isEmpty()) {
            return ValidatorResult.fail("Name cannot be empty.");
        }

        if (!string.matches("[A-Za-z ]+")) {
            return ValidatorResult.fail("Name must contain only letters and spaces.");
        }
        return ValidatorResult.success();
    }

    /**
     * method: validateCardRarity
     * parameters: enumString
     * return: boolean
     * purpose: validates the enum is an accepted value
     */
    public ValidatorResult validateCardRarity(String rarity) {
        if (rarity == null || rarity.isBlank()) {
            return ValidatorResult.fail("Rarity cannot be empty.");
        }
        try {
            CardRarity.valueOf(rarity.trim().toUpperCase());
            return ValidatorResult.success();
        } catch (IllegalArgumentException e) {
            return ValidatorResult.fail("Invalid rarity type. Must be COMMON, RARE, HERO, or LEGENDARY.");
        }
    }

    /**
     * method: dateValidator
     * parameters: dateString, dateTimeFormatter
     * return: boolean
     * purpose: validates the date is properly formatted and within reasonable bounds
     */
    public ValidatorResult dateValidator(String dateString, DateTimeFormatter dateTimeFormatter) {
        try {
            LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);

            LocalDate earliest = LocalDate.of(1900, 1, 1);
            LocalDate latest = LocalDate.now().plusYears(5);

            if (date.isBefore(earliest) || date.isAfter(latest)) {
                return ValidatorResult.fail("Date is not within a valid range (1900 to " + latest.getYear() + ").");
            }
            return ValidatorResult.success();
        } catch (DateTimeParseException e) {
            return ValidatorResult.fail("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    /**
     * method: validatePurchasePrice
     * parameters: purchasePriceString
     * return: boolean
     * purpose: validates the purchase price is formatted correctly and within bounds
     */
    public ValidatorResult validatePurchasePrice(String purchasePriceString) {
        try {
            BigDecimal price = new BigDecimal(purchasePriceString);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                return ValidatorResult.fail("Price must be non-negative.");
            }
            if (price.scale() > 2) {
                return ValidatorResult.fail("Price can have at most two decimal places.");
            }
            if (price.compareTo(new BigDecimal("1000000")) > 0) {
                return ValidatorResult.fail("Price exceeds the maximum allowed value.");
            }
            return ValidatorResult.success();
        } catch (NumberFormatException e) {
            return ValidatorResult.fail("Invalid price format. Please enter a numeric value.");
        }
    }

    /**
     * method: validateIsFoiled
     * parameters: purchasePriceString
     * return: boolean
     * purpose: validates entered text is a valid boolean
     */
    public ValidatorResult validateIsFoiled(String isFoiledString) {
        if (isFoiledString == null) {
            return ValidatorResult.fail("Input cannot be null.");
        }

        String normalized = isFoiledString.trim().toLowerCase();
        if (normalized.equals("true") || normalized.equals("false")) {
            return ValidatorResult.success();
        }
        return ValidatorResult.fail("Invalid input. Please enter 'true' or 'false'.");
    }
}

