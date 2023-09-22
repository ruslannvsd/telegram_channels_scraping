package com.example.ntfctns.utils;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.util.Locale;


public class TimeConverter {
    private static final String TG_TIME = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static final String ART_TIME = "hh.mma dd/MM/yy";
    public static long convertToMillis(String datetimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TG_TIME);
        LocalDateTime dateTime = LocalDateTime.parse(datetimeStr, formatter);
        return dateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    @NonNull
    public static String convertToReadableTime(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ART_TIME);
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format(
                Locale.getDefault(),
                "%dh %dm ago / %s",
                hours,
                minutes,
                dateTime.format(formatter).toUpperCase()
        );
    }
}
