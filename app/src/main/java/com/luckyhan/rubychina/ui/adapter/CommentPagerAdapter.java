package com.luckyhan.rubychina.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.luckyhan.rubychina.model.Reply;
import com.luckyhan.rubychina.ui.fragment.CommentDetailFragment;

import java.util.List;

public class CommentPagerAdapter extends FragmentPagerAdapter {

    private List<Reply> mReplyList;

    public CommentPagerAdapter(FragmentManager fm, List<Reply> replyList) {
        super(fm);
        mReplyList = replyList;
    }

    @Override
    public Fragment getItem(int position) {
        Reply reply = mReplyList.get(position);
        return CommentDetailFragment.newInstance(position, reply);
    }

    @Override
    public int getCount() {
        return mReplyList != null ? mReplyList.size() : 0;
    }

}
