package com.example.ntfctns.utils;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.core.widget.PopupWindowCompat;

import com.example.ntfctns.R;
import com.example.ntfctns.databinding.LoadPopupBinding;

public class Popup {
    private static Popup instance;
    private PopupWindow window;

    public static Popup getInstance() {
        if (instance == null) {
            instance = new Popup();
        }
        return instance;
    }

    public void showLoad(Context ctx) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.load_popup, new LinearLayout(ctx), false);
        LoadPopupBinding bnd = LoadPopupBinding.bind(popup);
        window = new PopupWindow(popup,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        window.setElevation(2f);
        View rootView = ((Activity) ctx).findViewById(android.R.id.content);
        window.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        PopupWindowCompat.setWindowLayoutType(window,
                FLAG_LAYOUT_IN_SCREEN);
        // Glide.with(ctx).load(R.drawable.load).into(bnd.load);
    }
    public void dismissPopup() {
        if (window != null && window.isShowing()) {
            window.dismiss();
        }
    }
}
