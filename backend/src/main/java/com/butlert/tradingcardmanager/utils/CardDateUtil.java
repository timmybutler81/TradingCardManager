package com.butlert.tradingcardmanager.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for performing date-related calculations on card records.
 *
 * <p>This class abstracts common date logic to promote code reuse and maintainability.
 * It is particularly useful for calculating time-based intervals used in card valuation.</p>
 *
 * <p>Example uses include calculating days since a card's set was published or determining
 * how many 30-day intervals have passed since that date.</p>
 *
 * @author Timothy Butler
 * @version 1.0
 * @since June 18, 2025
 */
@Component
public class CardDateUtil {
    /** The number of days in a single interval used for valuation calculations. */
    public final int DAY_INTERVAL = 30;

    /**
     * Default constructor for {@link CardDateUtil}.
     * <p>
     * No initialization logic is required as this utility relies entirely on
     * stateless method calls and a constant interval value.
     * </p>
     */
    public CardDateUtil() {

    }

    /**
     * Calculates the number of days between the provided publish date and today.
     *
     * @param publishDate the date the card set was published
     * @return the number of days since the publish date
     */
    public long calculateDaysSincePublished(LocalDate publishDate) {
        return ChronoUnit.DAYS.between(publishDate, LocalDate.now());
    }

    /**
     * Calculates how many complete 30-day intervals have passed since the given publish date.
     *
     * @param publishDate the date the card set was published
     * @return the number of full 30-day intervals since the publish date
     */
    public long calculateDayInterval(LocalDate publishDate) {
        long days = calculateDaysSincePublished(publishDate);
        return days / DAY_INTERVAL;
    }
}

