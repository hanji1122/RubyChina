package com.luckyhan.rubychina.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

    private static Toast mToast;

    public static void showShort(Context context, @StringRes int resId) {
        showShort(context, context.getString(resId));
    }

    public static void showShort(Context context, CharSequence message) {
        if (mToast != null) mToast.cancel();
        if (!TextUtils.isEmpty(message)) {
            mToast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public static void showLong(Context context, @StringRes int resId) {
        showLong(context, context.getString(resId));
    }

    public static void showLong(Context context, CharSequence message) {
        if (mToast != null) mToast.cancel();
        if (!TextUtils.isEmpty(message)) {
            mToast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
            mToast.show();
        }
    }

}
