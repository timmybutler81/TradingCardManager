package com.butlert.tradingcardmanager.utils;

import com.butlert.tradingcardmanager.model.Card;
import com.butlert.tradingcardmanager.model.CardRarity;
import com.butlert.tradingcardmanager.utils.validation.CardValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CardParserTest {

    private CardParser parser;

    @BeforeEach
    void setUp() {
        DateParser dateParser = new DateParser();
        CardValidator validator = new CardValidator();
        parser = new CardParser(dateParser, validator);
    }

    @Test
    void parseLine_validLine_returnsCard() {
        String line = "1 - Pokemon - Pikachu - Rare - 2023-06-22 - 2022-01-01 - 10.50 - true";
        Optional<Card> result = parser.parseLine(line);

        assertTrue(result.isPresent());
        Card card = result.get();


        assertEquals(1, card.getCardNumber());
        assertEquals("Pokemon", card.getCardGame());
        assertEquals("Pikachu", card.getCardName());
        assertEquals(CardRarity.RARE, card.getRarity());
        assertEquals(LocalDate.of(2023, 6, 22), card.getDatePurchased());
        assertEquals(LocalDate.of(2022, 1, 1), card.getDateSetPublished());
        assertEquals(new BigDecimal("10.50"), card.getPurchasePrice());
        assertTrue(card.isFoiled());
    }

    @Test
    void parseLine_missingFields_throwsException() {
        String line = "1 - Pokemon - Pikachu - Rare - 2023-06-22 - 2022-01-01 - 10.50";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parseLine(line));
        assertTrue(exception.getMessage().contains("File must have all 8 fields"));
    }

    @Test
    void parseLine_invalidRarity_throwsException() {
        String line = "1 - Pokemon - Pikachu - Unknown - 2023-06-22 - 2022-01-01 - 10.50 - true";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parseLine(line));
        assertTrue(exception.getMessage().contains("Rarity error:"));
    }

    @Test
    void parseLine_invalidDate_throwsException() {
        String line = "1 - Pokemon - Pikachu - Rare - not-a-date - 2022-01-01 - 10.50 - true";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parseLine(line));
        assertTrue(exception.getMessage().contains("Date Purchased error: "));
    }

    @Test
    void parseLine_invalidPrice_throwsException() {
        String line = "1 - Pokemon - Pikachu - Rare - 2023-06-22 - 2022-01-01 - not-a-price - true";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parseLine(line));
        assertTrue(exception.getMessage().contains("Purchase Price error: "));
    }

    @Test
    void parseLine_invalidFoil_throwsException() {
        String line = "1 - Pokemon - Pikachu - Rare - 2023-06-22 - 2022-01-01 - 10.50 - not-a-boolean";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parser.parseLine(line));
        assertTrue(exception.getMessage().contains("Is Foiled error: "));
    }
}