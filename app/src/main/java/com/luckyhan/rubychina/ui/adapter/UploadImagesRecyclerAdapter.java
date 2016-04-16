package com.luckyhan.rubychina.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.ui.emoji.EmojiPad;
import com.luckyhan.rubychina.ui.activity.ImageGalleryActivity;
import com.luckyhan.rubychina.ui.adapter.base.ItemBaseRecycleAdapter;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.MediaUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UploadImagesRecyclerAdapter extends ItemBaseRecycleAdapter<Image> {

    private OnDeleteImageListener mListener;

    public UploadImagesRecyclerAdapter(Context context, OnDeleteImageListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public Image getItem(int position) {
        return super.getItem(position - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImgViewHolder(mLayoutInflater.inflate(R.layout.list_item_upload_imgs, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ImgViewHolder viewHolder = (ImgViewHolder) holder;
        if (position == 0) {
            viewHolder.addView.setVisibility(View.VISIBLE);
            viewHolder.removeView.setVisibility(View.GONE);
            viewHolder.imageView.setImageResource(0);
            viewHolder.imageView.setBackgroundResource(R.drawable.bg_upload_add);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaUtils.openGallery((Activity) getContext(), EmojiPad.SELECT_PHOTO);
                }
            });
        } else {
            final int pos = viewHolder.getAdapterPosition() - 1;
            viewHolder.addView.setVisibility(View.GONE);
            viewHolder.removeView.setVisibility(View.VISIBLE);
            ImgLoader.INSTANCE.displayImage(getItem(position).url, viewHolder.imageView);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageGalleryActivity.newInstance(getContext(), pos, getList(), ImageGalleryActivity.FROM_POST_UPLOAD);
                }
            });
            viewHolder.removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(pos);
                    notifyDataSetChanged();
                    if (mListener != null) {
                        mListener.onDelete();
                    }
                }
            });
        }
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image) ImageView imageView;
        @Bind(R.id.add) ImageView addView;
        @Bind(R.id.remove) ImageView removeView;

        public ImgViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnDeleteImageListener {
        void onDelete();
    }

}
