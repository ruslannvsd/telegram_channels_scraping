package com.example.ntfctns.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.ntfctns.R;
import com.example.ntfctns.databinding.LoadPopupBinding;

public class Popup {
    @NonNull
    public PopupWindow showPopup(View anchorView, Context ctx) {
        LoadPopupBinding popupBinding = LoadPopupBinding.inflate(LayoutInflater.from(ctx), null, false);
        View popupView = popupBinding.getRoot();
        Glide.with(ctx).load(R.drawable.load).into(popupBinding.load);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
        popupWindow.showAtLocation(anchorView, Gravity.TOP, 0, 0);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        return popupWindow;
    }
}