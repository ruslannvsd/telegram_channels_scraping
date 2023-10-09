package com.example.ntfctns.network;

import static com.example.ntfctns.consts.Cons.MESSAGE_DIV;
import static com.example.ntfctns.consts.Cons.TEXT_DIV;

import static java.lang.Integer.*;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.adap.ArticleAd;
import com.example.ntfctns.adap.SummaryAd;
import com.example.ntfctns.classes.Article;
import com.example.ntfctns.classes.Keyword;
import com.example.ntfctns.consts.Cons;
import com.example.ntfctns.utils.ArticleMaking;
import com.example.ntfctns.utils.ListFuncs;
import com.example.ntfctns.utils.WordFuncs;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetLinks {
    RecyclerView artRv;
    Context ctx;
    PopupWindow window;
    ArticleAd artAd;
    SummaryAd sumAd;
    RecyclerView sumRv;

    public void getArticles(List<Keyword> keywords,
                            RecyclerView artRv,
                            ArticleAd artAd,
                            SummaryAd sumAd,
                            Context ctx,
                            RecyclerView sumRv,
                            int hours,
                            PopupWindow window
    ) {
        this.artRv = artRv;
        this.ctx = ctx;
        this.window = window;
        this.artAd = artAd;
        this.sumAd = sumAd;
        this.sumRv = sumRv;
        List<String> links = Cons.CHANNELS;
        //  single-threaded executor for sequential execution in the order of task submission
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        List<Article> artList = new ArrayList<>();
        // a callable task that will be executed by the executor service
        Callable<Void> task = () -> {
            Document doc;
            for (String link : links) {
                try {
                    doc = Jsoup.connect(link).timeout(20 * 1000).get();
                    Elements messageSections = doc.select("div." + MESSAGE_DIV);
                    String chnTitle = doc.select("meta[property=og:title]").first().attr("content");
                    for (Element section : messageSections) {
                        Elements allTextDivs = section.select("div." + TEXT_DIV);
                        for (Element articleBody : allTextDivs) {
                            if (articleBody.parent() != null && !articleBody.parent().hasClass("tgme_widget_message_reply")) {
                                for (Keyword keyword : keywords) {
                                    String word = keyword.key;
                                    String artBody = WordFuncs.replaceBR(articleBody);
                                    String lower = artBody.toLowerCase();
                                    if (!word.contains("_")) {
                                        if (lower.contains(word.toLowerCase())) {
                                            Article art = new ArticleMaking().makeArticle(chnTitle, section, artBody, word, hours);
                                            if (art != null) {
                                                artList.add(art);
                                            }
                                        }
                                    } else {
                                        String[] splitWord = word.split("_");
                                        if (lower.contains(splitWord[0].toLowerCase()) && lower.contains(splitWord[1].toLowerCase())) {
                                            Article art = new ArticleMaking().makeArticle(chnTitle, section, artBody, word, hours);
                                            if (art != null) {
                                                artList.add(art);
                                            }
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
                handler.post(() -> {
                    adPopulating(Collections.emptyList(), Collections.emptyList());
                    Toast.makeText(ctx, "Nothing has been found", Toast.LENGTH_LONG).show();
                });
            } else {
                if (keywords.size() == 1) {
                    List<Article> articles = new ListFuncs().sorting(artList);
                    handler.post(() -> adPopulating(articles, keywords));
                } else {
                    List<Article> articles = new ListFuncs().merging(artList);
                    keywords.add(0, new Keyword(Cons.ALL, articles.size()));
                    handler.post(() -> {
                        List<Keyword> results = results(keywords, articles);
                        adPopulating(articles, results);
                    });
                }
            }
            return null;
        };
        executorService.submit(task);
    }


    @NonNull
    private List<Keyword> results(@NonNull List<Keyword> keywords, List<Article> articles) {
        int count = 0;
        for (Keyword keyword : keywords) {
            String word = keyword.key;
            for (Article article : articles) {
                if (article.keywords.contains(word)) {
                    count++;
                }
            }
            keyword.setAmount(count);
            count = 0;
        }
        keywords.removeIf(kw -> kw.getAmount() == 0);
        keywords.sort((k1, k2) -> compare(k2.getAmount(), k1.getAmount()));
        return keywords;
    }

    private void adPopulating(List<Article> list, List<Keyword> keywords) {
        window.dismiss();
        artRv.setLayoutManager(new LinearLayoutManager(ctx));
        artAd.setArticles(list, ctx);
        artRv.setAdapter(artAd);
        FlexboxLayoutManager layM = new FlexboxLayoutManager(ctx);
        layM.setJustifyContent(JustifyContent.FLEX_START);
        sumRv.setLayoutManager(layM);
        sumAd.setKeywords(keywords, ctx, -1);
        sumRv.setAdapter(sumAd);
    }
}
