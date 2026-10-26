package com.example.smartpantry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;

public class ExpiryStatusTest {
    @Test
    public void marksExpiredAndUpcomingDates() {
        Calendar today = Calendar.getInstance();
        today.set(2026, Calendar.OCTOBER, 26, 12, 0, 0);
        today.set(Calendar.MILLISECOND, 0);

        assertEquals("Expired", ExpiryStatus.label("2026-10-25", today));
        assertEquals("Soon", ExpiryStatus.label("2026-10-26", today));
        assertEquals("Soon", ExpiryStatus.label("2026-10-29", today));
        assertEquals("", ExpiryStatus.label("2026-10-30", today));
        assertEquals("", ExpiryStatus.label("2026-02-30", today));
        assertEquals("", ExpiryStatus.label("", today));
    }
}
