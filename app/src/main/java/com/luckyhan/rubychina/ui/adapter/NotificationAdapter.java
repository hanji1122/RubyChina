package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.HtmlData;
import com.luckyhan.rubychina.model.Notification;
import com.luckyhan.rubychina.model.Reply;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.ui.activity.CommentPagerActivity;
import com.luckyhan.rubychina.ui.activity.TopicContentActivity;
import com.luckyhan.rubychina.ui.activity.UserCenterActivity;
import com.luckyhan.rubychina.ui.adapter.base.BaseRecycleLoadMoreAdapter;
import com.luckyhan.rubychina.utils.HtmlUtil;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.StringUtils;
import com.luckyhan.rubychina.utils.TimeUtils;
import com.luckyhan.rubychina.utils.URLImageGetter;
import com.luckyhan.rubychina.widget.textview.RichTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends BaseRecycleLoadMoreAdapter<Notification> implements URLImageGetter.LoadImgListener {

    private static final int VIEW_TYPE_ITEM_NORMAL = 3;
    private static final int VIEW_TYPE_ITEM_DELETED = 4;

    public NotificationAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public void onBindBasicViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationViewHolder) {
            NotificationViewHolder viewHolder = (NotificationViewHolder) holder;
            Notification notification = getItem(position);
            bindViews(viewHolder, notification);
        }
        // default view for deleted items
    }

    private void bindViews(NotificationViewHolder viewHolder, final Notification notification) {
        viewHolder.user.setText(notification.actor.login);
        ImgLoader.INSTANCE.displayAvatarImage(notification.actor.avatar_url, viewHolder.avatar);

        viewHolder.time.setText(TimeUtils.getPrettyTime(notification.created_at));

        if (notification.type.equals("TopicReply")) {
            viewHolder.type.setText(R.string.notification_reply);
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.content.setVisibility(View.VISIBLE);
            viewHolder.title.setText(notification.reply.topic_title);
            bindContentView(viewHolder, notification.reply.body_html);

            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Topic topic = new Topic();
                    topic.id = notification.reply.topic_id;
                    topic.title = notification.reply.topic_title;
                    TopicContentActivity.newInstance(getContext(), topic);
                }
            });
            viewHolder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Reply> replyList = new ArrayList<>();
                    notification.reply.user = notification.actor;
                    replyList.add(notification.reply);

                    Topic topic = new Topic();
                    topic.id = notification.reply.topic_id;
                    topic.title = notification.reply.topic_title;
                    CommentPagerActivity.newInstance(getContext(), topic, 0, replyList, false);
                }
            });

        } else if (notification.type.equals("Topic")) {
            viewHolder.type.setText(R.string.notification_new_post);
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.recyclerView.setVisibility(View.GONE);
            viewHolder.title.setText(notification.topic.title);
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicContentActivity.newInstance(getContext(), notification.topic);
                }
            });

        } else if (notification.type.equals("Mention")) {
            if (notification.mention_type.equals("Reply")) {
                viewHolder.type.setText(R.string.notification_mention_reply);
                viewHolder.title.setVisibility(View.GONE);
                bindContentView(viewHolder, notification.mention.body_html);
                viewHolder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Reply> replyList = new ArrayList<>();
                        Reply reply = new Reply();
                        reply.user = notification.actor;
                        reply.body_html = notification.mention.body_html;
                        reply.topic_id = notification.mention.topic_id;
                        replyList.add(reply);
                        Topic topic = new Topic();
                        topic.id = notification.mention.topic_id;
                        CommentPagerActivity.newInstance(getContext(), topic, 0, replyList, false);
                    }
                });

            } else if (notification.mention_type.equals("Topic")) {
                viewHolder.type.setText(R.string.notification_mention_topic);
                viewHolder.title.setVisibility(View.VISIBLE);
                viewHolder.title.setText(notification.mention.title);
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.recyclerView.setVisibility(View.GONE);
                viewHolder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Topic topic = new Topic();
                        topic.id = notification.mention.id;
                        topic.title = notification.mention.title;
                        topic.node_id = notification.mention.node_id;
                        topic.node_name = notification.mention.node_name;
                        TopicContentActivity.newInstance(getContext(), topic);
                    }
                });
            }

        } else if (notification.type.equals("Follow")) {
            viewHolder.type.setText(R.string.notification_follow_you);
            viewHolder.title.setVisibility(View.GONE);
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.recyclerView.setVisibility(View.GONE);
        }
    }

    private void bindContentView(NotificationViewHolder viewHolder, String body_html) {
        HtmlData result = HtmlUtil.removeImgTags(body_html);
        if (result.imgList != null) {
            viewHolder.recyclerView.setVisibility(View.VISIBLE);
            viewHolder.recyclerView.setAdapter(new TopicReplyImgsRecyclerAdapter(getContext(), result.imgList));
        } else {
            viewHolder.recyclerView.setVisibility(View.GONE);
        }
        CharSequence htmlData = StringUtils.trimWhiteLines(Html.fromHtml(result.html, new URLImageGetter(getContext(), this), null));
        String trimmedHtml = htmlData.toString().trim();
        if (TextUtils.isEmpty(trimmedHtml)) {
            viewHolder.content.setVisibility(View.GONE);
        } else {
            viewHolder.content.setVisibility(View.VISIBLE);
            viewHolder.content.setRichText(htmlData);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM_NORMAL) {
            return new NotificationViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_notification, parent, false));
        }
        return new DeletedViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_item_notification_deleted, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mList.size()) {
            Notification notification = getItem(position);
            if (notification.actor == null) {
                return VIEW_TYPE_ITEM_DELETED;
            } else {
                return VIEW_TYPE_ITEM_NORMAL;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onLoadingComplete() {
        notifyDataSetChanged();
    }

    class DeletedViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.text) TextView textView;

        public DeletedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textView.setText(R.string.notification_deleted);
        }
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.avatar) CircleImageView avatar;
        @Bind(R.id.user) TextView user;
        @Bind(R.id.type) TextView type;
        @Bind(R.id.time) TextView time;
        @Bind(R.id.title) TextView title;
        @Bind(R.id.content) RichTextView content;
        @Bind(R.id.recycler) RecyclerView recyclerView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            avatar.setOnClickListener(this);
            user.setOnClickListener(this);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Notification notification = mList.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.avatar:
                case R.id.user: {
                    UserCenterActivity.newInstance(getContext(), notification.actor);
                    break;
                }
                default:
                    break;
            }
        }
    }

}
