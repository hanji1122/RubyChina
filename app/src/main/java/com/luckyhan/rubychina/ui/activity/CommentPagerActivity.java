package com.luckyhan.rubychina.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.TopicRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.Reply;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.model.response.TopicResponse;
import com.luckyhan.rubychina.ui.adapter.CommentPagerAdapter;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CommentPagerActivity extends BaseSwipeActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_FORM_TOPIC_CONTENT = "from";

    @Bind(R.id.swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.pager) ViewPager mViewPager;
    @Bind(R.id.title) TextView mTitleView;

    private CommentPagerAdapter mAdapter;
    private int mCount;
    private Topic mTopic;
    private boolean mFromTopicContent;

    private SimpleOnPageChangeListener mSimpleOnPageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            setTitle(getString(R.string.title_comments, position + 1, mCount));
        }
    };

    public static void newInstance(Context context, Topic topic, int position, List<Reply> replyList, boolean isFromTopicContent) {
        Intent intent = new Intent(context, CommentPagerActivity.class);
        intent.putExtra(EXTRA_FORM_TOPIC_CONTENT, isFromTopicContent);
        intent.putExtra("position", position);
        intent.putExtra("topic", topic);
        intent.putParcelableArrayListExtra("replyList", (ArrayList<? extends Parcelable>) replyList);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_pager);
        ButterKnife.bind(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mTitleView.setOnClickListener(this);
        mTopic = getIntent().getParcelableExtra("topic");
        mFromTopicContent = getIntent().getBooleanExtra(EXTRA_FORM_TOPIC_CONTENT, false);
        if (TextUtils.isEmpty(mTopic.title)) {
            sendGetTopicRequest();
        } else {
            mTitleView.setText(mTopic.title);
            mSwipeRefreshLayout.setEnabled(false);
        }
        int position = getIntent().getIntExtra("position", 0);
        List<Reply> replyList = getIntent().getParcelableArrayListExtra("replyList");
        mCount = replyList != null ? replyList.size() : 0;
        initToolBar(getString(R.string.title_comments, position + 1, mCount));

        mAdapter = new CommentPagerAdapter(getSupportFragmentManager(), replyList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.addOnPageChangeListener(mSimpleOnPageChangeListener);
    }

    private void sendGetTopicRequest() {
        new TopicRequest().getTopicDetail(mTopic.id, DataUtils.getToken(), new Callback<TopicResponse>() {
            @Override
            public void onResponse(Response<TopicResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    mTopic = response.body().topic;
                    mTitleView.setText(mTopic.title);
                }
                stopRefresh(mSwipeRefreshLayout);
            }

            @Override
            public void onFailure(Throwable t) {
                stopRefresh(mSwipeRefreshLayout);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title: {
                if (mFromTopicContent) {
                    finish();
                } else {
                    TopicContentActivity.newInstance(getContext(), mTopic);
                }
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        sendGetTopicRequest();
    }

}
