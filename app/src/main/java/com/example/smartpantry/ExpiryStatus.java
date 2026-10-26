package com.example.smartpantry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

final class ExpiryStatus {
    private ExpiryStatus() {
    }

    static String label(String expiry, Calendar today) {
        if (expiry == null || expiry.isEmpty()) return "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setLenient(false);
        Date date;
        try {
            date = format.parse(expiry);
        } catch (ParseException exception) {
            return "";
        }

        Calendar start = (Calendar) today.clone();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar deadline = (Calendar) start.clone();
        deadline.add(Calendar.DAY_OF_MONTH, 3);
        if (date.before(start.getTime())) return "Expired";
        if (!date.after(deadline.getTime())) return "Soon";
        return "";
    }
}
