package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.ui.fragment.TopicListFragment;

import java.util.List;

public class TopicTabsAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private List<Node> mSubNodes;

    public TopicTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.node_all);
        }
        return mSubNodes.get(position - 1).name;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return TopicListFragment.newInstance(null, false, true);
        }
        return TopicListFragment.newInstance(mSubNodes.get(position - 1).id, true, true);
    }

    @Override
    public int getCount() {
        return mSubNodes != null ? mSubNodes.size() + 1 : 1;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
//        super.restoreState(state, loader);
    }

    public void updateNodes(List<Node> nodes) {
        mSubNodes = nodes;
    }

}
