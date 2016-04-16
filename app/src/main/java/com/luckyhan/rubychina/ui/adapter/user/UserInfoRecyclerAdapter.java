package com.luckyhan.rubychina.ui.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.UserInfoItem;
import com.luckyhan.rubychina.ui.adapter.base.ItemBaseRecycleAdapter;

public class UserInfoRecyclerAdapter extends ItemBaseRecycleAdapter<UserInfoItem> {

    private OnClickListener mListener;

    public UserInfoRecyclerAdapter(Context context, OnClickListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_userinfo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final UserInfoItem item = getItem(position);
        viewHolder.itemView.setTag(item);
        viewHolder.desc.setText(item.desc);
        viewHolder.content.setText(item.detail);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView desc;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.desc);
            content = (TextView) itemView.findViewById(R.id.content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoItem item = (UserInfoItem) v.getTag();
                    mListener.onClick(item);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    UserInfoItem item = (UserInfoItem) v.getTag();
                    mListener.onLongClick(item);
                    return true;
                }
            });
        }
    }

    public interface OnClickListener {
        void onClick(UserInfoItem item);

        void onLongClick(UserInfoItem item);
    }

}
