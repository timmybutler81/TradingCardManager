package main.java.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CardDateUtil {
    public long calculateDaysSincePublished(LocalDate publishDate) {
        return ChronoUnit.DAYS.between(publishDate, LocalDate.now());
    }

    public long calculateThirtyDayIntervals(LocalDate publishDate) {
        long days = calculateDaysSincePublished(publishDate);
        return days / 30;
    }
}
