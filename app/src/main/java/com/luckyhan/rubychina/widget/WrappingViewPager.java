package com.luckyhan.rubychina.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class WrappingViewPager extends ViewPager {

    public WrappingViewPager(Context context) {
        super(context);
    }

    public WrappingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // find the first child view
            View view = getChildAt(0);
            if (view != null) {
                // measure the first child view with the specified measure spec
                view.measure(widthMeasureSpec, heightMeasureSpec);
                int h = view.getMeasuredHeight();
                setMeasuredDimension(getMeasuredWidth(), h);
                //do not recalculate height anymore
                getLayoutParams().height = h;
            }
        }
    }

}
