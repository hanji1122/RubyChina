package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.fragment.ActiveUserFragment;
import com.luckyhan.rubychina.ui.fragment.user.UserCenterFollowersFragment;
import com.luckyhan.rubychina.ui.fragment.user.UserCenterFollowingFragment;

public class SelectAtUserTabsAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String[] mTitleArray;
    private User mUser;

    public SelectAtUserTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mTitleArray = context.getResources().getStringArray(R.array.select_at_user);
        mUser = DataUtils.getLoginUser();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleArray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserCenterFollowingFragment.newInstance(mUser, true);

            case 1:
                return UserCenterFollowersFragment.newInstance(mUser, true);

            case 2:
                return ActiveUserFragment.newInstance(true);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mTitleArray != null ? mTitleArray.length : 0;
    }

}
