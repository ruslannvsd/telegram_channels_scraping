package com.example.ntfctns.utils;

import static java.lang.Integer.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ntfctns.classes.Article;
import com.example.ntfctns.classes.Keyword;
import com.example.ntfctns.consts.Cons;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListFuncs {
    public List<Keyword> results(List<Keyword> keywords, List<Article> articles) {
        int total = 0;
        int count = 0;
        for (Keyword word : keywords) {
            for (Article article : articles) {
                if (article.keywords.contains(word.key)) {
                    count++;
                }
            }
            word.setAmount(count);
            total += count;
            count = 0;
        }
        keywords.add(new Keyword(Cons.ALL, total));
        keywords.removeIf(kw -> kw.getAmount() == 0);
        keywords.sort((k1, k2) -> compare(k2.getAmount(), k1.getAmount()));
        Log.i("Custom list : ", keywords.get(0).key);
        return keywords;
    }

    public List<Article> sorting(@NonNull List<Article> artList) {
        return artList.stream().sorted(Comparator.comparingLong(article -> -article.time))
                .collect(Collectors.toList());
    }

    public List<Article> merging(@NonNull List<Article> articles) {
        List<Article> merged = new ArrayList<>(articles.stream()
                .collect(Collectors.toMap(
                        article -> article.link,
                        Function.identity(),
                        (article1, article2) -> {
                            article1.keywords.addAll(article2.keywords);
                            article1.keywords = new ArrayList<>(new HashSet<>(article1.keywords));
                            return article1;
                        }))
                .values());
        return sorting(merged);
    }
}
