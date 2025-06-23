package com.butlert.tradingcardmanager.utils.validator;

import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardValidatorTest {

    private CardValidator validator;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        validator = new CardValidator();
    }

    @Test
    void validCardNumber_returnsTrue() {
        assertTrue(validator.validateCardNumber("123456"));
    }

    @Test
    void zeroCardNumber_returnsFalse() {
        assertFalse(validator.validateCardNumber("0"));
    }

    @Test
    void tooLargeCardNumber_returnsFalse() {
        assertFalse(validator.validateCardNumber("999999"));
    }

    @Test
    void invalidFormatCardNumber_returnsFalse() {
        assertFalse(validator.validateCardNumber("abc"));
    }

    @Test
    void validString_returnsTrue() {
        assertTrue(validator.stringValidator("Magic Card"));
    }

    @Test
    void emptyString_returnsFalse() {
        assertFalse(validator.stringValidator(""));
    }

    @Test
    void nullString_returnsFalse() {
        assertFalse(validator.stringValidator(null));
    }

    @Test
    void invalidCharactersString_returnsFalse() {
        assertFalse(validator.stringValidator("Card#1"));
    }

    @Test
    void validRarity_returnsTrue() {
        assertTrue(validator.validateCardRarity("common"));
    }

    @Test
    void invalidRarity_returnsFalse() {
        assertFalse(validator.validateCardRarity("epic"));
    }

    @Test
    void nullRarity_returnsFalse() {
        assertFalse(validator.validateCardRarity(null));
    }

    @Test
    void validDate_returnsTrue() {
        assertTrue(validator.dateValidator("2024-05-01", formatter));
    }

    @Test
    void tooOldDate_returnsFalse() {
        assertFalse(validator.dateValidator("1899-12-31", formatter));
    }

    @Test
    void futureTooFarDate_returnsFalse() {
        assertFalse(validator.dateValidator("2100-01-01", formatter));
    }

    @Test
    void invalidFormatDate_returnsFalse() {
        assertFalse(validator.dateValidator("05/01/2024", formatter));
    }

    @Test
    void validPrice_returnsTrue() {
        assertTrue(validator.validatePurchasePrice("19.99"));
    }

    @Test
    void negativePrice_returnsFalse() {
        assertFalse(validator.validatePurchasePrice("-5.00"));
    }

    @Test
    void tooManyDecimals_returnsFalse() {
        assertFalse(validator.validatePurchasePrice("5.123"));
    }

    @Test
    void tooLargePrice_returnsFalse() {
        assertFalse(validator.validatePurchasePrice("1000000.01"));
    }

    @Test
    void nonNumericPrice_returnsFalse() {
        assertFalse(validator.validatePurchasePrice("ten"));
    }

    @Test
    void validBooleanTrue_returnsTrue() {
        assertTrue(validator.validateIsFoiled("true"));
    }

    @Test
    void validBooleanFalse_returnsTrue() {
        assertTrue(validator.validateIsFoiled("false"));
    }

    @Test
    void invalidBoolean_returnsFalse() {
        assertFalse(validator.validateIsFoiled("yes"));
    }

    @Test
    void nullBoolean_returnsFalse() {
        assertFalse(validator.validateIsFoiled(null));
    }
}
