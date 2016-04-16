package com.luckyhan.rubychina.ui.emoji;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.widget.WrappingGridView;

public class EmojiPagerAdapter extends PagerAdapter {

    private Context mContext;
    private EmojiModel mEmojiModel;

    public EmojiPagerAdapter(Context context, EmojiModel emojiModel) {
        mContext = context;
        mEmojiModel = emojiModel;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        WrappingGridView gridView = (WrappingGridView) LayoutInflater.from(mContext).inflate(R.layout.layout_emoji_gridview, container, false);
        gridView.setNumColumns(7);
        gridView.setAdapter(new EmojiOnePageAdapter(mContext, mEmojiModel, position));
        container.addView(gridView, 0);
        return gridView;
    }

}
