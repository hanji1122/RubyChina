package com.luckyhan.rubychina.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.UserRequest;
import com.luckyhan.rubychina.model.Page;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.response.ActiveUsersResponse;
import com.luckyhan.rubychina.ui.activity.SelectAtUserActivity;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.ui.adapter.user.UserCenterUserListRecyclerAdapter;
import com.luckyhan.rubychina.ui.fragment.base.RecyclerLoadingBaseFragment;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ToastUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ActiveUserFragment extends RecyclerLoadingBaseFragment<UserCenterUserListRecyclerAdapter> {

    private boolean mIsSelectAtUser;

    public static ActiveUserFragment newInstance(boolean isSelectAtUser) {
        Bundle args = new Bundle();
        args.putBoolean("isSelectAtUser", isSelectAtUser);
        ActiveUserFragment fragment = new ActiveUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("isSelectAtUser")) {
            mIsSelectAtUser = getArguments().getBoolean("isSelectAtUser");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(AppUtils.getAppItemDecoration(getContext()));
        }
        mPage = new Page(Page.LIMIT);

        if (!mIsSelectAtUser) {
            setTitle(R.string.title_active_user);
            setHasOptionsMenu(true);
        }
        initToolBarClick(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsSelectAtUser) {
            mAdapter.setOnItemClickedListener(new UserCenterUserListRecyclerAdapter.OnItemClickedListener() {
                @Override
                public void onClick(User user) {
                    ((SelectAtUserActivity) getActivity()).onUserSelected(user);
                }
            });
            setUpToolBarOverlay(R.id.toolbar, R.id.scroll_parent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mIsSelectAtUser) {
            menu.clear();
            inflater.inflate(R.menu.menu_notification, menu);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopRefresh(mSwipeRefreshLayout);
    }

    @Override
    public void sendRequest() {
        if (!mPage.isTop(Page.LIMIT)) {
            mAdapter.toggleLoadMoreState(BaseRecycleLoadMoreAdapter.STATE_LOADING);
        }
        new UserRequest().getActiveUserList(mPage.current(), new Callback<ActiveUsersResponse>() {
            @Override
            public void onResponse(Response<ActiveUsersResponse> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    return;
                }
                List<User> users = response.body().users;
                mAdapter.setList(users);

                if (mPage.isTop(Page.LIMIT)) {
                    toggleEmptyView(users);
                }
                if (users.size() == 100) {
                    mAdapter.toggleLoadMoreState(BaseRecycleLoadMoreAdapter.STATE_END);
                } else {
                    mAdapter.toggleLoadMoreState(BaseRecycleLoadMoreAdapter.STATE_WAITING);
                }
                mPage.setLastSuccess();
                stopRefresh(mSwipeRefreshLayout);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
                stopRefresh(mSwipeRefreshLayout);
                if (mPage.isTop(Page.LIMIT)) {
                    mLoadingView.showRetryView();
                }
                mPage.recover();
                mAdapter.setError();
            }
        });
    }

    @Override
    public void onRetry() {
        mPage.refresh(Page.LIMIT);
        mAdapter.clearList();
        sendRequest();
    }

    @Override
    public void onRefresh() {
        mPage.refresh(Page.LIMIT);
        sendRequest();
    }

    @Override
    public UserCenterUserListRecyclerAdapter getAdapter(RecyclerView recyclerView) {
        return new UserCenterUserListRecyclerAdapter(getContext(), recyclerView);
    }

}
