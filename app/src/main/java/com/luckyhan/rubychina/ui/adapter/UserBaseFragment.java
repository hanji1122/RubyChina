package com.luckyhan.rubychina.ui.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.ui.fragment.base.RecyclerLoadingBaseFragment;
import com.luckyhan.rubychina.utils.AppUtils;

public abstract class UserBaseFragment<T extends BaseRecycleLoadMoreAdapter> extends RecyclerLoadingBaseFragment<T> {

    public static final String EXTRA_USER = "user";

    protected User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = getArguments().getParcelable(EXTRA_USER);
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

    public void toggleSwipeRefreshEnableState(int offset) {
        mSwipeRefreshLayout.setEnabled(offset == 0);
    }

}
