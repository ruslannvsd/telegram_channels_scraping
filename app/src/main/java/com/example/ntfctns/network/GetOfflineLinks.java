package com.example.ntfctns.network;

import static com.example.ntfctns.consts.Cons.MESSAGE_DIV;
import static com.example.ntfctns.consts.Cons.TEXT_DIV;

import android.content.Context;
import android.util.Log;

import com.example.ntfctns.classes.Article;
import com.example.ntfctns.classes.Keyword;
import com.example.ntfctns.consts.Cons;
import com.example.ntfctns.utils.ArticleMaking;
import com.example.ntfctns.utils.ListFuncs;
import com.example.ntfctns.utils.Saving;
import com.example.ntfctns.utils.WordFuncs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetOfflineLinks {
    public List<Keyword> getArticles(Context ctx, int time) {
        List<Keyword> keywords = new Saving().loadKeywords(ctx);
        List<String> links = Cons.CHANNELS;
        List<Article> artList = new ArrayList<>();
        Document doc;
        for (String link : links) {
            Log.i("Searching : ", link);
            try {
                doc = Jsoup.connect(link).timeout(30 * 1000).get();
                Elements messageSections = doc.select("div." + MESSAGE_DIV);
                String chnTitle = doc.select("meta[property=og:title]").first().attr("content");
                for (Element section : messageSections) {
                    Element articleBody = section.select("div." + TEXT_DIV).first();
                    if (articleBody != null) {
                        assert keywords != null;
                        for (Keyword keyword : keywords) {
                            String word = keyword.key;
                            String artBody = WordFuncs.replaceBR(articleBody);
                            String lower = artBody.toLowerCase();
                            if (!word.contains("_")) {
                                if (lower.contains(word.toLowerCase())) {
                                    Article art = new ArticleMaking().makeArticle(chnTitle, section, artBody, word, time);
                                    if (art != null) {
                                        artList.add(art);
                                    }
                                }
                            } else {
                                String[] splitWord = word.split("_");
                                if (lower.contains(splitWord[0].toLowerCase()) && lower.contains(splitWord[1].toLowerCase())) {
                                    Article art = new ArticleMaking().makeArticle(chnTitle, section, artBody, word, time);
                                    if (art != null) {
                                        artList.add(art);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("ERROR :", Objects.requireNonNull(e.getMessage()));
                throw new RuntimeException(e);
            }
        }
        List<Article> articles = new ListFuncs().merging(artList);
        assert keywords != null;
        List<Keyword> sortedList = new ListFuncs().results(keywords, articles);
        if (!sortedList.isEmpty()) {
            new Saving().saveKeywords(ctx, sortedList);
            new Saving().saveArticles(ctx, articles);
        }
        return sortedList;
    }
}
