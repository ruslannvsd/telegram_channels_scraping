package com.example.ntfctns.popups;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.PopupWindowCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.ntfctns.R;
import com.example.ntfctns.adap.WordsAd;
import com.example.ntfctns.alarm.NtcWorker;
import com.example.ntfctns.databinding.InputPopupBinding;
import com.example.ntfctns.utils.Saving;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InputPopup {
    InputPopupBinding bnd;
    Context ctx;
    PopupWindow window;
    WordsAd wordsAd;
    List<String> wordsList;

    public void inputPopup(@NonNull Context ctx) {
        this.ctx = ctx;
        View popupView = LayoutInflater.from(ctx).inflate(R.layout.input_popup, new LinearLayout(ctx), false);
        bnd = InputPopupBinding.bind(popupView);
        setupPopupWindow(popupView);
        setupWordsRecyclerView();
        loadAndSetPeriod();
        bnd.period.setOnKeyListener(this::btnClick);
    }

    private void setupPopupWindow(View popupView) {
        window = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        window.setElevation(2f);
        window.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        PopupWindowCompat.setWindowLayoutType(window, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        Toast.makeText(ctx, "Enter 0 hours to cancel", Toast.LENGTH_SHORT).show();
    }

    private void setupWordsRecyclerView() {
        wordsList = new Saving().loadWords(ctx);
        wordsAd = new WordsAd(word -> {
            if (wordsList.contains(word)) {
                wordsList.remove(word);
                new Saving().saveWords(ctx, wordsList);
                if (ctx instanceof Activity) {
                    ((Activity) ctx).runOnUiThread(() -> {
                        wordsAd.setWords(wordsList, ctx);
                        wordsAd.notifyDataSetChanged();
                    });
                }
            }
        });
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(ctx);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        bnd.words.setLayoutManager(layoutManager);
        wordsAd.setWords(wordsList, ctx);
        bnd.words.setAdapter(wordsAd);
    }

    private void loadAndSetPeriod() {
        String hours = new Saving().loadText(ctx, "HOURS_KEY");
        if (!hours.isEmpty()) {
            bnd.period.setText(hours);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private boolean btnClick(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            String inputText = bnd.enterWords.getText().toString().trim();
            if (!inputText.isEmpty()) {
                String[] newWords = inputText.split("\\s+");
                boolean listModified = false;

                for (String newWord : newWords) {
                    if (!newWord.isEmpty() && !wordsList.contains(newWord)) {
                        wordsList.add(newWord);
                        listModified = true;
                    }
                }
                if (listModified) {
                    new Saving().saveWords(ctx, wordsList);
                    wordsAd.setWords(wordsList, ctx);
                    wordsAd.notifyDataSetChanged();
                }
            }

            schedulePeriodicWork();
            window.dismiss();
            return true;
        }
        return false;
    }


    private void schedulePeriodicWork() {
        String newHours = bnd.period.getText().toString();
        new Saving().saveText(ctx, newHours, "HOURS_KEY");
        int hours = Integer.parseInt(newHours);
        if (hours > 0) {
            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
            PeriodicWorkRequest newRequest = new PeriodicWorkRequest.Builder(NtcWorker.class, hours, TimeUnit.HOURS)
                    .setConstraints(constraints).build();
            WorkManager.getInstance(ctx).enqueueUniquePeriodicWork("PeriodicWork", ExistingPeriodicWorkPolicy.KEEP, newRequest);
        } else {
            WorkManager.getInstance(ctx).cancelUniqueWork("PeriodicWork");
        }
    }
}
