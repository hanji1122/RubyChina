package com.luckyhan.rubychina.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.ui.adapter.base.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    private FooterView footerView;
    private int mCurrentScrollState;
    private boolean mIsLoadingMore;
    private int mCurrentState = STATE_WAITING;
    public static final int STATE_WAITING = -1;
    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_END = 2;

    private LoadMoreListener mFooterViewListener;

    public void setOnLoadMoreListener(LoadMoreListener listener) {
        mFooterViewListener = listener;
    }

    public LoadMoreListView(Context context) {
        super(context);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mIsLoadingMore = false;
        footerView = (FooterView) LayoutInflater.from(getContext()).inflate(R.layout.footer_view, this, false);
        footerView.showLoadingView();
        addFooterView(footerView);
        setOnScrollListener(this);
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentState) {
                    case STATE_END:
                        if (mFooterViewListener != null) {
                            mFooterViewListener.onClickEnd();
                        }
                        break;

                    case STATE_ERROR:
                        if (mFooterViewListener != null) {
                            mFooterViewListener.onClickError();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListeners != null) {
            for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                mScrollListeners.get(i).onScrollStateChanged(this, scrollState);
            }
        }
        mCurrentScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollListeners != null) {
            for (int i = mScrollListeners.size() - 1; i >= 0; i--) {
                mScrollListeners.get(i).onScroll(this, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
        if (mFooterViewListener != null && mCurrentState == STATE_WAITING) {
            if (visibleItemCount == totalItemCount) {
                return;
            }
            if (!mIsLoadingMore && (firstVisibleItem + getHeaderViewsCount() + visibleItemCount >= totalItemCount) && mCurrentScrollState != SCROLL_STATE_IDLE) {
                mIsLoadingMore = true;
                footerView.showLoadingView();
                if (mFooterViewListener != null) {
                    mFooterViewListener.onLoadMore();
                }
            }
        }
    }

    public void toggleLoadMoreState(int state) {
        toggleLoadMoreState(state, null);
    }

    public void toggleLoadMoreState(int state, String msg) {
        mIsLoadingMore = false;
        mCurrentState = state;
        switch (mCurrentState) {

            case STATE_WAITING:
                footerView.hideFooterView();
                break;

            case STATE_LOADING:
                footerView.showLoadingView();
                break;

            case STATE_ERROR:
                footerView.showRetryView();
                break;

            case STATE_END:
                footerView.showNoMoreView(msg);
                break;
        }
    }

    public void setWaiting() {
        toggleLoadMoreState(STATE_WAITING);
    }

    public void setError() {
        toggleLoadMoreState(STATE_ERROR);
    }

    public void setEnd() {
        toggleLoadMoreState(STATE_END);
    }

    public void setEnd(String msg) {
        toggleLoadMoreState(STATE_END, msg);
    }

    private List<OnScrollListener> mScrollListeners;

    public void addOnScrollListener(OnScrollListener listener) {
        if (mScrollListeners == null) {
            mScrollListeners = new ArrayList<>();
        }
        mScrollListeners.add(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        if (mScrollListeners != null) {
            mScrollListeners.remove(listener);
        }
    }

    public void clearOnScrollListeners() {
        if (mScrollListeners != null) {
            mScrollListeners.clear();
        }
    }

}
