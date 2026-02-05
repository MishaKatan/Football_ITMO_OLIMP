package com.sstats.footballstats.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {
    private static final String[] PARSE_PATTERNS = new String[] {
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm"
    };

    private DateUtils() {
    }

    public static String formatDateTime(String input) {
        if (input == null) {
            return "";
        }
        Date date = parseDate(input);
        if (date == null) {
            return input;
        }
        SimpleDateFormat out = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US);
        out.setTimeZone(TimeZone.getDefault());
        return out.format(date);
    }

    public static Date parseDate(String input) {
        for (String pattern : PARSE_PATTERNS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
                sdf.setLenient(true);
                return sdf.parse(input);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    public static String formatForApi(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }
}
