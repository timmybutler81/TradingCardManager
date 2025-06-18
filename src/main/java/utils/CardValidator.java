/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardValidator.java
 * This class is the main validator of data. Any class needing to validate data will need to pass through this
 * class to ensure all data is formatted correctly.
 */
package main.java.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CardValidator {

    /**
     * method: validateCardNumber
     * parameters: cardNumberString
     * return: boolean
     * purpose: validates the card ID follows expected formatting
     */
    public boolean validateCardNumber(String cardNumberString) {
        try {
            int id = Integer.parseInt(cardNumberString);
            if (id <= 0) {
                System.out.println("Card ID must be a positive integer.");
                return false;
            }
            if (id >= 999999) {
                System.out.println("Card ID must only be six digits.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Card ID format. Please enter a valid number.");
            return false;
        }
    }

    /**
     * method: stringValidator
     * parameters: string
     * return: boolean
     * purpose: validates the string for names are formatted in the expected way
     */
    public boolean stringValidator(String string) {
        if (string == null || string.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return false;
        }

        if (!string.matches("[A-Za-z ]+")) {
            System.out.println("Name must contain only letters and spaces.");
            return false;
        }
        return true;
    }

    /**
     * method: validateCardRarity
     * parameters: enumString
     * return: boolean
     * purpose: validates the enum is an accepted value
     */
    public boolean validateCardRarity(String enumString) {
        if (enumString == null || enumString.trim().isEmpty()) {
            System.out.println("Rarity cannot be empty");
            return false;
        }
        try {
            CardRarity.valueOf(enumString.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rarity. Valid options are: ");
            for (CardRarity rarity : CardRarity.values()) {
                System.out.print(rarity + ", ");
            }
            return false;
        }
    }

    /**
     * method: dateValidator
     * parameters: dateString, dateTimeFormatter
     * return: boolean
     * purpose: validates the date is properly formatted and within reasonable bounds
     */
    public boolean dateValidator(String dateString, DateTimeFormatter dateTimeFormatter) {
        try {
            LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);

            LocalDate earliest = LocalDate.of(1900, 1, 1);
            LocalDate latest = LocalDate.now().plusYears(5);

            if (date.isBefore(earliest) || date.isAfter(latest)) {
                System.out.println("Date is not within a valid range (1900 to " + latest.getYear() + ").");
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return false;
        }
    }

    /**
     * method: validatePurchasePrice
     * parameters: purchasePriceString
     * return: boolean
     * purpose: validates the purchase price is formatted correctly and within bounds
     */
    public boolean validatePurchasePrice(String purchasePriceString) {
        try {
            BigDecimal price = new BigDecimal(purchasePriceString);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Price must be non-negative.");
                return false;
            }
            if (price.scale() > 2) {
                System.out.println("Price can have at most two decimal places.");
                return false;
            }
            if (price.compareTo(new BigDecimal("1000000")) > 0) {
                System.out.println("Price exceeds the maximum allowed value.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format. Please enter a numeric value.");
            return false;
        }
    }

    /**
     * method: validateIsFoiled
     * parameters: purchasePriceString
     * return: boolean
     * purpose: validates entered text is a valid boolean
     */
    public boolean validateIsFoiled(String isFoiledString) {
        if (isFoiledString == null) {
            System.out.println("Input cannot be null.");
            return false;
        }

        String normalized = isFoiledString.trim().toLowerCase();
        if (normalized.equals("true") || normalized.equals("false")) {
            return true;
        }

        System.out.println("Invalid input. Please enter 'true' or 'false'.");
        return false;
    }
}
