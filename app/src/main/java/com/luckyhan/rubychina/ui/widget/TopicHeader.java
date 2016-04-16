package com.luckyhan.rubychina.ui.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.TopicRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.HtmlData;
import com.luckyhan.rubychina.model.Meta;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.model.response.LikeResponse;
import com.luckyhan.rubychina.model.response.TopicResponse;
import com.luckyhan.rubychina.ui.activity.TopicListActivity;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.HtmlUtil;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.luckyhan.rubychina.widget.FineWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TopicHeader extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.wv_content) FineWebView mFineWebView;
    @Bind(R.id.tv_title) TextView mTopicTitleTV;
    @Bind(R.id.tv_topic_desc) TextView mTopicDescTV;
    @Bind(R.id.tv_node_name) TextView mNodeNameTV;
    @Bind(R.id.tv_reply_count) TextView mReplyCountTV;
    @Bind(R.id.topic_like_action) LikeBounceView mLikeBounceView;

    private Context mContext;
    private Topic mTopic;
    private Meta mMeta;

    private OnUpdateListener mOnUpdateListener;
    private int mReplyTopY = 0;

    public void setOnUpdateListener(OnUpdateListener listener) {
        mOnUpdateListener = listener;
    }

    public interface OnUpdateListener {
        void onUpdateTopic(TopicResponse topicModel);

        void onTopicNotExist();

        void onUpdateFloatHeight(int topY);

        void onNetError();
    }

    public TopicHeader(Context context) {
        super(context);
        mContext = context;
    }

    public TopicHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        ButterKnife.bind(this, this);
    }

    public void setTopic(Topic topic) {
        mTopic = topic;
        requestTopicDetail();
    }

    private void initBasicHeader() {
        mTopicDescTV.setText(mContext.getString(R.string.formatter_read_count_time, mTopic.hits, mTopic.getCreatePrettyTime()));
        mTopicTitleTV.setText(mTopic.title);
        mNodeNameTV.setText(mTopic.node_name);
        mNodeNameTV.setOnClickListener(this);
        mLikeBounceView.setOnClickListener(this);
        mLikeBounceView.setLikeCountView(mTopic.likes_count);
        mReplyCountTV.setText(mContext.getString(R.string.topic_comment_count_formatter, mTopic.replies_count));
        mLikeBounceView.setChecked(mMeta.liked);
        mFineWebView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int replyTop = mReplyCountTV.getTop();
            if (replyTop > mReplyTopY) {
                mReplyTopY = replyTop;
                mOnUpdateListener.onUpdateFloatHeight(mReplyTopY);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mFineWebView.getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
        } else {
            mFineWebView.getViewTreeObserver().removeGlobalOnLayoutListener(mGlobalLayoutListener);
        }
        super.onDetachedFromWindow();
    }

    public void requestTopicDetail() {
        String token = null;
        if (DataUtils.isUserLogin()) {
            token = DataUtils.getUserToken().access_token;
        }
        new TopicRequest().getTopicDetail(mTopic.id, token, new Callback<TopicResponse>() {
            @Override
            public void onResponse(Response<TopicResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    TopicResponse topicModel = response.body();
                    mTopic = topicModel.topic;
                    mMeta = topicModel.meta;
                    initBasicHeader();
                    if (mOnUpdateListener != null) {
                        mOnUpdateListener.onUpdateTopic(topicModel);
                    }
                    prepareWebViewData();
                } else {
                    if (mOnUpdateListener != null) {
                        mOnUpdateListener.onTopicNotExist();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
                if (mOnUpdateListener != null) {
                    mOnUpdateListener.onNetError();
                }
            }
        });
    }

    private void prepareWebViewData() {
        new AsyncTask<String, Void, HtmlData>() {

            @Override
            protected HtmlData doInBackground(String... params) {
                return HtmlUtil.prepareHtmlData(mContext, params[0]);
            }

            @Override
            protected void onPostExecute(final HtmlData webData) {
                super.onPostExecute(webData);
                mFineWebView.setData(webData);
            }
        }.execute(mTopic.body_html);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topic_like_action: {
                if (AppUtils.jumpLogin(getContext())) {
                    return;
                }
                if (mMeta.liked) {
                    mMeta.liked = false;
                    mLikeBounceView.setLikeCountView(--mTopic.likes_count);
                    sendUnLikeTopicRequest();
                } else {
                    mMeta.liked = true;
                    mLikeBounceView.setLikeCountView(++mTopic.likes_count);
                    mLikeBounceView.setChecked(true, true);
                    sendLikeTopicRequest();
                }
                break;
            }

            case R.id.tv_node_name: {
                Node node = new Node();
                node.id = mTopic.node_id;
                node.name = mTopic.node_name;
                TopicListActivity.newInstance(getContext(), node);
                break;
            }

            default:
                break;
        }
    }

    private void sendLikeTopicRequest() {
        new TopicRequest().likeTopic(mTopic.id, DataUtils.getToken(), new Callback<LikeResponse>() {
            @Override
            public void onResponse(Response<LikeResponse> response, Retrofit retrofit) {
                LikeResponse likeModel = response.body();
                if (likeModel != null) {
                    mMeta.liked = true;
                    mTopic.likes_count = likeModel.count;
                    initBasicHeader();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
                mMeta.liked = false;
                mTopic.likes_count--;
                initBasicHeader();
            }
        });
    }

    private void sendUnLikeTopicRequest() {
        new TopicRequest().unlikeTopic(mTopic.id, DataUtils.getToken(), new Callback<LikeResponse>() {
            @Override
            public void onResponse(Response<LikeResponse> response, Retrofit retrofit) {
                LikeResponse likeModel = response.body();
                if (likeModel != null) {
                    mMeta.liked = false;
                    mTopic.likes_count = likeModel.count;
                    initBasicHeader();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
                mMeta.liked = true;
                mTopic.likes_count++;
                initBasicHeader();
            }
        });
    }

}
