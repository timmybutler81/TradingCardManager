package util.validation;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import com.butlert.tradingcardmanager.utils.validation.ValidatorResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CardValidatorTest {

    private CardValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CardValidator();
    }

    @Test
    void testValidateCardNumber_valid() {
        ValidatorResult result = validator.validateCardNumber("12345");
        assertTrue(result.isValid());
    }

    @Test
    void testValidateCardNumber_invalidEmpty() {
        ValidatorResult result = validator.validateCardNumber("");
        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("empty"));
    }

    @Test
    void testValidateCardNumber_invalidNonNumeric() {
        ValidatorResult result = validator.validateCardNumber("abc");
        assertFalse(result.isValid());
    }

    @Test
    void testValidateCardNumber_invalidTooLarge() {
        ValidatorResult result = validator.validateCardNumber("9999999");
        assertFalse(result.isValid());
    }

    @Test
    void testStringValidator_valid() {
        ValidatorResult result = validator.stringValidator("Magic Card");
        assertTrue(result.isValid());
    }

    @Test
    void testStringValidator_invalidCharacters() {
        ValidatorResult result = validator.stringValidator("Card123!");
        assertFalse(result.isValid());
    }

    @Test
    void testValidateCardRarity_valid() {
        ValidatorResult result = validator.validateCardRarity("common");
        assertTrue(result.isValid());
    }

    @Test
    void testValidateCardRarity_invalid() {
        ValidatorResult result = validator.validateCardRarity("rarely");
        assertFalse(result.isValid());
    }

    @Test
    void testValidatePurchasePrice_valid() {
        ValidatorResult result = validator.validatePurchasePrice("19.99");
        assertTrue(result.isValid());
    }

    @Test
    void testValidatePurchasePrice_tooManyDecimals() {
        ValidatorResult result = validator.validatePurchasePrice("10.999");
        assertFalse(result.isValid());
    }

    @Test
    void testValidatePurchasePrice_negative() {
        ValidatorResult result = validator.validatePurchasePrice("-5.00");
        assertFalse(result.isValid());
    }

    @Test
    void testValidatePurchasePrice_tooHigh() {
        ValidatorResult result = validator.validatePurchasePrice("1000001");
        assertFalse(result.isValid());
    }

    @Test
    void testValidateIsFoiled_validTrue() {
        ValidatorResult result = validator.validateIsFoiled("true");
        assertTrue(result.isValid());
    }

    @Test
    void testValidateIsFoiled_invalid() {
        ValidatorResult result = validator.validateIsFoiled("yes");
        assertFalse(result.isValid());
    }

    @Test
    void testDateValidator_valid() {
        String validDate = LocalDate.now().toString();
        ValidatorResult result = validator.dateValidator(validDate, DateTimeFormatter.ISO_LOCAL_DATE);
        assertTrue(result.isValid());
    }

    @Test
    void testDateValidator_invalidFormat() {
        ValidatorResult result = validator.dateValidator("07-20-2025", DateTimeFormatter.ISO_LOCAL_DATE);
        assertFalse(result.isValid());
    }

    @Test
    void testDateValidator_outOfRange() {
        ValidatorResult result = validator.dateValidator("1800-01-01", DateTimeFormatter.ISO_LOCAL_DATE);
        assertFalse(result.isValid());
    }

    @Test
    void testValidateCard_validCard() {
        Card card = new Card();
        card.setCardNumber(123);
        card.setCardName("Charizard");
        card.setCardGame("Pokemon");
        card.setRarity(CardRarity.LEGENDARY);
        card.setPurchasePrice(new BigDecimal("99.99"));
        card.setDateSetPublished(LocalDate.of(2020, 1, 1));
        card.setDatePurchased(LocalDate.of(2021, 1, 1));

        ValidatorResult result = validator.validateCard(card);
        assertTrue(result.isValid());
    }

    @Test
    void testValidateCard_invalidFields() {
        Card card = new Card();
        card.setCardNumber(-5);
        card.setCardName("123@Invalid");
        card.setCardGame("");
        card.setRarity(null);
        card.setPurchasePrice(new BigDecimal("-10"));
        card.setDateSetPublished(null);
        card.setDatePurchased(LocalDate.of(1800, 1, 1));

        ValidatorResult result = validator.validateCard(card);
        assertFalse(result.isValid());
        assertTrue(result.getMessage().contains("empty") || result.getMessage().contains("invalid"));
    }
}
