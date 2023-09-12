package com.example.ntfctns.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ntfctns.classes.Article;
import com.example.ntfctns.consts.Cons;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Saving {
    public void saveArticles(Context ctx, List<Article> articles) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(articles);
        editor.putString("articles", json);
        editor.apply();
    }

    public List<Article> loadArticles(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("articles", null);
        Type type = new TypeToken<List<Article>>() {}.getType();
        List<Article> articles = new Gson().fromJson(json, type);

        if (articles == null) {
            articles = new ArrayList<>();
        }
        return articles;
    }

    public void clearArticles(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("articles");
        editor.apply();
    }

    public void saveSummary(Context ctx, String summary) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("summary", summary);
        editor.apply();
    }

    public String loadSummary(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Cons.PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString("summary", "");
    }
}

