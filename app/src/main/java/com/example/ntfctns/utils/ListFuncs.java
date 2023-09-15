package com.example.ntfctns.utils;

import android.util.Log;

import com.example.ntfctns.classes.Article;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListFuncs {
    public String results(List<String> words, List<Article> articles) {
        StringBuilder string = new StringBuilder();
        int amount = 0;
        for (String word : words) {
            for (Article article : articles) {
                if (article.keywords.contains(word)) {
                    amount++;
                }
            }
            if (amount != 0) {
                string.append(word).append(" - ").append(amount).append("; ");
            }
            amount = 0;
        }
        if (!string.toString().equals("")) {
            return string.toString().trim();
        } else {
            return null;
        }
    }
    public List<Article> sorting(List<Article> artList) {
        List<Article> sorted = artList.stream()
                .sorted(Comparator.comparingLong(article -> -article.time))
                .collect(Collectors.toList());
        return sorted;
    }

    public List<Article> merging(List<Article> articles) {
        List<Article> merged = new ArrayList<>(articles.stream()
                .collect(Collectors.toMap(
                        article -> article.link,
                        Function.identity(),
                        (article1, article2) -> {
                            article1.keywords.addAll(article2.keywords);
                            return article1;
                        }))
                .values());
        return sorting(merged);
    }
}
