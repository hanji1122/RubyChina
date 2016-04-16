package com.luckyhan.rubychina.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.ui.activity.MainActivity;
import com.luckyhan.rubychina.ui.activity.SubscribeActivity;
import com.luckyhan.rubychina.ui.adapter.TopicTabsAdapter;
import com.luckyhan.rubychina.ui.base.BaseFragment;
import com.luckyhan.rubychina.utils.DeviceUtil;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TopicTabsFragment extends BaseFragment {

    private FrameLayout mParentContainer;
    @Bind(R.id.tab_viewpager) ViewPager mViewPager;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;

    private TopicTabsAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParentContainer = (FrameLayout) ((Activity) context).findViewById(R.id.fragment_container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_tabs, container, false);
        ButterKnife.bind(this, view);
        setTitle(R.string.app_name);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        setUpAdapter(getCachedSubscribeNodes());
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mParentContainer != null) {
            ((LinearLayout.LayoutParams) mParentContainer.getLayoutParams())
                    .setMargins(0, 0, 0, (int) DeviceUtil.dp2px(getContext(), -56F));
        }
    }

    @Override
    public void onDestroy() {
        if (mParentContainer != null) {
            ((LinearLayout.LayoutParams) mParentContainer.getLayoutParams()).setMargins(0, 0, 0, 0);
            ViewPropertyAnimator.animate(mParentContainer).translationY(0).setDuration(0).start();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_notification, menu);
        inflater.inflate(R.menu.menu_subscribe, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_nodes_save: {
                Intent intent = new Intent(getContext(), SubscribeActivity.class);
                getActivity().startActivityForResult(intent, MainActivity.REQUEST_CHANGE_NODES);
                return true;
            }

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Node> getCachedSubscribeNodes() {
        return Hawk.get(Constants.KEY_CACHE_SUBSCRIBE);
    }

    private void setUpAdapter(List<Node> nodes) {
        //set viewpager adapter
        mAdapter = new TopicTabsAdapter(getContext(), getChildFragmentManager());
        if (nodes != null) {
            mAdapter.updateNodes(nodes);
        }
        mViewPager.setAdapter(mAdapter);
        //bind the tabs to the viewpager
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void updateNodes(List<Node> nodes) {
        setUpAdapter(nodes);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
