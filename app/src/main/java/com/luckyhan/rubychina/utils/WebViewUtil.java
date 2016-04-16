package com.luckyhan.rubychina.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebViewDatabase;

public class WebViewUtil {

    public static void flushWebView(Context context) {
        clearCookies(context);
        clearData(context);
    }

    public static void clearCookies(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
            CookieSyncManager.getInstance().startSync();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }

    public static void clearData(Context context) {
        WebViewDatabase.getInstance(context).clearFormData();
        WebViewDatabase.getInstance(context).clearUsernamePassword();
        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword();
    }

}
