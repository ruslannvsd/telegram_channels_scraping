package com.example.ntfctns;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.adap.ArticleAd;
import com.example.ntfctns.classes.Article;
import com.example.ntfctns.consts.Cons;
import com.example.ntfctns.databinding.FragmentFirstBinding;
import com.example.ntfctns.network.GetLinks;
import com.example.ntfctns.popups.InputPopup;
import com.example.ntfctns.popups.LoadPopup;
import com.example.ntfctns.utils.Saving;
import com.example.ntfctns.utils.WordFuncs;

import java.util.List;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding bnd;
    private TextView qtyTV;
    private List<Article> articles = null;
    private String summary;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        bnd = FragmentFirstBinding.inflate(inflater, container, false);
        summary = new Saving().loadText(requireContext(), Cons.SUMMARY_KEY);
        return bnd.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qtyTV = bnd.summary;
        RecyclerView rv = bnd.articleRv;
        ArticleAd artAd = new ArticleAd();
        if (!new Saving().loadArticles(requireContext()).isEmpty()) {
            articles = new Saving().loadArticles(requireContext());
        }
        if (articles != null) {
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));
            artAd.setArticles(articles, requireContext());
            rv.setAdapter(artAd);
            qtyTV.setText(summary);
            new Saving().clearPrefs(requireContext(), Cons.ART_KEY);
            new Saving().clearPrefs(requireContext(), Cons.SUMMARY_KEY);
        }

        bnd.makeSchChg.setOnClickListener(v -> new InputPopup().inputPopup(requireContext()));
        bnd.enterWord.setOnKeyListener((v, keyCode, event)-> {
            btnClick(keyCode, event, rv, artAd);
            return false;
        });
        bnd.hours.setOnKeyListener((v, keyCode, event)-> {
            btnClick(keyCode, event, rv, artAd);
            return false;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bnd = null;
    }
    private void btnClick(int keyCode, KeyEvent event, RecyclerView rv, ArticleAd artAd) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (bnd.enterWord.getText().length() != 0) {
                String text = bnd.enterWord.getText().toString();
                List<String> words = WordFuncs.handlePunctuation(text, requireContext());
                if (words != null) {
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
                    new GetLinks().getArticles(words, rv, artAd, requireContext(), qtyTV, hours, window);
                }
            } else Toast.makeText(requireContext(), "Enter a word", Toast.LENGTH_LONG).show();
        }
    }
    private void closeKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}