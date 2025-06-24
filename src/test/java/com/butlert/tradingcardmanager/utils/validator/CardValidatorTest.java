package com.butlert.tradingcardmanager.utils.validator;

import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import com.butlert.tradingcardmanager.utils.validation.ValidatorResult;
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
        ValidatorResult result = validator.validateCardNumber("123456");
        assertTrue(result.isValid());
    }

    @Test
    void zeroCardNumber_returnsFalse() {
        ValidatorResult result = validator.validateCardNumber("0");
        assertFalse(result.isValid());
    }

    @Test
    void tooLargeCardNumber_returnsFalse() {
        ValidatorResult result = validator.validateCardNumber("999999");
        assertFalse(result.isValid());
    }

    @Test
    void invalidFormatCardNumber_returnsFalse() {
        ValidatorResult result = validator.validateCardNumber("abc");
        assertFalse(result.isValid());
    }

    @Test
    void validString_returnsTrue() {
        ValidatorResult result = validator.stringValidator("Magic Card");
        assertTrue(result.isValid());
    }

    @Test
    void emptyString_returnsFalse() {
        ValidatorResult result = validator.stringValidator("");
        assertFalse(result.isValid());
    }

    @Test
    void nullString_returnsFalse() {
        ValidatorResult result = validator.stringValidator(null);
        assertFalse(result.isValid());
    }

    @Test
    void invalidCharactersString_returnsFalse() {
        ValidatorResult result = validator.stringValidator("Card#1");
        assertFalse(result.isValid());
    }

    @Test
    void validRarity_returnsTrue() {
        ValidatorResult result = validator.validateCardRarity("common");
        assertTrue(result.isValid());
    }

    @Test
    void invalidRarity_returnsFalse() {
        ValidatorResult result = validator.validateCardRarity("epic");
        assertFalse(result.isValid());
    }

    @Test
    void nullRarity_returnsFalse() {
        ValidatorResult result = validator.validateCardRarity(null);
        assertFalse(result.isValid());
    }

    @Test
    void validDate_returnsTrue() {
        ValidatorResult result = validator.dateValidator("2024-05-01", formatter);
        assertTrue(result.isValid());
    }

    @Test
    void tooOldDate_returnsFalse() {
        ValidatorResult result = validator.dateValidator("1899-12-31", formatter);
        assertFalse(result.isValid());
    }

    @Test
    void futureTooFarDate_returnsFalse() {
        ValidatorResult result = validator.dateValidator("2100-01-01", formatter);
        assertFalse(result.isValid());
    }

    @Test
    void invalidFormatDate_returnsFalse() {
        ValidatorResult result = validator.dateValidator("05/01/2024", formatter);
        assertFalse(result.isValid());
    }

    @Test
    void validPrice_returnsTrue() {
        ValidatorResult result = validator.validatePurchasePrice("19.99");
        assertTrue(result.isValid());
    }

    @Test
    void negativePrice_returnsFalse() {
        ValidatorResult result = validator.validatePurchasePrice("-5.00");
        assertFalse(result.isValid());
    }

    @Test
    void tooManyDecimals_returnsFalse() {
        ValidatorResult result = validator.validatePurchasePrice("5.123");
        assertFalse(result.isValid());
    }

    @Test
    void tooLargePrice_returnsFalse() {
        ValidatorResult result = validator.validatePurchasePrice("1000000.01");
        assertFalse(result.isValid());
    }

    @Test
    void nonNumericPrice_returnsFalse() {
        ValidatorResult result = validator.validatePurchasePrice("ten");
        assertFalse(result.isValid());
    }

    @Test
    void validBooleanTrue_returnsTrue() {
        ValidatorResult result = validator.validateIsFoiled("true");
        assertTrue(result.isValid());
    }

    @Test
    void validBooleanFalse_returnsTrue() {
        ValidatorResult result = validator.validateIsFoiled("false");
        assertTrue(result.isValid());
    }

    @Test
    void invalidBoolean_returnsFalse() {
        ValidatorResult result = validator.validateIsFoiled("yes");
        assertFalse(result.isValid());
    }

    @Test
    void nullBoolean_returnsFalse() {
        ValidatorResult result = validator.validateIsFoiled(null);
        assertFalse(result.isValid());
    }
}