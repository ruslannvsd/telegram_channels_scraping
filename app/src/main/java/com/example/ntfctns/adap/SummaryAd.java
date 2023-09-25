package com.example.ntfctns.adap;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.classes.Keyword;
import com.example.ntfctns.databinding.SummaryLBinding;

import java.util.List;

public class SummaryAd extends RecyclerView.Adapter<SummaryAd.SummaryViewHolder> {
    List<Keyword> keywords;

    public SummaryAd.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SummaryLBinding bnd = SummaryLBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SummaryAd.SummaryViewHolder(bnd);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder h, int p) {
        String keyword = keywords.get(p).getKey() + " - " + keywords.get(p).getAmount();
        h.bnd.keyword.setText(keyword);
    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder {
        private final SummaryLBinding bnd;
        public SummaryViewHolder(SummaryLBinding bnd) {
            super(bnd.getRoot());
            this.bnd = bnd;
        }
    }
    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }
}
