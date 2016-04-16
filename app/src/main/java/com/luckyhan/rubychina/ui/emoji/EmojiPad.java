package com.luckyhan.rubychina.ui.emoji;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.ui.activity.SelectAtUserActivity;
import com.luckyhan.rubychina.ui.adapter.UploadImagesRecyclerAdapter;
import com.luckyhan.rubychina.ui.widget.HorizontalPhotosRecyclerView;
import com.luckyhan.rubychina.utils.KeyBoardUtils;
import com.luckyhan.rubychina.utils.ViewCompat;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class EmojiPad extends LinearLayout implements View.OnClickListener {

    public static final int SELECT_AT_USER = 99;
    public static final int SELECT_PHOTO = 100;

    private static final int STATE_OPEN_CLOSED = 0;
    private static final int STATE_OPEN_EMOJI = 1;
    private static final int STATE_OPEN_IMAGE = 2;

    @Bind(R.id.emoji_at) View mEditorAt;
    @Bind(R.id.emoji_pic) View mEditorPic;
    @Bind(R.id.emoji_pad_image_size) TextView mImageSizeView;
    @Bind(R.id.emoji_pick_images) View mPickImages;
    @Bind(R.id.recycler) HorizontalPhotosRecyclerView mRecyclerView;
    @Bind(R.id.emoji_toggle) View mEmojiToggleButton;
    @Bind(R.id.emoji_container) View mEmojiContainer;
    @Bind(R.id.emoji_pager) ViewPager mEmojiViewPager;
    @Bind(R.id.page_indicator) CircleIndicator mIndicator;
    private View mRootLayout;

    private EditText mEditText;
    private UploadImagesRecyclerAdapter mImageRecyclerAdapter;
    private int mState = STATE_OPEN_CLOSED;
    private int mSoftKeyboardHeight = 0;
    private OnKeyboardOpenCloseListener mOnKeyboardOpenCloseListener;

    public EmojiPad(Context context) {
        super(context);
        init();
    }

    public EmojiPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_emoji_pad, this, false);
        ButterKnife.bind(this, view);
        addView(view);
        mEditorAt.setOnClickListener(this);
        mEmojiToggleButton.setOnClickListener(this);
        mEditorPic.setOnClickListener(this);
    }

    public void hidePickImages() {
        mPickImages.setVisibility(GONE);
    }

    public void setUp(View rootLayout, EditText editText) {
        mRootLayout = rootLayout;
        mEmojiViewPager.setAdapter(new EmojiPagerAdapter(getContext(), new EmojiModel(editText)));
        mIndicator.setViewPager(mEmojiViewPager);
        setEditText(editText);
    }

    private void setEditText(EditText editText) {
        mEditText = editText;
        mEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mState = STATE_OPEN_CLOSED;
                mEmojiContainer.setVisibility(GONE);
                mRecyclerView.setVisibility(GONE);
                KeyBoardUtils.showKeyBoard(getContext(), mEditText);
            }
        });
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!isShown()) {
                        setVisibility(VISIBLE);
                    }
                } else {
                    if (isShown()) {
                        setVisibility(GONE);
                    }
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emoji_at: {
                SelectAtUserActivity.newInstance(getContext(), SELECT_AT_USER);
                break;
            }

            case R.id.emoji_toggle: {
                if (mSoftKeyboardHeight != 0) {
                    mOnKeyboardOpenCloseListener = new OnKeyboardOpenCloseListener() {
                        @Override
                        public void onClosed() {
                            handleClickEmoji();
                        }
                    };
                    KeyBoardUtils.hideSoftInput(getContext(), v);
                } else {
                    handleClickEmoji();
                }
                break;
            }

            case R.id.emoji_pic: {
                if (mSoftKeyboardHeight != 0) {
                    mOnKeyboardOpenCloseListener = new OnKeyboardOpenCloseListener() {

                        @Override
                        public void onClosed() {
                            handleClickImage();
                        }
                    };
                    KeyBoardUtils.hideSoftInput(getContext(), v);
                } else {
                    handleClickImage();
                }
                break;
            }
        }
    }

    private void handleClickEmoji() {
        switch (mState) {
            case STATE_OPEN_CLOSED:
                mEmojiContainer.setVisibility(VISIBLE);
                mState = STATE_OPEN_EMOJI;
                break;

            case STATE_OPEN_IMAGE:
                mRecyclerView.setVisibility(GONE);
                mEmojiContainer.setVisibility(VISIBLE);
                mState = STATE_OPEN_EMOJI;
                break;

            case STATE_OPEN_EMOJI:
                mEmojiContainer.setVisibility(GONE);
                mState = STATE_OPEN_CLOSED;
                break;
        }
    }

    private void handleClickImage() {
        switch (mState) {
            case STATE_OPEN_CLOSED:
                mRecyclerView.setVisibility(VISIBLE);
                mState = STATE_OPEN_IMAGE;
                break;

            case STATE_OPEN_IMAGE:
                mRecyclerView.setVisibility(GONE);
                mState = STATE_OPEN_CLOSED;
                break;

            case STATE_OPEN_EMOJI:
                mEmojiContainer.setVisibility(GONE);
                mRecyclerView.setVisibility(VISIBLE);
                mState = STATE_OPEN_IMAGE;
                break;
        }
        initImageRecyclerAdapter();
    }

    private void initImageRecyclerAdapter() {
        if (mRecyclerView.getAdapter() != null) {
            return;
        }
        mImageRecyclerAdapter = new UploadImagesRecyclerAdapter(getContext(),
                new UploadImagesRecyclerAdapter.OnDeleteImageListener() {
                    @Override
                    public void onDelete() {
                        updateImageIndicator();
                    }
                });
        mRecyclerView.setAdapter(mImageRecyclerAdapter);
    }

    public void updateImageRecycler(Image image) {
        mImageRecyclerAdapter.addItem(image);
        updateImageIndicator();
    }

    public List<Image> getUploadImageList() {
        return mImageRecyclerAdapter == null ? null : mImageRecyclerAdapter.getList();
    }

    private void updateImageIndicator() {
        int count = mImageRecyclerAdapter.getItemCount() - 1;
        if (count == 0) {
            mImageSizeView.setVisibility(GONE);
        } else {
            mImageSizeView.setVisibility(VISIBLE);
            mImageSizeView.setText(String.valueOf(count));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRootLayout.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewCompat.removeOnGlobalLayoutListener(mRootLayout, mOnGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            mRootLayout.getWindowVisibleDisplayFrame(r);
            int screenHeight;
            if (Build.VERSION.SDK_INT >= 5.0) {
                screenHeight = calculateScreenHeightForLollipop();
            } else {
                screenHeight = mRootLayout.getRootView().getHeight();
            }
            int heightDifference = screenHeight - (r.bottom - r.top);

            int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                heightDifference -= getContext().getResources().getDimensionPixelSize(resourceId);
            }
            mSoftKeyboardHeight = heightDifference;
            if (mOnKeyboardOpenCloseListener == null) {
                return;
            }
            if (mSoftKeyboardHeight == 0) {
                mOnKeyboardOpenCloseListener.onClosed();
            }
            mOnKeyboardOpenCloseListener = null;
        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public int calculateScreenHeightForLollipop() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public interface OnKeyboardOpenCloseListener {
        void onClosed();
    }

}
