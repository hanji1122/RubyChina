package com.luckyhan.rubychina.ui.fragment.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.UserRequest;
import com.luckyhan.rubychina.model.Page;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.model.response.TopicArrayResponse;
import com.luckyhan.rubychina.ui.adapter.TopicRecycleAdapter;
import com.luckyhan.rubychina.ui.adapter.UserBaseFragment;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.utils.ToastUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserCenterPostedFragment extends UserBaseFragment<TopicRecycleAdapter> {

    public static UserCenterPostedFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_USER, user);
        UserCenterPostedFragment fragment = new UserCenterPostedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void sendRequest() {
        new UserRequest().getPostedTopics(mUser.login, mPage.current(), Page.LIMIT, new Callback<TopicArrayResponse>() {
            @Override
            public void onResponse(Response<TopicArrayResponse> response, Retrofit retrofit) {
                stopRefresh(mSwipeRefreshLayout);
                if (response.isSuccess()) {
                    List<Topic> items = response.body().topics;
                    if (items == null) return;
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
                } else {
                    ErrorResponse errorResponse = ErrorUtils.parseError(getContext(), response, retrofit);
                    ToastUtils.showShort(getContext(), errorResponse.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                stopRefresh(mSwipeRefreshLayout);
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
                if (mPage.isTop()) {
                    mLoadingView.showRetryView();
                }
                mPage.recover();
                mAdapter.setError();
            }
        });
    }

    @Override
    public TopicRecycleAdapter getAdapter(RecyclerView recyclerView) {
        return new TopicRecycleAdapter(getContext(), recyclerView);
    }


}
