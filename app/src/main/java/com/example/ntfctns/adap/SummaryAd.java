package com.example.ntfctns.adap;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.databinding.SummaryLBinding;

import java.util.List;
import java.util.Map;

public class SummaryAd extends RecyclerView.Adapter<SummaryAd.SummaryViewHolder> {
    List<Map.Entry<String, Integer>> keywords;

    public SummaryAd.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SummaryLBinding bnd = SummaryLBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SummaryAd.SummaryViewHolder(bnd);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder h, int p) {
        String keyword = keywords.get(p).getKey() + " - " + keywords.get(p).getValue();
        h.bnd.keyword.setText(keyword);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder {
        private final SummaryLBinding bnd;
        public SummaryViewHolder(SummaryLBinding bnd) {
            super(bnd.getRoot());
            this.bnd = bnd;
        }
    }
    public void setKeywords(List<Map.Entry<String, Integer>> keywords) {
        this.keywords = keywords;
    }
}
