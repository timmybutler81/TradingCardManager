package com.butlert.tradingcardmanager.utils.validation;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validates the fields of a {@link Card} object to ensure data integrity before processing or persistence.
 *
 * <p>This class is responsible for field-level validation including:</p>
 * <ul>
 *   <li>Card number format and range</li>
 *   <li>String validation for names and games</li>
 *   <li>Rarity enum validation</li>
 *   <li>Purchase price formatting and limits</li>
 *   <li>Date boundaries and consistency between dates</li>
 *   <li>Boolean parsing for "foiled" status</li>
 * </ul>
 *
 * <p>Used as a shared component for input validation across services and utilities.</p>
 */
@Component
public class CardValidator {

    /**
     * Default constructor for {@link CardValidator}.
     * <p>
     * Since this class is managed by Spring as a {@code @Component},
     * explicit construction is typically handled by the Spring container.
     * This constructor exists for clarity and potential future extension.
     * </p>
     */
    public CardValidator() {

    }

    /**
     * Validates all fields of a {@link Card} object including ID, name, game, rarity, price, and dates.
     * Aggregates all validation errors and returns a failure if any exist.
     *
     * @param card the card object to validate
     * @return a {@link ValidatorResult} indicating success or listing error messages
     */
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
        LocalDate latest = LocalDate.now().plusYears(5);

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
     * Validates the card number to ensure it is a positive integer and within a 6-digit range.
     *
     * @param cardNumberString the card number as a string
     * @return a {@link ValidatorResult} indicating if the card number is valid
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
     * Validates that a name or game string is non-empty and contains only letters and spaces.
     *
     * @param string the input string to validate
     * @return a {@link ValidatorResult} indicating if the string is valid
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
     * Validates that the rarity string corresponds to a valid {@link CardRarity} enum constant.
     *
     * @param rarity the input rarity string
     * @return a {@link ValidatorResult} indicating if the rarity is valid
     */
    public ValidatorResult validateCardRarity(String rarity) {
        if (rarity == null || rarity.isBlank()) {
            return ValidatorResult.fail("Rarity cannot be empty.");
        }
        try {
            CardRarity.valueOf(rarity.trim().toUpperCase());
            return ValidatorResult.success();
        } catch (IllegalArgumentException e) {
            return ValidatorResult.fail("Invalid rarity type. Must be COMMON, UNCOMMON, HERO, or LEGENDARY.");
        }
    }

    /**
     * Validates that a date string can be parsed and falls within a reasonable range (1900 to 5 years in the future).
     *
     * @param dateString the date string to validate
     * @param dateTimeFormatter the formatter to use for parsing the date
     * @return a {@link ValidatorResult} indicating if the date is valid
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
     * Validates the purchase price format and range.
     * Ensures it is numeric, non-negative, no more than two decimal places, and below 1,000,000.
     *
     * @param purchasePriceString the price as a string
     * @return a {@link ValidatorResult} indicating if the price is valid
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
     * Validates whether the input string can be interpreted as a boolean ('true' or 'false').
     *
     * @param isFoiledString the string representation of a boolean value
     * @return a {@link ValidatorResult} indicating if the value is valid
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

