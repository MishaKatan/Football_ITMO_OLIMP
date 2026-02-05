package com.sstats.footballstats.util;

import java.util.TimeZone;

public final class TimeZoneUtils {
    private TimeZoneUtils() {
    }

    public static int getTimeZoneOffsetHours() {
        int offsetMs = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        int hours = Math.round(offsetMs / 3600000f);
        if (hours < -12) {
            return -12;
        }
        if (hours > 12) {
            return 12;
        }
        return hours;
    }
}
