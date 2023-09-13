package com.example.ntfctns.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ntfctns.classes.Article;
import com.example.ntfctns.consts.Cons;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Saving {
    public void saveArticles(Context ctx, List<Article> articles) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(articles);
        editor.putString(Cons.ART_KEY, json);
        Log.d("custom StoredJSON", json);
        editor.apply();
    }

    public List<Article> loadArticles(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Cons.ART_KEY, "");

        if (json.length() == 0) {
            Log.d("custom LoadedJSON", json);
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<Article>>() {}.getType();
        try {
            List<Article> articles = new Gson().fromJson(json, type);
            return articles != null ? articles : new ArrayList<>();
        } catch (JsonSyntaxException e) {
            Log.e("JsonSyntaxException", Objects.requireNonNull(e.getMessage()));
            return new ArrayList<>();
        }
    }

    public void clearPrefs(Context ctx, String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void saveSummary(Context ctx, String summary, String key) {
        Log.i("custom Saving Summary", summary);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, summary);
        editor.apply();
    }

    public String loadSummary(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Cons.SUMMARY_KEY, "");
    }
}

