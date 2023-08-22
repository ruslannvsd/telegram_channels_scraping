package com.example.ntfctns.adap;

import static java.util.Collections.emptyList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.R;
import com.example.ntfctns.classes.Article;
import com.example.ntfctns.databinding.ArticleLBinding;
import com.example.ntfctns.utils.TimeConverter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class ArticleAd extends RecyclerView.Adapter<ArticleAd.ArticleViewHolder>{
    List<Article> articles = emptyList();
    Context ctx;
    @NonNull
    public ArticleViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        ArticleLBinding bnd = ArticleLBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArticleViewHolder(bnd);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ArticleAd.ArticleViewHolder h, int p) {
        Article art = articles.get(p);
        int color = cardBgColor(art.time, ctx);
        Log.i("Color", String.valueOf(color));
        h.bnd.card.setBackgroundColor(color);
        String time = TimeConverter.convertToReadableTime(art.time);
        h.bnd.keyword.setText(String.valueOf(art.keywords));
        if (!Objects.equals(art.image, "IMG")) {
            Log.i("Ad Img : ", art.image);
            Picasso.get().load(art.image).into(h.bnd.img);
        }
        setLink(h.bnd.tgLink, art.link);
        h.bnd.artBody.setText(art.body);
        h.bnd.artTime.setText(time);
    }
    @Override
    public int getItemCount() {
        return articles.size();
    }
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final ArticleLBinding bnd;
        public ArticleViewHolder(ArticleLBinding bnd) {
            super(bnd.getRoot());
            this.bnd = bnd;
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setArticles(List<Article> articles, Context ctx) {
        this.articles = articles;
        this.ctx = ctx;
        notifyDataSetChanged();
    }
    private void setLink(TextView tgLink, String link) {
        tgLink.setText(Html.fromHtml(link, Html.FROM_HTML_MODE_COMPACT));
        Linkify.addLinks(tgLink, Linkify.ALL);
        tgLink.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public int cardBgColor(long artTime, Context ctx) {
        long now = System.currentTimeMillis();
        long timeDiff = (artTime - now) / (3600000);
        String string = String.valueOf(timeDiff);
        Log.i("Now (milliseconds): ", TimeConverter.convertToReadableTime(now));
        Log.i("Art Time (milliseconds): ", TimeConverter.convertToReadableTime(artTime));
        Log.i("Hours : ", string);
        if (timeDiff == -1) {
            return ctx.getColor(R.color.one);
        } else if (timeDiff == -2 || timeDiff == -3) {
            return ctx.getColor(R.color.two);
        } else if (timeDiff <= -4 && timeDiff > -7) {
            return ctx.getColor(R.color.three);
        } else if (timeDiff <= -7 && timeDiff > -12) {
            return ctx.getColor(R.color.four);
        } else if (timeDiff <= -12 && timeDiff > -24) {
            return ctx.getColor(R.color.five);
        } else if (timeDiff <= -24 && timeDiff > -48) {
            return ctx.getColor(R.color.six);
        } else {
            return ctx.getColor(R.color.seven); // Replace with the actual color code for "seven"
        }
    }
}
