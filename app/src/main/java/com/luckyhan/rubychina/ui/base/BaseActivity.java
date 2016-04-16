package com.luckyhan.rubychina.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.ui.activity.MainActivity;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !(getContext() instanceof MainActivity)) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    protected void initToolBar() {
        initToolBar("");
    }

    protected void initToolBar(@StringRes int resId) {
        initToolBar(getString(resId));
    }

    protected void initToolBar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
    }

    protected void startRefresh(final SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    protected void stopRefresh(final SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_LOGIN && resultCode == RESULT_CANCELED) {
            ToastUtils.showLong(getContext(), R.string.msg_login_cancel);
        }
        if (requestCode == MainActivity.REQUEST_LOGIN && resultCode == RESULT_OK) {
            ToastUtils.showShort(getContext(), getString(R.string.msg_login_success));
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.Intent.LOGIN_SUCCESS));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPause(this);
    }
}
