package com.luckyhan.rubychina.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseRecycleLoadMoreAdapter<T> extends ItemBaseRecycleAdapter<T> {

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_FOOT = 2;

    public static final int STATE_WAITING = -1;
    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_END = 2;

    private int mCurrentState = STATE_WAITING;

    private LoadViewHolder mLoadViewHolder;
    private LoadMoreListener mLoadMoreListener;

    public BaseRecycleLoadMoreAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mCurrentState == STATE_WAITING && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
                        int itemCount = llm.getItemCount();
                        int firstVisibleItemPosition = llm.findFirstVisibleItemPosition();
                        if (firstVisibleItemPosition + recyclerView.getChildCount() >= itemCount) {
                            if (firstVisibleItemPosition > 0) {
                                loadMore();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_FOOT) {
            toggleLoadMoreState(mCurrentState);
        } else {
            onBindBasicViewHolder(holder, position);
        }
    }

    public abstract void onBindBasicViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateBasicViewHolder(ViewGroup parent, int viewType);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // build load more ViewHolder
        if (viewType == VIEW_TYPE_FOOT) {
            if (mLoadViewHolder == null) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_recycle_footer, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurrentState == STATE_ERROR) {
                            clickError();
                        }
                        if (mCurrentState == STATE_END) {
                            clickEnd();
                        }
                    }
                });
                mLoadViewHolder = new LoadViewHolder(view);
            }
            return mLoadViewHolder;
        }
        // build basic item ViewHolder
        return onCreateBasicViewHolder(parent, viewType);
    }

    class LoadViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.footer_container) View layout;
        @Bind(R.id.footer_progressbar) View loadProgress;
        @Bind(R.id.footer_text) TextView loadText;

        public LoadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null && !mList.isEmpty()) {
            return mList.size() + 1;
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (position == mList.size()) {
            return 0;
        }
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mList.size()) {
            return VIEW_TYPE_FOOT;
        }
        return VIEW_TYPE_ITEM;
    }

    public void toggleLoadMoreState(int state) {
        mCurrentState = state;
        if (mLoadViewHolder == null) {
            return;
        }
        switch (state) {
            case STATE_WAITING:
                mLoadViewHolder.loadProgress.setVisibility(View.GONE);
                mLoadViewHolder.loadText.setVisibility(View.GONE);
                break;

            case STATE_LOADING:
                mLoadViewHolder.loadProgress.setVisibility(View.VISIBLE);
                mLoadViewHolder.loadText.setVisibility(View.GONE);
                break;

            case STATE_ERROR:
                mLoadViewHolder.loadProgress.setVisibility(View.GONE);
                mLoadViewHolder.loadText.setVisibility(View.VISIBLE);
                mLoadViewHolder.loadText.setText(mContext.getString(R.string.footer_retry));
                break;

            case STATE_END:
                mLoadViewHolder.loadProgress.setVisibility(View.GONE);
                mLoadViewHolder.loadText.setVisibility(View.VISIBLE);
                mLoadViewHolder.loadText.setText(mContext.getString(R.string.footer_no_more));
                break;
        }
    }

    public void setOnLoadMoreListener(LoadMoreListener listener) {
        mLoadMoreListener = listener;
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

    public void loadMore() {
        if (mLoadMoreListener != null) {
            toggleLoadMoreState(STATE_LOADING);
            mLoadMoreListener.onLoadMore();
        } else {
            throw new IllegalStateException("this adapter should set load more listener");
        }
    }

    private void clickError() {
        if (mLoadMoreListener != null) {
            toggleLoadMoreState(STATE_LOADING);
            mLoadMoreListener.onClickError();
        } else {
            throw new IllegalStateException("this adapter should set load more listener");
        }
    }

    private void clickEnd() {
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onClickEnd();
        } else {
            throw new IllegalStateException("this adapter should set load more listener");
        }
    }

}
