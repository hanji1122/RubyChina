package com.luckyhan.rubychina.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyhan.rubychina.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentLoadingView extends FrameLayout {

    @Bind(R.id.loading) View mLoadingView;
    @Bind(R.id.retry) TextView mRetryView;
    @Bind(R.id.empty) ImageView mEmptyView;

    private RetryListener mRetryListener;

    public ContentLoadingView(Context context) {
        super(context);
        init();
    }

    public ContentLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContentLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_content_loading, this);
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        ButterKnife.bind(this);
        mRetryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    showLoading();
                    mRetryListener.onRetry();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    public void showRetryView() {
        mLoadingView.setVisibility(GONE);
        mRetryView.setVisibility(VISIBLE);
        mEmptyView.setVisibility(GONE);
    }

    public void showLoading() {
        mRetryView.setVisibility(GONE);
        mLoadingView.setVisibility(VISIBLE);
        mEmptyView.setVisibility(GONE);
    }

    public void showEmptyView() {
        setVisibility(VISIBLE);
        mRetryView.setVisibility(GONE);
        mLoadingView.setVisibility(GONE);
        mEmptyView.setVisibility(VISIBLE);
    }

    public void setRetryListener(RetryListener listener) {
        mRetryListener = listener;
    }


    public interface RetryListener {
        void onRetry();
    }

}
