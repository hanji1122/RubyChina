package com.luckyhan.rubychina.ui.adapter.user;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.fragment.user.UserCenterFavoritesFragment;
import com.luckyhan.rubychina.ui.fragment.user.UserCenterFollowersFragment;
import com.luckyhan.rubychina.ui.fragment.user.UserCenterFollowingFragment;
import com.luckyhan.rubychina.ui.fragment.user.UserCenterPostedFragment;

public class UserCenterTabsAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private User mUser;

    public UserCenterTabsAdapter(Context context, FragmentManager fm, User user) {
        super(fm);
        mContext = context;
        mUser = user;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                String title = mContext.getString(R.string.user_tab_title_created);
                if (!TextUtils.isEmpty(mUser.topics_count)) {
                    title += mContext.getString(R.string.user_tab_count_format, mUser.topics_count);
                }
                return title;
            }

            case 1: {
                String title = mContext.getString(R.string.user_tab_title_fav);
                if (!TextUtils.isEmpty(mUser.favorites_count)) {
                    title += mContext.getString(R.string.user_tab_count_format, mUser.favorites_count);
                }
                return title;
            }

            case 2: {
                String title = mContext.getString(R.string.user_tab_title_following);
                if (!TextUtils.isEmpty(mUser.following_count)) {
                    title += mContext.getString(R.string.user_tab_count_format, mUser.following_count);
                }
                return title;
            }
            case 3: {
                String title = mContext.getString(R.string.user_tab_title_followers);
                if (!TextUtils.isEmpty(mUser.followers_count)) {
                    title += mContext.getString(R.string.user_tab_count_format, mUser.followers_count);
                }
                return title;
            }
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserCenterPostedFragment.newInstance(mUser);

            case 1:
                return UserCenterFavoritesFragment.newInstance(mUser);

            case 2:
                return UserCenterFollowingFragment.newInstance(mUser);

            case 3:
                return UserCenterFollowersFragment.newInstance(mUser);
        }
        return null;
    }

}
