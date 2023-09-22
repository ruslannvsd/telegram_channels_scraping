package com.example.ntfctns.utils;

import android.content.Context;

import com.example.ntfctns.consts.Cons;

import java.util.Objects;

public class Hours {
    public int getHours(Context ctx) {
        String hoursStr = new Saving().loadText(ctx, Cons.HOURS_KEY);
        if (!Objects.equals(hoursStr, "")) {
            return Integer.parseInt(hoursStr);
        } else {
            return 1;
        }
    }
}
