package com.luckyhan.rubychina.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyhan.rubychina.R;

public class FooterView extends LinearLayout {

    private View mContainer;
    private View mProgressBar;
    private TextView mFooterTipView;

    private Context mContext;

    public FooterView(Context context) {
        super(context);
        init(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_recycle_footer, this);
        mContainer = findViewById(R.id.footer_container);
        mProgressBar = findViewById(R.id.footer_progressbar);
        mFooterTipView = (TextView) findViewById(R.id.footer_text);
    }

    public void hideFooterView() {
        mContainer.setVisibility(GONE);
    }

    public void showIdleView() {
        showLoadingView();
    }

    public void showLoadingView() {
        mContainer.setVisibility(VISIBLE);
        mProgressBar.setVisibility(VISIBLE);
        mFooterTipView.setVisibility(GONE);
    }

    public void showNoMoreView(String msg) {
        mContainer.setVisibility(VISIBLE);
        mFooterTipView.setVisibility(VISIBLE);
        if (msg != null) {
            mFooterTipView.setText(msg);
        } else {
            mFooterTipView.setText(mContext.getString(R.string.footer_no_more));
        }
        mProgressBar.setVisibility(GONE);
    }

    public void showRetryView() {
        mContainer.setVisibility(VISIBLE);
        mFooterTipView.setVisibility(VISIBLE);
        mFooterTipView.setText(mContext.getString(R.string.footer_retry));
        mProgressBar.setVisibility(GONE);
    }

}
