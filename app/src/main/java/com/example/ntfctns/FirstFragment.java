package com.example.ntfctns;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.adap.ArticleAd;
import com.example.ntfctns.adap.SummaryAd;
import com.example.ntfctns.classes.Article;
import com.example.ntfctns.classes.Keyword;
import com.example.ntfctns.consts.Cons;
import com.example.ntfctns.databinding.FragmentFirstBinding;
import com.example.ntfctns.network.GetLinks;
import com.example.ntfctns.popups.InputPopup;
import com.example.ntfctns.popups.LoadPopup;
import com.example.ntfctns.utils.Saving;
import com.example.ntfctns.utils.WordFuncs;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements SummaryAd.OnKeywordClick {
    private FragmentFirstBinding bnd;
    private RecyclerView sumRv;
    private ArticleAd artAd;
    private List<Article> articles = null;
    private List<Keyword> summary;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        bnd = FragmentFirstBinding.inflate(inflater, container, false);
        return bnd.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sumRv = bnd.summaryRv;
        RecyclerView artRv = bnd.articleRv;
        artAd = new ArticleAd();
        SummaryAd sumAd = new SummaryAd(this);
        if (!new Saving().loadArticles(requireContext()).isEmpty()) {
            articles = new Saving().loadArticles(requireContext());
            summary = new Saving().loadKeywords(requireContext());
        }
        if (articles != null) {
            artRv.setLayoutManager(new LinearLayoutManager(requireContext()));
            artAd.setArticles(articles, requireContext());
            artRv.setAdapter(artAd);
            FlexboxLayoutManager layM = new FlexboxLayoutManager(requireContext());
            layM.setJustifyContent(JustifyContent.FLEX_START);
            sumRv.setLayoutManager(layM);
            sumAd.setKeywords(summary);
            sumRv.setAdapter(sumAd);
            new Saving().clearPrefs(requireContext(), Cons.ART_KEY);
            new Saving().clearPrefs(requireContext(), Cons.SUMMARY_KEY);
        }

        bnd.makeSchChg.setOnClickListener(v -> new InputPopup().inputPopup(requireContext()));
        bnd.enterWord.setOnKeyListener((v, keyCode, event) -> {
            btnClick(keyCode, event, artRv, artAd, sumAd);
            return false;
        });
        bnd.hours.setOnKeyListener((v, keyCode, event) -> {
            btnClick(keyCode, event, artRv, artAd, sumAd);
            return false;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bnd = null;
    }

    private void btnClick(int keyCode, KeyEvent event, RecyclerView rv, ArticleAd artAd, SummaryAd sumAd) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (bnd.enterWord.getText().length() != 0) {
                String text = bnd.enterWord.getText().toString();
                List<Keyword> words = WordFuncs.handlePunctuation(text, requireContext());
                closeKeyboard(requireView());
                String hoursStr = bnd.hours.getText().toString();
                int hours = 0;
                if (!hoursStr.isEmpty()) {
                    int hoursInt = Integer.parseInt(hoursStr);
                    if (hoursInt > 0 && hoursInt <= 48) {
                        hours = hoursInt;
                    }
                }
                PopupWindow window = new LoadPopup().loadPopup(getView(), requireContext());
                new GetLinks().getArticles(words, rv, artAd, sumAd, requireContext(), sumRv, hours, window);
            } else Toast.makeText(requireContext(), "Enter a word", Toast.LENGTH_LONG).show();
        }
    }

    private void closeKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onKeywordClick(@NonNull Keyword keyword) {
        List<Article> newArtList = new ArrayList<>();
        if (keyword.key.equals(Cons.ALL)) {
            newArtList = articles;
        } else {
            for (Article article : articles) {
                if (article.keywords.contains(keyword.getKey())) {
                    newArtList.add(article);
                }
            }
        }
        artAd.setArticles(newArtList, requireContext());
        artAd.notifyDataSetChanged();
    }
}