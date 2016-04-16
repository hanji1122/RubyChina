package com.luckyhan.rubychina.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.OAuthRequest;
import com.luckyhan.rubychina.api.request.UserRequest;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Token;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.response.UserResponse;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.utils.WebViewUtil;
import com.luckyhan.rubychina.widget.FixedWebView;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OAuthActivity extends BaseSwipeActivity {

    @Bind(R.id.webPage) FixedWebView mWebPageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        ButterKnife.bind(this);
        initToolBar(R.string.title_oauth);

        WebViewUtil.flushWebView(getContext());
        mWebPageView.loadUrl(OAuthRequest.getAuthorizeUrl());
        mWebPageView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //sign in and reload
                if (url.equals("https://www.ruby-china.org/")) {
                    mWebPageView.loadUrl(OAuthRequest.getAuthorizeUrl());
                }
                //get authorize code from the redirected url and transfer back to MainActivity
                if (url.contains("code=")) {
                    String code = url.split("=")[1];
                    requestToken(code);
                }
            }
        });
    }

    private void requestToken(String code) {
        new OAuthRequest().getToken(code, new Callback<Token>() {
            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    onErrorAction();
                    return;
                }
                Token token = response.body();
                Hawk.put(Constants.KEY_TOKEN, token);
                requestCurrentUser(token.access_token);
            }

            @Override
            public void onFailure(Throwable t) {
                CrashReport.postCatchedException(t);
                onErrorAction();
            }

        });
    }

    private void requestCurrentUser(String access_token) {
        new UserRequest().getLoginUser(access_token, new Callback<UserResponse>() {

            @Override
            public void onResponse(Response<UserResponse> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    onErrorAction();
                    return;
                }
                User user = response.body().user;
                Hawk.put(Constants.KEY_USER, user);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                CrashReport.postCatchedException(t);
                onErrorAction();
            }
        });
    }

    private void onErrorAction() {
        Hawk.remove(Constants.KEY_TOKEN);
        Hawk.remove(Constants.KEY_USER);
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewUtil.flushWebView(getContext());
    }
}
