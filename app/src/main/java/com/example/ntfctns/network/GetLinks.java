package com.example.ntfctns.network;

import static com.example.ntfctns.consts.Cons.ART_META;
import static com.example.ntfctns.consts.Cons.DATETIME;
import static com.example.ntfctns.consts.Cons.D_TIME;
import static com.example.ntfctns.consts.Cons.LINK;
import static com.example.ntfctns.consts.Cons.MESSAGE_DIV;
import static com.example.ntfctns.consts.Cons.SECTION;
import static com.example.ntfctns.consts.Cons.TEXT_DIV;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.adap.ArticleAd;
import com.example.ntfctns.classes.Article;
import com.example.ntfctns.consts.Cons;
import com.example.ntfctns.utils.TimeConverter;
import com.example.ntfctns.utils.WordFuncs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GetLinks {


    public void getLinks(List<String> words, RecyclerView rv, ArticleAd artAd, Context ctx) {
        List<String> links = Cons.channels;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        List<Article> artList = new ArrayList<>();
        Callable<Void> task = () -> {
            Document doc;
            for (String link : links) {
                Log.i("Link", link);
                try {
                    doc = Jsoup.connect(link).timeout(20 * 1000).get();
                    Elements messageSections = doc.select("div." + MESSAGE_DIV);
                    for (Element section : messageSections) {
                        Element articleBody = section.select("div." + TEXT_DIV).first();
                        if (articleBody != null) {
                            for (String word : words) {
                                String artBody = WordFuncs.replaceBR(articleBody);
                                String lower = artBody.toLowerCase();
                                if (!word.contains("_")) {
                                    if (lower.contains(word.toLowerCase())) {
                                        Article art = makeArticle(section, artBody, word);
                                        artList.add(art);
                                    }
                                }
                                else {
                                    String[] splitWord = word.split("_");
                                    if (lower.contains(splitWord[0].toLowerCase()) && lower.contains(splitWord[1].toLowerCase())) {
                                        Article art = makeArticle(section, artBody, word);
                                        artList.add(art);
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
            Handler handler = new Handler(Looper.getMainLooper());
            if (artList.isEmpty()) {
                Toast.makeText(ctx, "Nothing has been found", Toast.LENGTH_LONG).show();
            } else {
                if (words.size() == 1) {
                    List<Article> articles = sorting(artList);
                    handler.post(() -> {
                        rv.setLayoutManager(new LinearLayoutManager(ctx));
                        artAd.setArticles(articles, ctx);
                        rv.setAdapter(artAd);
                    });
                } else {
                    List<Article> articles = merging(artList);
                    handler.post(() -> {
                        rv.setLayoutManager(new LinearLayoutManager(ctx));
                        artAd.setArticles(articles, ctx);
                        rv.setAdapter(artAd);
                    });
                }
            }
            return null;
        };
        executorService.submit(task);
    }
    private Article makeArticle(Element el, String body, String keyW) {
        String imgLink = WordFuncs.getLink(el);
        String articleTime = el.select("span." + ART_META + DATETIME).attr(D_TIME);
        long millis = TimeConverter.convertToMillis(articleTime);
        Element linkElement = el.select("span." + ART_META + " a." + SECTION).first();
        String art_link = linkElement.attr(LINK);
        List<String> keywords = new ArrayList<>();
        keywords.add(keyW);
        return new Article(imgLink, art_link, body, millis, keywords);
    }
    private List<Article> sorting(List<Article> artList) {
        return artList.stream()
                .sorted(Comparator.comparingLong(article -> -article.time))
                .collect(Collectors.toList());
    }
    private List<Article> merging(List<Article> articles) {
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
