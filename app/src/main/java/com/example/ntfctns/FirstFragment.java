package com.example.ntfctns;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ntfctns.adap.ArticleAd;
import com.example.ntfctns.databinding.FragmentFirstBinding;
import com.example.ntfctns.network.GetLinks;
import com.example.ntfctns.utils.WordFuncs;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding bnd;

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
        RecyclerView rv = bnd.articleRv;
        ArticleAd artAd = new ArticleAd();

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
                Log.i("Text", text);
                List<String> words = WordFuncs.handlePunctuation(text, requireContext());
                if (words != null) {
                    Log.i("Words", words.toString());
                    String hoursStr = bnd.hours.getText().toString();
                    int hours = 0;
                    if (!hoursStr.isEmpty()) {
                        int hoursInt = Integer.parseInt(hoursStr);
                        if (hoursInt > 0 && hoursInt <= 48) {
                            hours = hoursInt;
                        }
                    }
                    new GetLinks().getLinks(words, rv, artAd, requireContext(), bnd.showQuantity, hours);
                }
            } else Toast.makeText(requireContext(), "Enter a word", Toast.LENGTH_LONG).show();
        }
    }
}