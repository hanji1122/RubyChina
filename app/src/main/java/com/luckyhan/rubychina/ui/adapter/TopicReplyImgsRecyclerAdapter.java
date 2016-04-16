package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.ui.activity.ImageGalleryActivity;
import com.luckyhan.rubychina.ui.adapter.base.ItemBaseRecycleAdapter;
import com.luckyhan.rubychina.utils.ImgLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TopicReplyImgsRecyclerAdapter extends ItemBaseRecycleAdapter<Image> {

    public TopicReplyImgsRecyclerAdapter(Context context, List<Image> imgList) {
        super(context);
        setList(imgList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImgViewHolder(mLayoutInflater.inflate(R.layout.list_item_reply_img, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ImgViewHolder viewHolder = (ImgViewHolder) holder;
        final Image img = getItem(position);
        viewHolder.itemView.setTag(img);
        if (img != null) {
            ImgLoader.INSTANCE.displayImage(img.url, viewHolder.imageView);
        }
    }

    class ImgViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image) ImageView imageView;

        public ImgViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageGalleryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", getAdapterPosition());
                    bundle.putParcelableArrayList("imgList", (ArrayList<? extends Parcelable>) getList());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
