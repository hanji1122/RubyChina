package com.luckyhan.rubychina.ui.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.luckyhan.rubychina.R;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public abstract class BaseSwipeActivity extends BaseActivity {

    protected SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.swipeback_layout, null);
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.attachToActivity(this);
    }

    protected void swipeBackEnableToggle(boolean enable) {
        mSwipeBackLayout.setEnableGesture(enable);
    }

}
