package com.example.ntfctns.adap;

import static java.util.Collections.emptyList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.databinding.WordsBinding;

import java.util.List;

public class WordsAd extends RecyclerView.Adapter<WordsAd.WordsViewHolder> {
    List<String> words = emptyList();
    Context ctx;
    final private OnDeleteWord onDeleteWord;

    public WordsAd(OnDeleteWord onDeleteWord) {
        this.onDeleteWord = onDeleteWord;
    }

    @NonNull
    @Override
    public WordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WordsBinding bnd = WordsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WordsViewHolder(bnd, onDeleteWord);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsAd.WordsViewHolder h, int p) {
        String word = words.get(p);
        h.bnd.word.setText(word);
    }

    @Override
    public int getItemCount() { return words.size(); }

    @SuppressLint("NotifyDataSetChanged")

    public void setWords(List<String> words, Context ctx) {
        this.words = words;
        this.ctx = ctx;
        notifyDataSetChanged();
    }

    public static class WordsViewHolder extends RecyclerView.ViewHolder {
        private final WordsBinding bnd;

        public WordsViewHolder(@NonNull WordsBinding bnd, final OnDeleteWord onDeleteWord) {
            super(bnd.getRoot());
            this.bnd = bnd;

            itemView.setOnLongClickListener(view -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onDeleteWord.onDeleteWord(bnd.word.getText().toString());
                }
                return true;
            });
        }
    }

    public interface OnDeleteWord {
        void onDeleteWord(String word);
    }
}
