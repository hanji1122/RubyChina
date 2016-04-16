package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.ui.activity.TopicContentActivity;
import com.luckyhan.rubychina.ui.activity.TopicListActivity;
import com.luckyhan.rubychina.ui.activity.UserCenterActivity;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.utils.ImgLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TopicRecycleAdapter extends BaseRecycleLoadMoreAdapter<Topic> {

    private boolean mHideNode;

    public TopicRecycleAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    public void hideNode() {
        mHideNode = true;
    }

    @Override
    public void onBindBasicViewHolder(ViewHolder holder, int position) {
        TopicViewHolder viewHolder = (TopicViewHolder) holder;
        Topic topic = mList.get(position);
        viewHolder.itemView.setTag(topic);
        if (topic != null) {
            viewHolder.title.setText(topic.title);
            viewHolder.postUser.setText(topic.user.login);
            viewHolder.postTime.setText(mContext.getString(R.string.formatter_reply_count_time, topic.replies_count, topic.getUpdatePrettyTime()));
            if (topic.excellent) {
                viewHolder.excellentView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.excellentView.setVisibility(View.GONE);
            }
            if (mHideNode) {
                viewHolder.node.setVisibility(View.GONE);
            } else {
                viewHolder.node.setText(topic.node_name);
            }
            ImgLoader.INSTANCE.displayAvatarImage(topic.user.avatar_url, viewHolder.avatar);
        }
    }

    @Override
    public ViewHolder onCreateBasicViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(mLayoutInflater.inflate(R.layout.list_item_topic, parent, false));
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.topic_item_avatar) CircleImageView avatar;
        @Bind(R.id.topic_item_node) TextView node;
        @Bind(R.id.topic_item_title) TextView title;
        @Bind(R.id.topic_item_user) TextView postUser;
        @Bind(R.id.topic_item_posttime) TextView postTime;
        @Bind(R.id.topic_item_excellent) TextView excellentView;

        public TopicViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Topic topic = (Topic) itemView.getTag();
                    TopicContentActivity.newInstance(getContext(), topic);
                }
            });

            View.OnClickListener userCenterClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Topic topic = (Topic) itemView.getTag();
                    UserCenterActivity.newInstance(mContext, topic.user);
                }
            };
            avatar.setOnClickListener(userCenterClickListener);
            postUser.setOnClickListener(userCenterClickListener);

            node.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Topic topic = (Topic) itemView.getTag();
                    TopicListActivity.newInstance(mContext, topic.node_id, topic.node_name);
                }
            });
        }

    }

}
