package com.example.ntfctns.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ntfctns.classes.Article;
import com.example.ntfctns.classes.Keyword;
import com.example.ntfctns.consts.Cons;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Saving {
    public void clearPrefs(@NonNull Context ctx, String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void saveArticles(@NonNull Context ctx, List<Article> articles) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(articles);
        editor.putString(Cons.ART_KEY, json);
        editor.apply();
    }

    public List<Article> loadArticles(@NonNull Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Cons.ART_KEY, "");

        if (json.length() == 0) {
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

    public void saveText(@NonNull Context ctx, String text, String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, text);
        editor.apply();
    }

    public String loadText(@NonNull Context ctx, String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void saveWords(@NonNull Context ctx, List<String> words) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonWords = gson.toJson(words);
        editor.putString(Cons.WORDS_KEY, jsonWords);
        editor.apply();
    }

    @Nullable
    public List<String> loadWords(@NonNull Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        String jsonWords = sharedPreferences.getString(Cons.WORDS_KEY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(jsonWords, type);
    }

    public void saveKeywords(Context ctx, List<Keyword> keywords) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(keywords);
        editor.putString(Cons.KEYW_KEY, json);
        editor.apply();
    }

    public List<Keyword> loadKeywords(@NonNull Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Cons.KEYW_KEY, "");

        if (json.length() == 0) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<Keyword>>() {}.getType();
        try {
            List<Keyword> keywords = new Gson().fromJson(json, type);
            return keywords != null ? keywords : new ArrayList<>();
        } catch (JsonSyntaxException e) {
            Log.e("JsonSyntaxException", Objects.requireNonNull(e.getMessage()));
            return new ArrayList<>();
        }
    }
}

