package com.example.ntfctns.adap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.R;
import com.example.ntfctns.classes.Keyword;
import com.example.ntfctns.databinding.SummaryLBinding;

import java.util.List;

public class SummaryAd extends RecyclerView.Adapter<SummaryAd.SummaryViewHolder> {
    int largest;
    int smallest;
    Context ctx;
    List<Keyword> keywords;
    private int pressed;
    private final OnKeywordClick onKeywordClick;

    @NonNull
    public SummaryAd.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SummaryLBinding bnd = SummaryLBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SummaryAd.SummaryViewHolder(bnd);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder h, int p) {
        Keyword keyword = keywords.get(p);
        String kw = keyword.getKey() + " - " + keyword.getAmount();
        int color = cardBgColor(keyword.amount, ctx);
        if (pressed == p) {
            h.bnd.card.setBackgroundColor(ctx.getColor(R.color.black));
        } else {
            h.bnd.card.setBackgroundColor(color);
        }
        h.bnd.keyword.setText(kw);
        h.itemView.setOnClickListener(v -> {
            notifyItemChanged(pressed);
            pressed = h.getAdapterPosition();
            notifyItemChanged(pressed);
            if (onKeywordClick != null) {
                onKeywordClick.onKeywordClick(keywords.get(p));
            }
        });
    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder {
        private final SummaryLBinding bnd;
        public SummaryViewHolder(@NonNull SummaryLBinding bnd) {
            super(bnd.getRoot());
            this.bnd = bnd;
        }
    }
    public void setKeywords(@NonNull List<Keyword> keywords, Context ctx, int pressed) {
        this.pressed = pressed;
        this.keywords = keywords;
        this.ctx = ctx;
        if (keywords.size() > 1) {
            this.largest = keywords.get(1).amount;
            this.smallest = keywords.get(keywords.size()-1).amount;
        }
    }
    public interface OnKeywordClick {
        void onKeywordClick(Keyword keyword);
    }
    public SummaryAd(OnKeywordClick onKeywordClick) {
        this.onKeywordClick = onKeywordClick;
    }
    public int cardBgColor(int quantity, Context ctx) {
        if (smallest == largest) {
            return ctx.getColor(R.color.two);
        }
        float range = (largest - smallest) / 5.0f;
        if (quantity > largest) {
            return ctx.getColor(R.color.yellow);
        } else if (quantity <= smallest + range) {
            return ctx.getColor(R.color.five);
        } else if (quantity <= smallest + 2 * range) {
            return ctx.getColor(R.color.four);
        } else if (quantity <= smallest + 3 * range) {
            return ctx.getColor(R.color.three);
        } else {
            return ctx.getColor(R.color.two);
        }
    }
}
