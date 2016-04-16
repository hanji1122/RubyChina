package com.luckyhan.rubychina.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.model.Post;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.base.BaseActivity;
import com.luckyhan.rubychina.ui.emoji.EmojiPad;
import com.luckyhan.rubychina.upload.UploadTaskService;
import com.luckyhan.rubychina.utils.CollectionUtils;
import com.luckyhan.rubychina.utils.KeyBoardUtils;
import com.luckyhan.rubychina.utils.MediaUtils;
import com.luckyhan.rubychina.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_NODE = "node";

    @Bind(R.id.root) LinearLayout mRootLayout;
    @Bind(R.id.select_node) TextView mSelectNodeView;
    @Bind(R.id.title) EditText mTitle;
    @Bind(R.id.content) EditText mContent;
    @Bind(R.id.emoji_pad) EmojiPad mEmojiPad;

    private Node mCheckedNode;

    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        initNode();
        mEmojiPad.setUp(getWindow().getDecorView().getRootView(), mContent);
        initToolBar(R.string.title_post);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    private void initNode() {
        mCheckedNode = getIntent().getParcelableExtra(EXTRA_NODE);
        if (mCheckedNode != null) {
            mSelectNodeView.setText(mCheckedNode.name);
        }
        mSelectNodeView.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyBoardUtils.hideSoftInput(getContext(), mContent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            if (!validation()) {
                return false;
            }
            final String title = mTitle.getText().toString().trim();
            final String body = mContent.getText().toString().trim();
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.dialog_title_tip)
                    .setMessage(getString(R.string.dialog_msg_post))
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPost = new Post();
                            mPost.title = title;
                            mPost.content = body;
                            mPost.node = mCheckedNode;
                            mPost.localImageList = mEmojiPad.getUploadImageList();

                            Intent intent = new Intent(getContext(), UploadTaskService.class);
                            intent.putExtra(UploadTaskService.EXTRA_POST_DATA, mPost);
                            startService(intent);
                            finish();

                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, null).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validation() {
        if (mCheckedNode == null) {
            ToastUtils.showShort(getContext(), R.string.msg_post_validation_topic);
            return false;
        }
        if (TextUtils.isEmpty(mTitle.getText().toString().trim())) {
            ToastUtils.showShort(getContext(), R.string.msg_post_validation_title);
            mTitle.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(mContent.getText().toString().trim())) {
            ToastUtils.showShort(getContext(), R.string.msg_post_validation_content);
            mContent.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_node:
                Intent intent = new Intent(getContext(), ChooseNodesActivity.class);
                intent.putExtra(ChooseNodesActivity.EXTRA_NODE, mCheckedNode);
                startActivityForResult(intent, ChooseNodesActivity.REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChooseNodesActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Node node = data.getParcelableExtra(ChooseNodesActivity.EXTRA_NODE);
            if (node != null) {
                mCheckedNode = node;
                mSelectNodeView.setText(node.name);
            }
        }
        if (requestCode == EmojiPad.SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String realPath = MediaUtils.getImagePathFromUri(getContext(), selectedImage);
            Image image = new Image(selectedImage.toString(), "");
            image.realPath = realPath;
            mEmojiPad.updateImageRecycler(image);
        }
        if (requestCode == EmojiPad.SELECT_AT_USER && resultCode == RESULT_OK) {
            User user = data.getParcelableExtra(SelectAtUserActivity.EXTRA_USER);
            mContent.getEditableText().insert(mContent.getSelectionStart(), "@" + user.login + " ");
        }
    }

    @Override
    public void onBackPressed() {
        if (exitDirectly()) {
            super.onBackPressed();
        }
    }

    private boolean exitDirectly() {
        if (TextUtils.isEmpty(mTitle.getText().toString().trim())
                && TextUtils.isEmpty(mContent.getText().toString().trim())
                && CollectionUtils.isEmpty(mEmojiPad.getUploadImageList())) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_title_tip);
        builder.setMessage(R.string.dialog_msg_post_loss);
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
}
