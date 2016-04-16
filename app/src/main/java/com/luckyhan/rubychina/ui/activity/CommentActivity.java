package com.luckyhan.rubychina.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.TopicRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.ui.emoji.EmojiPad;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.luckyhan.rubychina.widget.SimpleLoadingDialog;
import com.squareup.okhttp.ResponseBody;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CommentActivity extends BaseSwipeActivity implements TextWatcher {

    public static final String EXTRA_TOPIC_ID = "topic_id";
    public static final String EXTRA_AT_USER = "at_user";

    @Bind(R.id.content) EditText mContentEt;
    @Bind(R.id.emoji_pad) EmojiPad mEmojiPad;

    private SimpleLoadingDialog mSimpleLoadingDialog;

    private String mTopicId;

    public static void newInstance(Context context, String topicId, String atUser) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(EXTRA_TOPIC_ID, topicId);
        intent.putExtra(EXTRA_AT_USER, atUser);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initToolBar(getString(R.string.title_comment));
        mEmojiPad.setUp(getWindow().getDecorView().getRootView(), mContentEt);
        mEmojiPad.hidePickImages();
        mTopicId = getIntent().getStringExtra(EXTRA_TOPIC_ID);
        initAtUser();
        mContentEt.addTextChangedListener(this);
    }

    private void initAtUser() {
        String atUser = getIntent().getStringExtra(EXTRA_AT_USER);
        if (!TextUtils.isEmpty(atUser)) {
            mContentEt.append("@" + atUser + " ");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            exitDirectly();
            return true;
        }
        if (item.getItemId() == R.id.action_send) {
            final String body = mContentEt.getText().toString();
            final String token = DataUtils.getUserToken().access_token;
            if (TextUtils.isEmpty(body.trim())) {
                ToastUtils.showShort(getContext(), R.string.msg_comment_empty);
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.dialog_title_tip);
            builder.setMessage(getString(R.string.dialog_msg_send_comment));
            builder.setPositiveButton(R.string.dialog_send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSimpleLoadingDialog = SimpleLoadingDialog.show(getContext());
                    sendRequest(mTopicId, body, token);
                }
            });
            builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendRequest(String topicId, String body, String access_token) {
        new TopicRequest().addReply(topicId, body, access_token, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                mSimpleLoadingDialog.cancel();
                if (response.isSuccess()) {
                    if (response.body() != null) {
                        ToastUtils.showShort(getContext(), R.string.msg_comment_success);
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    ErrorResponse error = ErrorUtils.parseError(getContext(), response, retrofit);
                    if (error != null) {
                        ToastUtils.showLong(getContext(), error.toString());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mSimpleLoadingDialog.cancel();
                t.printStackTrace();
                ToastUtils.showShort(getContext(), R.string.msg_net_error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (exitDirectly()) {
            super.onBackPressed();
        }
    }

    private boolean exitDirectly() {
        if (TextUtils.isEmpty(mContentEt.getText().toString().trim())) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_title_tip);
        builder.setMessage(R.string.dialog_msg_comment_loss);
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton(R.string.dialog_option_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create().show();
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mEmojiPad.hideEmojiContainer();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mContentEt.getText().toString().length() == 0) {
            swipeBackEnableToggle(true);
        } else {
            swipeBackEnableToggle(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EmojiPad.SELECT_AT_USER && resultCode == RESULT_OK) {
            User user = data.getParcelableExtra(SelectAtUserActivity.EXTRA_USER);
            mContentEt.getEditableText().insert(mContentEt.getSelectionStart(), "@" + user.login + " ");
        }
    }
}
