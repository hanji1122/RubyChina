package com.luckyhan.rubychina.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ClipboardUtil;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseSwipeActivity {

    public static final String EXTRA_URL = "url";

    @Bind(R.id.webview_pro) ProgressBar mProgressBar;
    @Bind(R.id.webview) WebView mWebView;

    private ObjectAnimator mObjectAnimator;

    public static void newInstance(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initToolBar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
        mWebView.setWebViewClient(new WebClient(getContext()));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChrome());
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.getSettings().setDisplayZoomControls(false);
        }
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        initData();
    }

    private void initData() {
        String url = getIntent().getStringExtra(EXTRA_URL);
        if (url != null) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {
                mWebView.reload();
                return true;
            }
            case R.id.action_copy_link: {
                ClipboardUtil.copyToClipboard(getContext(), mWebView.getUrl());
                ToastUtils.showShort(getContext(), R.string.copied_to_clipboard);
                return true;
            }
            case R.id.action_open_browser: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    private class WebClient extends WebViewClient {

        private Context context;

        public WebClient(Context context) {
            this.context = context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:")) {
                String address = url.split("mailto:")[1];
                AppUtils.openEmail(context, address);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class WebChrome extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress != 100) {
                if (mProgressBar.getVisibility() == View.GONE) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                int lastProgress = mProgressBar.getProgress();
                int shenme = 5 * (newProgress - lastProgress);
                if (shenme > 0) {
                    if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
                        mObjectAnimator.end();
                    }
                    mObjectAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", lastProgress, newProgress);
                    mObjectAnimator.setDuration(shenme);
                    mObjectAnimator.start();
                }

            } else {

                int lastProgress = mProgressBar.getProgress();
                if (lastProgress == newProgress) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
                        mObjectAnimator.end();
                    }
                    mObjectAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", lastProgress, 100);
                    mObjectAnimator.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            mProgressBar.setProgress(0);
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mProgressBar.setProgress(0);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });

                    mObjectAnimator.setDuration(300L);
                    mObjectAnimator.start();
                }

            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
