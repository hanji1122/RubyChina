package com.luckyhan.rubychina.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;

public abstract class BaseRecycleHeaderFooterAdapter<T> extends ItemBaseRecycleAdapter<T> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_FOOT = 2;

    public static final int STATE_WAITING = -1;
    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_END = 2;

    private int mCurrentState = STATE_WAITING;

    private LoadViewHolder mLoadViewHolder;
    private LoadMoreListener mLoadMoreListener;

    private RecyclerView mRecyclerView;

    public RecyclerView getRecycleView() {
        return mRecyclerView;
    }

    private boolean isNeedHeader = false;

    private boolean isNeedHeader() {
        return isNeedHeader;
    }

    public void setNeedHeader() {
        isNeedHeader = true;
    }

    public BaseRecycleHeaderFooterAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        mRecyclerView = recyclerView;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return onCreateHeaderViewHolder(parent);

            case VIEW_TYPE_ITEM:
                return onCreateBasicViewHolder(parent);

            case VIEW_TYPE_FOOT:
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
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                onBindHeaderViewHolder(holder, position);
                break;

            case VIEW_TYPE_FOOT:
                toggleLoadMoreState(mCurrentState);
                break;

            case VIEW_TYPE_ITEM:
                if (isNeedHeader()) {
                    onBindBasicViewHolder(holder, position - 1);
                } else {
                    onBindBasicViewHolder(holder, position);
                }
                break;
        }
    }

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

    public abstract void onBindBasicViewHolder(RecyclerView.ViewHolder holder, int position);

    public abstract RecyclerView.ViewHolder onCreateBasicViewHolder(ViewGroup parent);

    private class LoadViewHolder extends RecyclerView.ViewHolder {

        View layout;
        View loadProgress;
        TextView loadText;

        public LoadViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.footer_container);
            loadProgress = itemView.findViewById(R.id.footer_progressbar);
            loadText = (TextView) itemView.findViewById(R.id.footer_text);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null && !mList.isEmpty()) {
            if (isNeedHeader()) {
                return mList.size() + 2;
            } else {
                return mList.size() + 1;
            }
        } else {
            if (isNeedHeader()) {
                return 1;
            }
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isNeedHeader() && position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            if (position == getItemCount() - 1) {
                return VIEW_TYPE_FOOT;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }
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

    private void loadMore() {
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
