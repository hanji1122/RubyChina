package com.luckyhan.rubychina.ui.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.luckyhan.rubychina.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EmojiOnePageAdapter extends BaseAdapter {

    private Context mContext;
    private int mPageIndex;
    private EmojiModel mEmojiModel;

    public EmojiOnePageAdapter(Context context, EmojiModel emojiModel, int position) {
        mContext = context;
        mEmojiModel = emojiModel;
        mPageIndex = position;
    }

    @Override
    public int getCount() {
        return 21;
    }

    @Override
    public Emotion getItem(int position) {
        if (position < getCount() - 1) {
            int index = mPageIndex * 20 + position;
            return mEmojiModel.getEmojiList().get(index);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_emoj, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount() - 1) {
            bindDeleteItem(viewHolder);
        } else {
            bindEmojiItem(viewHolder, position);
        }
        return convertView;
    }

    private void bindDeleteItem(ViewHolder viewHolder) {
        ViewGroup.LayoutParams lp = viewHolder.emotionView.getLayoutParams();
        lp.height = EmojiUtil.getEmojiWidth4Keyboard(mContext);
        lp.width = EmojiUtil.getEmojiWidth4Keyboard(mContext);
        viewHolder.emotionView.setImageResource(R.drawable.emoji_back);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmojiModel.delete();
            }
        };
        viewHolder.emotionView.setOnClickListener(clickListener);
        viewHolder.itemView.setOnClickListener(clickListener);
    }

    private void bindEmojiItem(ViewHolder viewHolder, final int position) {
        int id = mContext.getResources().getIdentifier(getItem(position).key, "drawable", mContext.getPackageName());
        ViewGroup.LayoutParams lp = viewHolder.emotionView.getLayoutParams();
        lp.height = EmojiUtil.getEmojiWidth4Keyboard(mContext);
        lp.width = EmojiUtil.getEmojiWidth4Keyboard(mContext);
        viewHolder.emotionView.setImageResource(id);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmojiModel.addEmojiSpan(mPageIndex, position);
            }
        };
        viewHolder.emotionView.setOnClickListener(clickListener);
        viewHolder.itemView.setOnClickListener(clickListener);
    }

    public class ViewHolder {

        View itemView;
        @Bind(R.id.emoji_iv) ImageView emotionView;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
            itemView = convertView;
        }
    }
}
