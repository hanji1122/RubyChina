package com.luckyhan.rubychina.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Page;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.ui.adapter.base.LoadMoreListener;
import com.luckyhan.rubychina.ui.base.BaseFragment;
import com.luckyhan.rubychina.utils.CollectionUtils;
import com.luckyhan.rubychina.widget.ContentLoadingView;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.List;

public abstract class RecyclerLoadingBaseFragment<T extends BaseRecycleLoadMoreAdapter> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener, ContentLoadingView.RetryListener {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected ContentLoadingView mLoadingView;
    protected T mAdapter;
    protected Page mPage = new Page();

    private Toolbar mToolbar;
    private View mParentContainer;
    private boolean mIsShowToolbar = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usercenter_common, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mLoadingView = (ContentLoadingView) view.findViewById(R.id.content_loading);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = getAdapter(mRecyclerView);
        mAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mLoadingView.showLoading();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int y = recyclerView.computeVerticalScrollOffset();
                mSwipeRefreshLayout.setEnabled(y == 0);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mLoadingView.setRetryListener(this);
        return view;
    }

    protected void initToolBarClick(int toolbarId) {
        mToolbar = (Toolbar) getActivity().findViewById(toolbarId);
        if (mToolbar == null) {
            return;
        }
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEnd();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendRequest();
    }

    protected void setUpToolBarOverlay(int toolbarId, int parentId) {
        mToolbar = (Toolbar) getActivity().findViewById(toolbarId);
        mParentContainer = getActivity().findViewById(parentId);
        if (mToolbar == null || mParentContainer == null) {
            return;
        }
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                toggle(mIsShowToolbar);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 20) {
                    mIsShowToolbar = false;
                } else if (dy < -5) {
                    mIsShowToolbar = true;
                }
            }
        });
    }

    private void toggle(boolean isShowToolbar) {
        if (isShowToolbar) {
            ViewPropertyAnimator.animate(mParentContainer).translationY(0)
                    .setDuration(300L).setInterpolator(new DecelerateInterpolator()).start();
        } else {
            ViewPropertyAnimator.animate(mParentContainer).translationY(-mToolbar.getBottom())
                    .setDuration(300L).setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    public abstract void sendRequest();

    public abstract T getAdapter(RecyclerView recyclerView);

    @Override
    public void onLoadMore() {
        mPage.nextPage();
        sendRequest();
    }

    @Override
    public void onClickError() {
        mPage.nextPage();
        sendRequest();
    }

    @Override
    public void onClickEnd() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        mPage.refresh();
        sendRequest();
    }

    @Override
    public void onRetry() {
        mPage.refresh();
        mAdapter.clearList();
        sendRequest();
    }

    protected void toggleEmptyView(List items) {
        if (CollectionUtils.isEmpty(items)) {
            mLoadingView.showEmptyView();
        } else {
            mLoadingView.setVisibility(View.GONE);
        }
    }

}
