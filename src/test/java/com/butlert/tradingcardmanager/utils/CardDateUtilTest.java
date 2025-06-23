package com.butlert.tradingcardmanager.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CardDateUtilTest {

    private CardDateUtil util;

    @BeforeEach
    void setUp() {
        util = new CardDateUtil();
    }

    @Test
    void calculateDaysSincePublished_shouldReturnCorrectDays() {
        LocalDate pastDate = LocalDate.now().minusDays(100);
        long days = util.calculateDaysSincePublished(pastDate);
        assertEquals(100, days);
    }

    @Test
    void calculateDayInterval_shouldReturnCorrectIntervals() {
        LocalDate pastDate = LocalDate.now().minusDays(90); // 3 full 30-day intervals
        long intervals = util.calculateDayInterval(pastDate);
        assertEquals(3, intervals);
    }

    @Test
    void calculateDaysSincePublished_today_shouldReturnZero() {
        LocalDate today = LocalDate.now();
        long days = util.calculateDaysSincePublished(today);
        assertEquals(0, days);
    }

    @Test
    void calculateDayInterval_lessThanInterval_shouldReturnZero() {
        LocalDate pastDate = LocalDate.now().minusDays(10);
        long intervals = util.calculateDayInterval(pastDate);
        assertEquals(0, intervals);
    }
}
