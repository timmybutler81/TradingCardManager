package main.java.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CardValidator {
    public boolean validateCardNumber(String input) {
        try {
            int id = Integer.parseInt(input);
            if (id <= 0) {
                System.out.println("Card ID must be a positive integer.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Card ID format. Please enter a valid number.");
            return false;
        }
    }

    public boolean stringValidator(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return false;
        }

        if (!input.matches("[A-Za-z ]+")) {
            System.out.println("Name must contain only letters and spaces.");
            return false;
        }
        return true;
    }

    public boolean validateCardRarity(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Rarity cannot be empty");
            return false;
        }

        try {
            CardRarity.valueOf(input.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rarity. Valid options are: ");
            for (CardRarity rarity: CardRarity.values()) {
                System.out.print(rarity.name() + ", ");
            }
            return false;
        }
    }

    public boolean dateValidator(String input, DateTimeFormatter dateTimeFormatter) {
        try {
            LocalDate.parse(input, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return false;
        }
    }

    public boolean validatePurchasePrice(String input) {
        try {
            BigDecimal price = new BigDecimal(input);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Price must be non-negative.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format. Please enter a numeric value.");
            return false;
        }
    }

    public boolean validateIsFoiled(String input) {
        if (input == null) {
            System.out.println("Input cannot be null.");
            return false;
        }

        String normalized = input.trim().toLowerCase();
        if (normalized.equals("true") || normalized.equals("false")) {
            return true;
        }

        System.out.println("Invalid input. Please enter 'true' or 'false'.");
        return false;
    }

    public boolean validateCardFields(String[] parts, DateTimeFormatter dateTimeFormatter) {
        return validateCardNumber(parts[0])
                && stringValidator(parts[1])
                && stringValidator(parts[2])
                && validateCardRarity(parts[3]) // once implemented
                && dateValidator(parts[4], dateTimeFormatter)
                && dateValidator(parts[5], dateTimeFormatter)
                && validatePurchasePrice(parts[6])
                && validateIsFoiled(parts[7]);
    }
}
