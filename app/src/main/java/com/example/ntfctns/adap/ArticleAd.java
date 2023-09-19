package com.example.ntfctns.adap;

import static java.util.Collections.emptyList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ntfctns.R;
import com.example.ntfctns.classes.Article;
import com.example.ntfctns.databinding.ArticleLBinding;
import com.example.ntfctns.utils.TimeConverter;

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
        h.bnd.card.setBackgroundColor(color);
        String time = TimeConverter.convertToReadableTime(art.time);
        h.bnd.keyword.setText(String.valueOf(art.keywords));
        if (!Objects.equals(art.image, "IMG")) {
            Glide.with(ctx).load(art.image).placeholder(R.drawable.load).into(h.bnd.img);
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
        if (timeDiff == 0) {
            return ctx.getColor(R.color.one);
        } else if (timeDiff == -1 || timeDiff == -2) {
            return ctx.getColor(R.color.two);
        } else if (timeDiff <= -3 && timeDiff > -6) {
            return ctx.getColor(R.color.three);
        } else if (timeDiff <= -6 && timeDiff > -11) {
            return ctx.getColor(R.color.four);
        } else if (timeDiff <= -11 && timeDiff > -23) {
            return ctx.getColor(R.color.five);
        } else if (timeDiff <= -23 && timeDiff > -47) {
            return ctx.getColor(R.color.six);
        } else {
            return ctx.getColor(R.color.seven);
        }
    }
}
