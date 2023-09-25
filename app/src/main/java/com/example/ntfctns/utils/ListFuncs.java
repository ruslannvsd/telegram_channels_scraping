package com.example.ntfctns.utils;

import static java.lang.Integer.*;
import static java.util.Collections.*;

import com.example.ntfctns.classes.Article;
import com.example.ntfctns.classes.Keyword;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListFuncs {
    public List<Keyword> results(List<Keyword> keywords, List<Article> articles) {
        int count = 0;
        for (Keyword word : keywords) {
            for (Article article : articles) {
                if (article.keywords.contains(word.key)) {
                    count++;
                }
            }
            word.setAmount(count);
            count = 0;
        }
        Iterator<Keyword> iterator = keywords.iterator();
        while (iterator.hasNext()) {
            Keyword kw = iterator.next();
            if (kw.getAmount() == 0) {
                iterator.remove();
            }
        }
        sort(keywords, (k1, k2) -> compare(k2.getAmount(), k1.getAmount()));
        return keywords;
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
