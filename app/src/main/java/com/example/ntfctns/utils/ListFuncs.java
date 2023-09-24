package com.example.ntfctns.utils;

import com.example.ntfctns.classes.Article;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListFuncs {
    public List<Map.Entry<String, Integer>> results(List<String> words, List<Article> articles) {
        Map<String, Integer> keywordFrequency = new HashMap<>();
        for (String word : words) {
            int count = 0;
            for (Article article : articles) {
                if (article.keywords.contains(word)) {
                    count++;
                }
            }
            if (count > 0) {
                keywordFrequency.put(word, count);
            }
        }
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(keywordFrequency.entrySet());
        sortedList.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        return sortedList;
    }

    public String listToString(List<Map.Entry<String, Integer>> sortedList) {
        StringBuilder string = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sortedList) {
            string.append(entry.getKey()).append(" - ").append(entry.getValue()).append("; ");
        }
        return string.length() > 0 ? string.toString().trim() : null;
    }

    public List<Article> sorting(List<Article> artList) {
        return artList.stream().sorted(Comparator.comparingLong(article -> -article.time))
                .collect(Collectors.toList());
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
