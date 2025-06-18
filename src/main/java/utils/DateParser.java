/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * DateParser.java
 * This is a date utility class to enforce my standard of date formatting. It ensures that all dates are formatted
 * and a reusable class.
 */
package main.java.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser {
    private final DateTimeFormatter dateTimeFormatter;

    public DateParser() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    /**
     * method: parse
     * parameters: input
     * return: LocalDate
     * purpose: returns a proper formatted date from a string from the file
     */
    public LocalDate parse(String localDateString) {
        LocalDate localDate = LocalDate.parse(localDateString, dateTimeFormatter);
        return LocalDate.parse(localDateString, dateTimeFormatter);
    }


    public DateTimeFormatter getFormatter() {
        return dateTimeFormatter;
    }
}
