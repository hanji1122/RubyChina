package com.luckyhan.rubychina.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.adapter.SelectAtUserTabsAdapter;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.utils.DeviceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectAtUserActivity extends BaseSwipeActivity {

    public static final String EXTRA_USER = "user";

    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.tab_viewpager) ViewPager mViewPager;
    @Bind(R.id.scroll_parent) LinearLayout mParentLayout;
    private SelectAtUserTabsAdapter mAdapter;

    public static void newInstance(Context context, int requestCode) {
        Intent intent = new Intent(context, SelectAtUserActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_at_user);
        ButterKnife.bind(this);
        initToolBar(R.string.title_select_at_user);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        setUpTabAdapter();
        ((LinearLayout.LayoutParams) mParentLayout.getLayoutParams())
                .setMargins(0, 0, 0, (int) DeviceUtil.dp2px(getContext(), -56F));
    }

    private void setUpTabAdapter() {
        mAdapter = new SelectAtUserTabsAdapter(getContext(), getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(Integer.MAX_VALUE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void onUserSelected(User user) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER, user);
        setResult(RESULT_OK, intent);
        finish();
    }

}
