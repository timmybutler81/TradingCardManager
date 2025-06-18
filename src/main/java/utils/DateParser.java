package main.java.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser {
    private final DateTimeFormatter dateTimeFormatter;

    public DateParser() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public LocalDate parse(String input) {
        LocalDate localDate = LocalDate.parse(input, dateTimeFormatter);
        return LocalDate.parse(input, dateTimeFormatter);
    }

    public DateTimeFormatter getFormatter() {
        return dateTimeFormatter;
    }
}
