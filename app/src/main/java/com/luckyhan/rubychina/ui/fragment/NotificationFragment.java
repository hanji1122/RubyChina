package com.luckyhan.rubychina.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.NotificationRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.Notification;
import com.luckyhan.rubychina.model.Page;
import com.luckyhan.rubychina.model.response.ActionResponse;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.model.response.NotificationResponse;
import com.luckyhan.rubychina.ui.adapter.NotificationAdapter;
import com.luckyhan.rubychina.ui.fragment.base.RecyclerLoadingBaseFragment;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ToastUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NotificationFragment extends RecyclerLoadingBaseFragment<NotificationAdapter> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(AppUtils.getAppItemDecoration(getContext()));
        }
        setHasOptionsMenu(true);
        initToolBarClick(R.id.toolbar);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mAdapter.getItemCount() != 0 && menu.findItem(R.id.action_clear) == null) {
            inflater.inflate(R.menu.menu_clear, menu);
        } else {
            menu.clear();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            sendClearRequest();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendRequest() {
        String access_token = DataUtils.getUserToken().access_token;
        new NotificationRequest().getNotifications(mPage.current(), Page.LIMIT, access_token, new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Response<NotificationResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Notification> items = response.body().notifications;
                    if (items != null) {
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
                        supportInvalidateOptionsMenu();
                    }
                    stopRefresh(mSwipeRefreshLayout);

                } else {
                    ErrorResponse errorResponse = ErrorUtils.parseError(getContext(), response, retrofit);
                    ToastUtils.showShort(getContext(), errorResponse.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                stopRefresh(mSwipeRefreshLayout);
                t.printStackTrace();
            }
        });
    }

    private void supportInvalidateOptionsMenu() {
        if (getActivity() != null) {
            getActivity().supportInvalidateOptionsMenu();
        }
    }

    private void sendClearRequest() {
        new NotificationRequest().clearNotification(DataUtils.getUserToken().access_token, new Callback<ActionResponse>() {
            @Override
            public void onResponse(Response<ActionResponse> response, Retrofit retrofit) {
                ActionResponse result = response.body();
                if (result != null && result.ok == 1) {
                    mAdapter.clearList();
                    mLoadingView.showEmptyView();
                }
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
            }
        });
    }

    @Override
    public NotificationAdapter getAdapter(RecyclerView recyclerView) {
        return new NotificationAdapter(getContext(), recyclerView);
    }

}
