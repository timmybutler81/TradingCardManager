package com.butlert.tradingcardmanager.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for standardized date parsing and formatting throughout the application.
 *
 * <p>This component enforces a consistent date format (yyyy-MM-dd) for parsing strings
 * into {@link LocalDate} objects. It centralizes date-related logic to maintain DRY
 * principles and ensure uniformity across the system.</p>
 *
 * <p>Example of accepted date format: <code>2025-07-18</code></p>
 *
 * @author Timothy Butler
 * @version 1.0
 * @since June 18, 2025
 */
@Component
public class DateParser {
    private final DateTimeFormatter dateTimeFormatter;

    /**
     * Constructs a {@code DateParser} with a custom date format.
     *
     */
    public DateParser() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    /**
     * Parses a string into a {@link LocalDate} using the enforced date format (yyyy-MM-dd).
     *
     * @param localDateString the string representation of the date
     * @return the parsed {@link LocalDate}
     * @throws java.time.format.DateTimeParseException if the input does not match the expected format
     */
    public LocalDate parse(String localDateString) {
        return LocalDate.parse(localDateString, dateTimeFormatter);
    }

    /**
     * Returns the formatter used to parse and format dates.
     *
     * @return the configured {@link DateTimeFormatter}
     */
    public DateTimeFormatter getFormatter() {
        return dateTimeFormatter;
    }
}

