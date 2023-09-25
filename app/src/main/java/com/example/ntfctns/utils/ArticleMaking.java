package com.example.ntfctns.utils;

import static com.example.ntfctns.consts.Cons.ART_META;
import static com.example.ntfctns.consts.Cons.DATETIME;
import static com.example.ntfctns.consts.Cons.D_TIME;
import static com.example.ntfctns.consts.Cons.LINK;
import static com.example.ntfctns.consts.Cons.SECTION;

import androidx.annotation.Nullable;

import com.example.ntfctns.classes.Article;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class ArticleMaking {
    @Nullable
    public Article makeArticle(Element el, String body, String keyW, int hours) {
        if (hours != 0) {
            long now = System.currentTimeMillis();
            long hoursMilli = (long) hours * 60 * 60 * 1000;
            long threshold = now - hoursMilli;
            String articleTime = el.select("span." + ART_META + DATETIME).attr(D_TIME);
            long millis = TimeConverter.convertToMillis(articleTime);
            if (millis >= threshold) {
                String imgLink = WordFuncs.getLink(el);
                Element linkElement = el.select("span." + ART_META + " a." + SECTION).first();
                String artLink = linkElement.attr(LINK);
                List<String> keywords = new ArrayList<>();
                keywords.add(keyW);
                return new Article(imgLink, artLink, body, millis, keywords);
            }
        } else {
            String articleTime = el.select("span." + ART_META + DATETIME).attr(D_TIME);
            long millis = TimeConverter.convertToMillis(articleTime);
            String imgLink = WordFuncs.getLink(el);
            Element linkElement = el.select("span." + ART_META + " a." + SECTION).first();
            String art_link = linkElement.attr(LINK);
            List<String> keywords = new ArrayList<>();
            keywords.add(keyW);
            return new Article(imgLink, art_link, body, millis, keywords);
        }
        return null;
    }
}
