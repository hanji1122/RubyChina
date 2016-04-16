package com.luckyhan.rubychina.utils;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;

public class ClipboardUtil {

    public static void copyToClipboard(Context context, String text) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copy", text);
            clipboard.setPrimaryClip(clip);
        }
    }

}
