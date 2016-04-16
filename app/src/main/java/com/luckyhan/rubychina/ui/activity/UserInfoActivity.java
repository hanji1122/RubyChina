package com.luckyhan.rubychina.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.UserInfoItem;
import com.luckyhan.rubychina.ui.adapter.user.UserInfoRecyclerAdapter;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ClipboardUtil;
import com.luckyhan.rubychina.utils.ThirdPartyUtils;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends BaseSwipeActivity implements UserInfoRecyclerAdapter.OnClickListener {

    public static final String EXTRA_USER = "user";
    private RecyclerView mRecyclerView;
    private UserInfoRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .color(ContextCompat.getColor(getContext(), R.color.list_divider))
                .sizeResId(R.dimen.list_divider)
                .marginResId(R.dimen.list_divider_margin, R.dimen.list_divider_margin)
                .build());
        mAdapter = new UserInfoRecyclerAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        prepareData();
        initToolBar(R.string.title_userinfo);
    }

    private void prepareData() {
        User user = getIntent().getParcelableExtra(EXTRA_USER);
        List<UserInfoItem> itemList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(user.login)) {
            sb.append(user.login);
        }
        if (!TextUtils.isEmpty(user.name)) {
            sb.append("(").append(user.name).append(")");
        }
        itemList.add(new UserInfoItem(0, getString(R.string.userinfo_nickname), sb.toString()));
        if (!TextUtils.isEmpty(user.id)) {
            itemList.add(new UserInfoItem(0, getString(R.string.userinfo_sequence), getString(R.string.register_id, user.id)));
        }
        if (!TextUtils.isEmpty(user.location)) {
            itemList.add(new UserInfoItem(1, getString(R.string.userinfo_location), user.location));
        }
        if (!TextUtils.isEmpty(user.company)) {
            itemList.add(new UserInfoItem(2, getString(R.string.userinfo_company), user.company));
        }
        if (!TextUtils.isEmpty(user.twitter)) {
            itemList.add(new UserInfoItem(3, getString(R.string.userinfo_twitter), user.twitter));
        }
        if (!TextUtils.isEmpty(user.website)) {
            itemList.add(new UserInfoItem(4, getString(R.string.userinfo_web), user.website));
        }
        if (!TextUtils.isEmpty(user.github)) {
            itemList.add(new UserInfoItem(5, getString(R.string.userinfo_github), user.github));
        }
        if (!TextUtils.isEmpty(user.email)) {
            itemList.add(new UserInfoItem(6, getString(R.string.userinfo_email), user.email));
        }
        mAdapter.setList(itemList);
    }

    @Override
    public void onClick(UserInfoItem item) {
        switch (item.type) {
            // Twitter
            case 3:
                ThirdPartyUtils.openTwitter(getContext(), item.detail);
                break;

            // Website
            case 4:
                AppUtils.openUrl(getContext(), item.detail);
                break;

            // Github
            case 5:
                ThirdPartyUtils.openGithub(getContext(), item.detail);
                break;

            // Email
            case 6:
                AppUtils.openEmail(getContext(), item.detail);
                break;
        }
    }

    @Override
    public void onLongClick(UserInfoItem item) {
        ClipboardUtil.copyToClipboard(getContext(), item.detail);
        ToastUtils.showShort(getContext(), getString(R.string.msg_copy_to_clipboard));
    }

}
