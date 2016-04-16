package com.luckyhan.rubychina.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.HtmlData;
import com.luckyhan.rubychina.model.Reply;
import com.luckyhan.rubychina.ui.activity.UserCenterActivity;
import com.luckyhan.rubychina.ui.base.BaseFragment;
import com.luckyhan.rubychina.utils.HtmlUtil;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.TimeUtils;
import com.luckyhan.rubychina.widget.FineWebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentDetailFragment extends BaseFragment implements View.OnClickListener {

    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_REPLY = "reply";

    @Bind(R.id.comment_avatar) CircleImageView mAvatarView;
    @Bind(R.id.comment_user) TextView mNameView;
    @Bind(R.id.comment_floor_time) TextView mFloorTimeView;
    @Bind(R.id.comment_like_action) View mLikeAction;
    @Bind(R.id.detail_webview) FineWebView mFineWebView;
    private Reply mReply;
    private int mPosition;

    public static CommentDetailFragment newInstance(int position, Reply reply) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_POSITION, position);
        args.putParcelable(EXTRA_REPLY, reply);
        CommentDetailFragment fragment = new CommentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPosition = args.getInt(EXTRA_POSITION);
            mReply = args.getParcelable(EXTRA_REPLY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_detail, container, false);
        ButterKnife.bind(this, view);
        mLikeAction.setVisibility(View.GONE);
        mAvatarView.setOnClickListener(this);
        mNameView.setOnClickListener(this);
        mFineWebView.setVerticalScrollBarEnabled(true);
        initData();
        return view;
    }

    private void initData() {
        ImgLoader.INSTANCE.displayImage(mReply.user.avatar_url, mAvatarView);
        mNameView.setText(mReply.user.login);
        mFloorTimeView.setText(getString(R.string.formatter_comment_floor_time, mPosition + 1, TimeUtils.getPrettyTime(mReply.created_at)));

        new AsyncTask<String, Void, HtmlData>() {

            private String html;

            @Override
            protected HtmlData doInBackground(String... params) {
                html = params[0];
                return HtmlUtil.prepareHtmlData(getContext(), html);
            }

            @Override
            protected void onPostExecute(final HtmlData htmlData) {
                super.onPostExecute(htmlData);
                mFineWebView.setData(htmlData);
            }
        }.execute(mReply.body_html);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_avatar:
            case R.id.comment_user: {
                UserCenterActivity.newInstance(getContext(), mReply.user);
                break;
            }
        }
    }
}
