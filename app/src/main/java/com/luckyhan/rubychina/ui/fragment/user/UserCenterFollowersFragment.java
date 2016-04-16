package com.luckyhan.rubychina.ui.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.UserRequest;
import com.luckyhan.rubychina.model.Page;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.model.response.FollowersArrayResponse;
import com.luckyhan.rubychina.ui.activity.SelectAtUserActivity;
import com.luckyhan.rubychina.ui.adapter.UserBaseFragment;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.ui.adapter.user.UserCenterUserListRecyclerAdapter;
import com.luckyhan.rubychina.utils.ToastUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserCenterFollowersFragment extends UserBaseFragment<UserCenterUserListRecyclerAdapter> {

    private boolean mIsSelectAtUser;

    public static UserCenterFollowersFragment newInstance(User user) {
        return newInstance(user, false);
    }

    public static UserCenterFollowersFragment newInstance(User user, boolean isSelectAtUser) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_USER, user);
        args.putBoolean("isSelectAtUser", isSelectAtUser);
        UserCenterFollowersFragment fragment = new UserCenterFollowersFragment();
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
    public void sendRequest() {
        new UserRequest().getFollowers(mUser.login, mPage.current(), Page.LIMIT, new Callback<FollowersArrayResponse>() {
            @Override
            public void onResponse(Response<FollowersArrayResponse> response, Retrofit retrofit) {
                stopRefresh(mSwipeRefreshLayout);
                if (response.isSuccess()) {
                    List<User> items = response.body().followers;
                    if (items != null) {
                        mLoadingView.setVisibility(View.GONE);
                        if (mPage.isTop()) {
                            mAdapter.setList(items);
                            toggleEmptyView(items);
                        } else {
                            mAdapter.addList(items);
                        }
                        if (items.size() < Page.LIMIT) {
                            mAdapter.toggleLoadMoreState(BaseRecycleLoadMoreAdapter.STATE_END);
                        } else {
                            mAdapter.toggleLoadMoreState(BaseRecycleLoadMoreAdapter.STATE_WAITING);
                        }
                        mPage.setLastSuccess();

                    }
                } else {
                    ErrorResponse errorResponse = ErrorUtils.parseError(getContext(), response, retrofit);
                    ToastUtils.showShort(getContext(), errorResponse.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
                stopRefresh(mSwipeRefreshLayout);
                if (mPage.isTop()) {
                    mLoadingView.showRetryView();
                }
                mPage.recover();
                mAdapter.setError();
            }
        });
    }

    @Override
    public UserCenterUserListRecyclerAdapter getAdapter(RecyclerView recyclerView) {
        return new UserCenterUserListRecyclerAdapter(getContext(), recyclerView);
    }

}
