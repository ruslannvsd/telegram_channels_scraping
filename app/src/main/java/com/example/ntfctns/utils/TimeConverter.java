package com.example.ntfctns.utils;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.Instant;


public class TimeConverter {

    private static final String TG_TIME = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static final String ART_TIME = "hh.mma dd/MM/yy";

    public static long convertToMillis(String datetimeStr) {
        Log.i("custom datetimeStr", datetimeStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TG_TIME);
        LocalDateTime dateTime = LocalDateTime.parse(datetimeStr, formatter);
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String convertToReadableTime(long millis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ART_TIME);
        return dateTime.format(formatter).toUpperCase();
    }
}

