package com.luckyhan.rubychina.ui.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.activity.UserCenterActivity;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.utils.ImgLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserCenterUserListRecyclerAdapter extends BaseRecycleLoadMoreAdapter<User> {

    private OnItemClickedListener mListener;

    public UserCenterUserListRecyclerAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindBasicViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserViewHolder viewHolder = (UserViewHolder) holder;
        final User user = mList.get(position);
        viewHolder.itemView.setTag(user);
        if (user != null) {
            ImgLoader.INSTANCE.displayAvatarImage(user.avatar_url, viewHolder.avatar);
            viewHolder.login.setText(user.login);
            if (TextUtils.isEmpty(user.name)) {
                viewHolder.name.setVisibility(View.GONE);
            } else {
                viewHolder.name.setVisibility(View.VISIBLE);
                viewHolder.name.setText(user.name);
            }
            viewHolder.number.setText(mContext.getString(R.string.register_number_formatter, user.id));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(user);
                    } else {
                        UserCenterActivity.newInstance(mContext, user);
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(mLayoutInflater.inflate(R.layout.list_item_user, parent, false));
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.avatar) CircleImageView avatar;
        @Bind(R.id.login) TextView login;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.number) TextView number;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickedListener {
        void onClick(User user);
    }

}
