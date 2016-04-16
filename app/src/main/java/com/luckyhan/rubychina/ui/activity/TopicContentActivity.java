package com.luckyhan.rubychina.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.TopicRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.Meta;
import com.luckyhan.rubychina.model.Page;
import com.luckyhan.rubychina.model.Reply;
import com.luckyhan.rubychina.model.Status;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.model.response.LikeResponse;
import com.luckyhan.rubychina.model.response.ReplyResponse;
import com.luckyhan.rubychina.model.response.TopicResponse;
import com.luckyhan.rubychina.ui.adapter.TopicCommentsAdapter;
import com.luckyhan.rubychina.ui.adapter.base.LoadMoreListener;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.ui.widget.LikeBounceView;
import com.luckyhan.rubychina.ui.widget.TopicHeader;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ClipboardUtil;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.ListViewUtils;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.luckyhan.rubychina.widget.ContentLoadingView;
import com.luckyhan.rubychina.widget.LoadMoreListView;
import com.luckyhan.rubychina.widget.textview.RichTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class TopicContentActivity extends BaseSwipeActivity implements LoadMoreListener,
        TopicHeader.OnUpdateListener, View.OnClickListener,
        AbsListView.OnScrollListener, ContentLoadingView.RetryListener,
        TopicCommentsAdapter.OnItemClickListener, AdapterView.OnItemLongClickListener, RichTextView.OnFloorClickListener {

    public static final String EXTRA_TOPIC = "topic";

    @Bind(R.id.avatar) ImageView mToolBarAvatarView;
    @Bind(R.id.author) TextView mToolBarAuthorView;
    @Bind(R.id.content_reply_float) View mFloatView;
    @Bind(R.id.tv_reply_count_float) TextView mReplyCountFloatView;
    @Bind(R.id.lv_comments) LoadMoreListView mListView;
    @Bind(R.id.content_loading) ContentLoadingView mLoadingView;
    private TopicHeader mTopicHeader;

    private static final int REQUEST_CODE_COMMENT = 0;

    private Topic mTopic;
    private int mFloatHeight;
    private int mFloatY;
    private int mFirstItemOffsetY;

    private Meta mMeta = new Meta();
    private TopicCommentsAdapter mAdapter;
    private Page mPage = new Page();

    public static void newInstance(Context context, Topic topic) {
        Intent i = new Intent(context, TopicContentActivity.class);
        i.putExtra(EXTRA_TOPIC, topic);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_content);
        ButterKnife.bind(this);
        mLoadingView.setRetryListener(this);
        mFloatView.setVisibility(View.GONE);
        mFloatView.setOnClickListener(this);
        mFloatView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = mFloatView.getMeasuredHeight();
                if (height > 0) {
                    mFloatHeight = height;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mFloatView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mFloatView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
        mTopic = getIntent().getParcelableExtra(EXTRA_TOPIC);

        addTopicHeader();

        mAdapter = new TopicCommentsAdapter(getContext(), this, this);
        mListView.setOnLoadMoreListener(this);
        mListView.setAdapter(mAdapter);

        mToolBarAuthorView.setOnClickListener(this);
        mToolBarAvatarView.setOnClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.addOnScrollListener(this);
        initToolBar();

        if (mTopic != null && !TextUtils.isEmpty(mTopic.id)) {
            requestTopicReply(mTopic.id);
        }
        initViews();
    }

    private void addTopicHeader() {
        mTopicHeader = (TopicHeader) LayoutInflater.from(getContext()).inflate(R.layout.layout_header, mListView, false);
        mTopicHeader.setOnUpdateListener(this);
        if (mTopic != null) {
            mTopicHeader.setTopic(mTopic);
        }
        mListView.addHeaderView(mTopicHeader);
    }

    private void initViews() {
        if (mTopic == null || mTopic.user == null) {
            return;
        }
        mToolBarAvatarView.setVisibility(View.VISIBLE);
        ImgLoader.INSTANCE.displayAvatarImage(mTopic.user.avatar_url, mToolBarAvatarView);
        mToolBarAuthorView.setText(mTopic.user.login);
        mReplyCountFloatView.setText(getContext().getString(R.string.topic_comment_count_formatter, mTopic.replies_count));
    }

    private void requestTopicReply(final String topicId) {
        new TopicRequest().getTopicReplies(topicId, mPage.current(), Page.LIMIT, DataUtils.getToken(), new Callback<ReplyResponse>() {
            @Override
            public void onResponse(Response<ReplyResponse> response, Retrofit retrofit) {
                ReplyResponse replyModel = response.body();
                if (replyModel == null) {
                    return;
                }
                if (mPage.isTop()) {
                    mAdapter.setList(replyModel.replies);
                    mAdapter.setLikedIds(replyModel.meta.user_liked_reply_ids);
                } else {
                    mAdapter.addList(replyModel.replies);
                    mAdapter.addLikedIds(replyModel.meta.user_liked_reply_ids);
                }
                if (replyModel.replies.size() < Page.LIMIT) {
                    if (mAdapter.getCount() == 0) {
                        mListView.setEnd(getString(R.string.no_comments));
                    } else {
                        mListView.setEnd();
                    }
                } else {
                    mListView.setWaiting();
                }
                mPage.setLastSuccess();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(getContext(), getString(R.string.msg_net_error));
                mPage.recover();
                mListView.setError();
            }
        });
    }

    @Override
    public void onLoadMore() {
        mPage.nextPage();
        requestTopicReply(mTopic.id);
    }

    @Override
    public void onClickError() {
        mPage.nextPage();
        requestTopicReply(mTopic.id);
    }

    @Override
    public void onClickEnd() {
        ListViewUtils.smoothScrollListViewToTop(mListView);
    }

    @Override
    public void onUpdateTopic(TopicResponse topicModel) {
        mTopic = topicModel.topic;
        mMeta = topicModel.meta;
        initViews();
        supportInvalidateOptionsMenu();
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onTopicNotExist() {
        ToastUtils.showShort(getContext(), R.string.msg_topic_not_exist);
        finish();
    }

    @Override
    public void onUpdateFloatHeight(int topY) {
        mFloatY = topY;
    }

    @Override
    public void onNetError() {
        mLoadingView.showRetryView();
        mLoadingView.setRetryListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        MenuItem menuItem = menu.findItem(R.id.action_bookmark);
        if (mMeta.favorited) {
            menuItem.setIcon(R.drawable.ic_bookmark_white_24dp);
        } else {
            menuItem.setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark: {
                if (AppUtils.jumpLogin(getContext())) {
                    return true;
                }
                if (mMeta.favorited) {
                    mMeta.favorited = false;
                    sendRemoveFavRequest();
                } else {
                    mMeta.favorited = true;
                    sendAddFavRequest();
                }
                supportInvalidateOptionsMenu();
                return true;
            }

            case R.id.action_share: {
                AppUtils.openShareIntent(getContext(), getString(R.string.share_topic_content, mTopic.id));
                return true;
            }

            case R.id.action_comment: {
                if (AppUtils.jumpLogin(getContext())) {
                    return true;
                }
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra(CommentActivity.EXTRA_TOPIC_ID, mTopic.id);
                startActivityForResult(intent, REQUEST_CODE_COMMENT);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COMMENT && resultCode == RESULT_OK) {
            mListView.setWaiting();
            mPage.refresh();
            mAdapter.clearList();
            mTopicHeader.requestTopicDetail();
            requestTopicReply(mTopic.id);
        }
    }

    private void sendRemoveFavRequest() {
        new TopicRequest().unFavorite(mTopic.id, DataUtils.getUserToken().access_token, new Callback<Status>() {
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Status status = response.body();
                    if (status == null || !status.isSuccess()) {
                        mMeta.favorited = true;
                        supportInvalidateOptionsMenu();
                    }
                } else {
                    mMeta.favorited = true;
                    supportInvalidateOptionsMenu();
                    ErrorResponse errorResponse = ErrorUtils.parseError(getContext(), response, retrofit);
                    ToastUtils.showShort(getContext(), errorResponse.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                mMeta.favorited = true;
                supportInvalidateOptionsMenu();
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
            }
        });
    }

    private void sendAddFavRequest() {
        new TopicRequest().addTopicFavorite(mTopic.id, DataUtils.getUserToken().access_token, new Callback<Status>() {
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Status status = response.body();
                    if (status == null || !status.isSuccess()) {
                        mMeta.favorited = false;
                        supportInvalidateOptionsMenu();
                    }
                } else {
                    mMeta.favorited = false;
                    supportInvalidateOptionsMenu();
                    ErrorResponse errorResponse = ErrorUtils.parseError(getContext(), response, retrofit);
                    ToastUtils.showShort(getContext(), errorResponse.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                mMeta.favorited = false;
                supportInvalidateOptionsMenu();
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
            }
        });
    }

    private void sendLikeReplyRequest(final String replyId, final int position) {
        new TopicRequest().likeReply(replyId, DataUtils.getToken(), new Callback<LikeResponse>() {

            @Override
            public void onResponse(Response<LikeResponse> response, Retrofit retrofit) {
                LikeResponse result = response.body();
                if (result == null) return;
                mAdapter.addReplyLike(result, position);
            }

            @Override
            public void onFailure(Throwable t) {
                mAdapter.removeReplyLikeFake(replyId, position);
            }
        });
    }

    private void sendUnLikeReplyRequest(final String replyId, final int position) {
        new TopicRequest().unlikeReply(replyId, DataUtils.getToken(), new Callback<LikeResponse>() {
            @Override
            public void onResponse(Response<LikeResponse> response, Retrofit retrofit) {
                LikeResponse result = response.body();
                if (result == null) return;
                mAdapter.removeReplyLike(result, position);
            }

            @Override
            public void onFailure(Throwable t) {
                mAdapter.addReplyLikeFake(replyId, position);
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int adapterPos = (int) id;
        if (id < 0 || mAdapter.getItemViewType(adapterPos) == TopicCommentsAdapter.ITEM_TYPE_DELETED) {
            return false;
        }
        final Reply reply = mAdapter.getItem(adapterPos);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(R.array.reply_popup, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        ClipboardUtil.copyToClipboard(getContext(), reply.body_html);
                        ToastUtils.showShort(getContext(), R.string.msg_comment_copied);
                        break;
                    }

                    case 1: {
                        if (AppUtils.jumpLogin(getContext())) {
                            return;
                        }
                        CommentActivity.newInstance(getContext(), mTopic.id, reply.user.login);
                        break;
                    }
                }
            }
        });
        builder.show();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
            case R.id.author: {
                UserCenterActivity.newInstance(getContext(), mTopic.user);
                break;
            }

            case R.id.content_reply_float: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mListView.smoothScrollToPositionFromTop(1, mFloatHeight);
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mListView.getFirstVisiblePosition() == 0) {
            View firstChild = mListView.getChildAt(0);
            int topY = 0;
            if (firstChild != null) {
                topY = firstChild.getTop();
                mFirstItemOffsetY = topY;
            }
            if (mFloatY != 0 && Math.abs(mFirstItemOffsetY) >= mFloatY) {
                mFloatView.setVisibility(View.VISIBLE);
            } else {
                mFloatView.setVisibility(View.GONE);
            }
        } else if (mFloatY != 0) {
            mFloatView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRetry() {
        mPage.refresh();
        mAdapter.clearList();
        mTopicHeader.requestTopicDetail();
        requestTopicReply(mTopic.id);
    }

    @Override
    public void onUserClick(int position) {
        Reply reply = mAdapter.getItem(position);
        UserCenterActivity.newInstance(getContext(), reply.user);
    }

    @Override
    public void onContentClick(int position) {
        CommentPagerActivity.newInstance(getContext(), mTopic, position, mAdapter.getList(), true);
    }

    @Override
    public void onFavClick(View view, int position) {
        Reply reply = mAdapter.getItem(position);
        boolean isLike = mAdapter.isLikedItem(reply.id);
        LikeBounceView favView = (LikeBounceView) view;

        if (AppUtils.jumpLogin(getContext())) {
            return;
        }
        if (isLike) {
            favView.setChecked(false);
            mAdapter.removeReplyLikeFake(reply.id, position);
            sendUnLikeReplyRequest(reply.id, position);
        } else {
            favView.setChecked(true, true);
            mAdapter.addReplyLikeFake(reply.id, position);
            sendLikeReplyRequest(reply.id, position);
        }
    }

    @Override
    public void onFloorClicked(int floor) {
        if (floor <= mAdapter.getCount()) {
            mListView.smoothScrollToPositionFromTop(floor, mFloatHeight);
        }
    }

}
