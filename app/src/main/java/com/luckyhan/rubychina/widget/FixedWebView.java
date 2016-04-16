package com.luckyhan.rubychina.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class FixedWebView extends WebView {

    public FixedWebView(Context context) {
        super(context);
        setUp();
    }

    public FixedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public FixedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            freeMemory();
        }
        destroy();
    }

    private void setUp() {
        if (isInEditMode()) {
            return;
        }
        clearCache(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);

        if (!isInEditMode()) {
            WebSettings settings = getSettings();
            settings.setSavePassword(false);
            settings.setSupportZoom(false);
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setAppCacheEnabled(true);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                settings.setDisplayZoomControls(false);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                settings.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            }
        }
        fixWebViewJSInterface();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void fixWebViewJSInterface() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            removeJavascriptInterface("searchBoxJavaBridge_");
        }
    }

}
