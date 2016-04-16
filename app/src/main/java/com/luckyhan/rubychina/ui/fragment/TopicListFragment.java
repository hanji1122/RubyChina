package com.luckyhan.rubychina.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.TopicRequest;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Page;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.model.response.TopicListResponse;
import com.luckyhan.rubychina.ui.adapter.TopicRecycleAdapter;
import com.luckyhan.rubychina.ui.fragment.base.RecyclerLoadingBaseFragment;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.SharedPrefUtil;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.luckyhan.rubychina.widget.ContentLoadingView;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TopicListFragment extends RecyclerLoadingBaseFragment<TopicRecycleAdapter> implements ContentLoadingView.RetryListener {

    public static final String EXTRA_NODE_ID = "node_id";
    public static final String EXTRA_HIDE_NODE = "hide_node";
    public static final String EXTRA_ENABLE_OVERLAY = "overlay";

    private String nodeId;
    private boolean mHideNode;
    private boolean mEnableOverlay;

    public static TopicListFragment newInstance(String nodeId, boolean hideNode, boolean enableOverlay) {
        TopicListFragment f = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_NODE_ID, nodeId);
        bundle.putBoolean(EXTRA_HIDE_NODE, hideNode);
        bundle.putBoolean(EXTRA_ENABLE_OVERLAY, enableOverlay);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nodeId = getArguments().getString(EXTRA_NODE_ID);
        mHideNode = getArguments().getBoolean(EXTRA_HIDE_NODE);
        mEnableOverlay = getArguments().getBoolean(EXTRA_ENABLE_OVERLAY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(AppUtils.getAppItemDecoration(getContext()));
        }
        initToolBarClick(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mEnableOverlay) {
            setUpToolBarOverlay(R.id.toolbar, R.id.fragment_container);
        }
    }

    private String getSorting() {
        int sorting = SharedPrefUtil.getInt(getContext(), Constants.Pref.SORTING, 0);
        return getResources().getStringArray(R.array.sorting)[sorting];
    }

    @Override
    public void sendRequest() {
        new TopicRequest().getTopics(getSorting(), nodeId, mPage.current(), Page.LIMIT, new Callback<TopicListResponse>() {
            @Override
            public void onResponse(Response<TopicListResponse> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    return;
                }
                mLoadingView.setVisibility(View.GONE);
                List<Topic> items = response.body().topics;
                if (mPage.isTop()) {
                    mAdapter.setList(items);
                    toggleEmptyView(items);
                } else {
                    mAdapter.addList(items);
                }
                if (items.size() < Page.LIMIT) {
                    mAdapter.setEnd();
                } else {
                    mAdapter.setWaiting();
                }
                mPage.setLastSuccess();
                stopRefresh(mSwipeRefreshLayout);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
                if (mPage.isTop()) {
                    mLoadingView.showRetryView();
                }
                stopRefresh(mSwipeRefreshLayout);
                mPage.recover();
                mAdapter.setError();
            }
        });
    }

    @Override
    public TopicRecycleAdapter getAdapter(RecyclerView recyclerView) {
        TopicRecycleAdapter adapter = new TopicRecycleAdapter(getContext(), recyclerView);
        if (mHideNode) {
            adapter.hideNode();
        }
        return adapter;
    }

}
