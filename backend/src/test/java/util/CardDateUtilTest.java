package util;

import com.butlert.tradingcardmanager.utils.CardDateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardDateUtilTest {

    private CardDateUtil dateUtil;

    @BeforeEach
    void setUp() {
        dateUtil = new CardDateUtil();
    }

    @Test
    void testCalculateDaysSincePublished_withPastDate_returnsCorrectDays() {
        LocalDate pastDate = LocalDate.now().minusDays(45);
        long result = dateUtil.calculateDaysSincePublished(pastDate);
        assertEquals(45, result);
    }

    @Test
    void testCalculateDaysSincePublished_withToday_returnsZero() {
        LocalDate today = LocalDate.now();
        long result = dateUtil.calculateDaysSincePublished(today);
        assertEquals(0, result);
    }

    @Test
    void testCalculateDaysSincePublished_withFutureDate_returnsNegativeDays() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        long result = dateUtil.calculateDaysSincePublished(futureDate);
        assertEquals(-10, result);
    }

    @Test
    void testCalculateDayInterval_returnsCorrectIntervals() {
        LocalDate pastDate = LocalDate.now().minusDays(90);
        long result = dateUtil.calculateDayInterval(pastDate);
        assertEquals(3, result);  // 90 / 30
    }

    @Test
    void testCalculateDayInterval_withToday_returnsZero() {
        LocalDate today = LocalDate.now();
        long result = dateUtil.calculateDayInterval(today);
        assertEquals(0, result);
    }

    @Test
    void testCalculateDayInterval_withLessThan30Days_returnsZero() {
        LocalDate pastDate = LocalDate.now().minusDays(20);
        long result = dateUtil.calculateDayInterval(pastDate);
        assertEquals(0, result);
    }

    @Test
    void testCalculateDayInterval_withFutureDate_returnsNegativeIntervals() {
        LocalDate futureDate = LocalDate.now().plusDays(60);
        long result = dateUtil.calculateDayInterval(futureDate);
        assertEquals(-2, result);
    }
}
