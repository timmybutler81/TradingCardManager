package com.butlert.tradingcardmanager.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class DateParserTest {

    private DateParser parser;

    @BeforeEach
    void setUp() {
        parser = new DateParser();
    }

    @Test
    void parse_validDate_returnsLocalDate() {
        String input = "2023-06-22";
        LocalDate expected = LocalDate.of(2023, 6, 22);
        LocalDate result = parser.parse(input);
        assertEquals(expected, result);
    }

    @Test
    void parse_invalidDate_throwsException() {
        String input = "invalid-date";
        assertThrows(DateTimeParseException.class, () -> parser.parse(input));
    }

    @Test
    void getFormatter_shouldFormatCorrectly() {
        DateParser parser = new DateParser();
        DateTimeFormatter formatter = parser.getFormatter();

        String result = formatter.format(LocalDate.of(2025, 6, 22));
        assertEquals("2025-06-22", result);
    }
}
