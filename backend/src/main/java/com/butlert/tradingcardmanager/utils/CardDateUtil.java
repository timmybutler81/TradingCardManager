/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * June 18, 2025
 * CardDateUtil.java
 * This class is used to do date calculations that are used in other classes to create abstraction and keep the code
 * dry.
 */
package com.butlert.tradingcardmanager.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class CardDateUtil {
    public final int DAY_INTERVAL = 30;

    /**
     * method: calculateDaysSincePublished
     * parameters: publishDate
     * return: long
     * purpose: calculates the amount of days between publish and purchase date
     */
    public long calculateDaysSincePublished(LocalDate publishDate) {
        return ChronoUnit.DAYS.between(publishDate, LocalDate.now());
    }

    /**
     * method: calculateDayInterval
     * parameters: publishDate
     * return: long
     * purpose: calculates interval of number of days defined
     */
    public long calculateDayInterval(LocalDate publishDate) {
        long days = calculateDaysSincePublished(publishDate);
        return days / DAY_INTERVAL;
    }
}

