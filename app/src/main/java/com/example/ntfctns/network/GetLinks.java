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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.adap.ArticleAd;
import com.example.ntfctns.classes.Article;
import com.example.ntfctns.consts.Cons;
import com.example.ntfctns.utils.ArticleMaking;
import com.example.ntfctns.utils.ListFuncs;
import com.example.ntfctns.utils.TimeConverter;
import com.example.ntfctns.utils.WordFuncs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GetLinks {
    public void getArticles(List<String> words, RecyclerView rv, ArticleAd artAd, Context ctx, TextView txtView, int hours) {
        List<String> links = Cons.CHANNELS;
        //  single-threaded executor for sequential execution in the order of task submission
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        List<Article> artList = new ArrayList<>();
        // a callable task that will be executed by the executor service
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
                                        Article art = new ArticleMaking().makeArticle(section, artBody, word, hours);
                                        if (art != null) {
                                            artList.add(art);
                                        }
                                    }
                                }
                                else {
                                    String[] splitWord = word.split("_");
                                    if (lower.contains(splitWord[0].toLowerCase()) && lower.contains(splitWord[1].toLowerCase())) {
                                        Article art = new ArticleMaking().makeArticle(section, artBody, word, hours);
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
            Handler handler = new Handler(Looper.getMainLooper());
            if (artList.isEmpty()) {
                Toast.makeText(ctx, "Nothing has been found", Toast.LENGTH_LONG).show();
            } else {
                if (words.size() == 1) {
                    List<Article> articles = new ListFuncs().sorting(artList);
                    handler.post(() -> {
                        rv.setLayoutManager(new LinearLayoutManager(ctx));
                        artAd.setArticles(articles, ctx);
                        rv.setAdapter(artAd);
                        txtView.setText(String.valueOf(articles.size()));
                    });
                } else {
                    List<Article> articles = new ListFuncs().merging(artList);
                    handler.post(() -> {
                        rv.setLayoutManager(new LinearLayoutManager(ctx));
                        artAd.setArticles(articles, ctx);
                        rv.setAdapter(artAd);
                        String results = results(words, articles);
                        if (results != null) {
                            txtView.setText(results);
                        }
                    });
                }
            }
            return null;
        };
        executorService.submit(task);
    }


    private String results(List<String> words, List<Article> articles) {
        StringBuilder string = new StringBuilder();
        int amount = 0;
        for (String word : words) {
            for (Article article : articles) {
                if (article.keywords.contains(word)) {
                    amount++;
                }
            }
            string.append(word).append(" - ").append(amount).append("; ");
            amount = 0;
        }
        if (!string.toString().equals("")) {
            return string.toString().trim();
        }
        else {
            return null;
        }
    }
}
