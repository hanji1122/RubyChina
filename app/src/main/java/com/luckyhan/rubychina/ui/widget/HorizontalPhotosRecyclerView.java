package com.luckyhan.rubychina.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.luckyhan.rubychina.R;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

public class HorizontalPhotosRecyclerView extends RecyclerView {

    public HorizontalPhotosRecyclerView(Context context) {
        super(context);
        init();
    }

    public HorizontalPhotosRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public HorizontalPhotosRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (getLayoutManager() == null) {
            setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        addItemDecoration(new VerticalDividerItemDecoration.Builder(getContext())
                .color(ContextCompat.getColor(getContext(), android.R.color.transparent))
                .sizeResId(R.dimen.comment_img_margin)
                .build());
    }

}
