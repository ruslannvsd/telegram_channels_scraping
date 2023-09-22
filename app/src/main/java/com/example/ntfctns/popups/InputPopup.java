package com.example.ntfctns.popups;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.core.widget.PopupWindowCompat;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.ntfctns.R;
import com.example.ntfctns.alarm.NtcWorker;
import com.example.ntfctns.consts.Cons;
import com.example.ntfctns.databinding.InputPopupBinding;
import com.example.ntfctns.utils.Hours;
import com.example.ntfctns.utils.Saving;
import com.example.ntfctns.utils.WordFuncs;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InputPopup {
    InputPopupBinding bnd;
    Context ctx;
    PopupWindow window;
    public void inputPopup(Context ctx) {
        this.ctx = ctx;
        LayoutInflater inflater =
                (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.input_popup, new LinearLayout(ctx), false);
        bnd = InputPopupBinding.bind(popupView);
        window = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        window.setElevation(2f);
        window.showAtLocation(popupView, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
        PopupWindowCompat.setWindowLayoutType(
                window,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        );

        Toast.makeText(ctx, "Enter 0 hours to cancel", Toast.LENGTH_SHORT).show();
        List<String> wordsList = new Saving().loadWords(ctx);
        String hours = new Saving().loadText(ctx, Cons.HOURS_KEY);
        if (wordsList != null) {
            String wordsAsString = TextUtils.join(" ", wordsList);
            bnd.enterWords.setText(wordsAsString);
        }
        if (hours.length() != 0) {
            bnd.period.setText(hours);
        }
        // bnd.enterWords.setOnKeyListener((v, keyCode, event) -> {
            // btnClick(keyCode, event);
            // return false;
        // });
        bnd.period.setOnKeyListener((v, keyCode, event) -> {
            btnClick(keyCode, event);
            return false;
        });
    }

    private void btnClick(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            String words = bnd.enterWords.getText().toString();
            String newHours = bnd.period.getText().toString();
            List<String> newWordList = WordFuncs.handlePunctuation(words, ctx);
            new Saving().saveWords(ctx, newWordList);
            new Saving().saveText(ctx, newHours, Cons.HOURS_KEY);
            int hours = new Hours().getHours(ctx);
            WorkManager.getInstance(ctx).cancelUniqueWork("PeriodicWork");
            if (hours != 0) {
                schedule(hours);
            }
            window.dismiss();
        }
    }

    public void schedule(int period) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest newRequest = new PeriodicWorkRequest.Builder(NtcWorker.class, period, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
                "PeriodicWork",
                ExistingPeriodicWorkPolicy.KEEP,
                newRequest
        );
    }
}