package com.example.ntfctns.adap;

import static java.util.Collections.emptyList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        long currentTimeMillis = System.currentTimeMillis();
        long differenceMillis = currentTimeMillis - art.time;
        long hours = TimeUnit.MILLISECONDS.toHours(differenceMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis) % 60;
        String timeDifference = String.format(Locale.getDefault(), "%dh %dm ago / ", hours, minutes);
        String time = TimeConverter.convertToReadableTime(art.time);
        h.bnd.channel.setText(art.chnTitle);
        h.bnd.artTime.setText(timeDifference + time);
        h.bnd.keyword.setText(String.valueOf(art.keywords));
        if (!Objects.equals(art.image, "IMG")) {
            Glide.with(ctx).load(R.drawable.line).placeholder(R.drawable.load).into(h.bnd.divider);
            Glide.with(ctx).load(art.image).dontTransform().placeholder(R.drawable.load).into(h.bnd.img);
        } else {
            Glide.with(ctx).load(R.drawable.line).placeholder(R.drawable.load).into(h.bnd.divider);
            h.bnd.img.setVisibility(View.GONE);
        }
        setLink(h.bnd.link, art.link);
        SpannableStringBuilder textWithBold = boldWords(art.body, art.keywords);
        h.bnd.artBody.setText(textWithBold);
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
    @NonNull
    private static SpannableStringBuilder boldWords(String fullText, @NonNull List<String> wordsToBold) {
        SpannableStringBuilder sb = new SpannableStringBuilder(fullText);
        String lowerFullText = fullText.toLowerCase(Locale.ROOT);
        for (String item : wordsToBold) {
            if (item.contains("_")) {
                String[] splitWords = item.split("_");
                for (String wordToBold : splitWords) {
                    italicizeWord(sb, lowerFullText, wordToBold);
                }
            } else {
                italicizeWord(sb, lowerFullText, item);
            }
        }

        return sb;
    }

    private static void italicizeWord(SpannableStringBuilder sb, String lowerFullText, String wordToBold) {
        int startSpan = 0;
        while (startSpan != -1) {
            startSpan = lowerFullText.indexOf(wordToBold, startSpan);
            if (startSpan != -1) {
                int endSpan = startSpan + wordToBold.length();
                sb.setSpan(new StyleSpan(Typeface.BOLD), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.setSpan(new ForegroundColorSpan(Color.YELLOW), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startSpan = endSpan;
            }
        }
    }
}
