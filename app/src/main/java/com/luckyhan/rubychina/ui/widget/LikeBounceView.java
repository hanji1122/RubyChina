package com.luckyhan.rubychina.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.widget.CheckableImageView;
import com.luckyhan.rubychina.widget.CheckableRelativeLayout;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class LikeBounceView extends CheckableRelativeLayout {

    private CheckableImageView mLikeIconView;
    private TextView mLikeCountView;

    public LikeBounceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setChecked(boolean checked, boolean scale) {
        setChecked(checked);
        if (scale && checked) {
            beat(this);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        mLikeIconView.setChecked(checked);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_like_bounce, this);
        mLikeIconView = (CheckableImageView) findViewById(R.id.like_icon);
        mLikeCountView = (TextView) findViewById(R.id.like_count);
    }

    public void setLikeCountView(int count) {
        if (count == 0) {
            mLikeCountView.setVisibility(INVISIBLE);
            return;
        }
        mLikeCountView.setVisibility(VISIBLE);
        if (count >= 100) {
            mLikeCountView.setText(R.string.nine_nine_plus);
        } else {
            mLikeCountView.setText(String.valueOf(count));
        }
    }

    private void beat(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1.0F, 1.3F, 1.0F),
                ObjectAnimator.ofFloat(view, "scaleY", 1.0F, 1.3F, 1.0F),
                ObjectAnimator.ofFloat(view, "alpha", 1.0F, 0.8F, 1.0F)
        );
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.setDuration(300);
        animatorSet.start();
    }

}
