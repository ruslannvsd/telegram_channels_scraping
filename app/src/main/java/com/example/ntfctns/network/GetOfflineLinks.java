package com.example.ntfctns.network;

import static com.example.ntfctns.consts.Cons.MESSAGE_DIV;
import static com.example.ntfctns.consts.Cons.TEXT_DIV;

import android.content.Context;
import android.util.Log;

import com.example.ntfctns.classes.Article;
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
    public String getArticles(Context ctx, int time) {
        String text = null;
        List<String> keywords = Cons.KEYWORDS;
        List<String> links = Cons.CHANNELS;
        List<Article> artList = new ArrayList<>();
        Document doc;
        for (String link : links) {
            Log.i("Link", link);
            try {
                doc = Jsoup.connect(link).timeout(30 * 1000).get();
                Elements messageSections = doc.select("div." + MESSAGE_DIV);
                for (Element section : messageSections) {
                    Element articleBody = section.select("div." + TEXT_DIV).first();
                    if (articleBody != null) {
                        for (String word : keywords) {
                            String artBody = WordFuncs.replaceBR(articleBody);
                            String lower = artBody.toLowerCase();
                            if (!word.contains("_")) {
                                if (lower.contains(word.toLowerCase())) {
                                    Article art = new ArticleMaking().makeArticle(section, artBody, word, time);
                                    if (art != null) {
                                        artList.add(art);
                                    }
                                }
                            } else {
                                String[] splitWord = word.split("_");
                                if (lower.contains(splitWord[0].toLowerCase()) && lower.contains(splitWord[1].toLowerCase())) {
                                    Article art = new ArticleMaking().makeArticle(section, artBody, word, time);
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
        for (Article art : articles) {
            Log.i("custom Article : ", art.link);
        }
        String results = new ListFuncs().results(keywords, articles);
        if (results != null) {
            text = results;
            new Saving().saveSummary(ctx, results, Cons.SUMMARY_KEY);
            new Saving().saveArticles(ctx, articles);
        }
        return text;
    }
}
