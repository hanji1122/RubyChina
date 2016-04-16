package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.HtmlData;
import com.luckyhan.rubychina.model.Reply;
import com.luckyhan.rubychina.model.response.LikeResponse;
import com.luckyhan.rubychina.ui.base.ItemBaseAdapter;
import com.luckyhan.rubychina.ui.widget.HorizontalPhotosRecyclerView;
import com.luckyhan.rubychina.ui.widget.LikeBounceView;
import com.luckyhan.rubychina.utils.HtmlUtil;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.StringUtils;
import com.luckyhan.rubychina.utils.TimeUtils;
import com.luckyhan.rubychina.utils.URLImageGetter;
import com.luckyhan.rubychina.widget.textview.RichTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TopicCommentsAdapter extends ItemBaseAdapter<Reply> implements View.OnClickListener {

    private static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_DELETED = 1;

    private OnItemClickListener mListener;
    private RichTextView.OnFloorClickListener mFloorClickListener;
    private List<String> mLikedIds;

    public TopicCommentsAdapter(Context context, OnItemClickListener listener, RichTextView.OnFloorClickListener floorClickListener) {
        super(context);
        mListener = listener;
        mFloorClickListener = floorClickListener;
    }

    public void setLikedIds(String... ids) {
        if (mLikedIds == null) {
            mLikedIds = new ArrayList<>();
        } else {
            mLikedIds.clear();
        }
        mLikedIds.addAll(Arrays.asList(ids));
    }

    public void addLikedIds(String... ids) {
        if (mLikedIds == null) {
            mLikedIds = new ArrayList<>();
        }
        mLikedIds.addAll(Arrays.asList(ids));
    }

    public void removeLikeId(String replyId) {
        if (mLikedIds == null) return;
        Iterator<String> iterator = mLikedIds.iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            if (id.equals(replyId)) {
                iterator.remove();
            }
        }
    }

    public boolean isLikedItem(String replyId) {
        if (mLikedIds == null) return false;
        for (String likeId : mLikedIds) {
            if (likeId.equals(replyId)) {
                return true;
            }
        }
        return false;
    }

    public void addReplyLike(LikeResponse likeModel, int position) {
        mList.get(position).likes_count = likeModel.count;
        addLikedIds(likeModel.obj_id);
        notifyDataSetChanged();
    }

    public void removeReplyLike(LikeResponse likeModel, int position) {
        mList.get(position).likes_count = likeModel.count;
        removeLikeId(likeModel.obj_id);
        notifyDataSetChanged();
    }

    public void addReplyLikeFake(String replyId, int position) {
        mList.get(position).likes_count++;
        addLikedIds(replyId);
        notifyDataSetChanged();
    }

    public void removeReplyLikeFake(String replyId, int position) {
        mList.get(position).likes_count--;
        removeLikeId(replyId);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        Reply reply = getItem(position);
        return reply.deleted ? ITEM_TYPE_DELETED : ITEM_TYPE_NORMAL;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEM_TYPE_DELETED: {
                convertView = getDeletedView(position, convertView, parent);
                break;
            }

            case ITEM_TYPE_NORMAL: {
                convertView = getNormalView(position, convertView, parent);
                break;
            }
        }
        convertView.setFocusable(false);
        return convertView;
    }

    @NonNull
    private View getNormalView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = getLayoutInflater().inflate(R.layout.list_item_comment, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bindData(viewHolder, getItem(position), position);

        viewHolder.commentUserAvatar.setTag(position);
        viewHolder.commentUserView.setTag(position);
        viewHolder.commentContentView.setTag(position);
        viewHolder.commentLikeActionView.setTag(position);
        viewHolder.commentUserAvatar.setOnClickListener(this);
        viewHolder.commentUserView.setOnClickListener(this);
        viewHolder.commentContentView.setOnClickListener(this);
        viewHolder.commentLikeActionView.setOnClickListener(this);
        viewHolder.commentContentView.setOnFloorClickListener(mFloorClickListener);
        return convertView;
    }

    @NonNull
    private View getDeletedView(int position, View convertView, ViewGroup parent) {
        DeletedViewHolder viewHolder;
        if (convertView == null || !(convertView.getTag() instanceof DeletedViewHolder)) {
            convertView = getLayoutInflater().inflate(R.layout.list_item_notification_deleted, parent, false);
            viewHolder = new DeletedViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DeletedViewHolder) convertView.getTag();
        }
        viewHolder.deletedView.setText(mContext.getString(R.string.comment_deleted_formatter, position + 1));
        return convertView;
    }

    private void bindData(ViewHolder holder, Reply reply, int position) {
        ImgLoader.INSTANCE.displayAvatarImage(reply.user.avatar_url, holder.commentUserAvatar);
        // parse reply html body
        HtmlData result = HtmlUtil.removeImgTags(reply.body_html);

        if (result.imgList != null) {
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.recyclerView.setAdapter(new TopicReplyImgsRecyclerAdapter(mContext, result.imgList));
        } else {
            holder.recyclerView.setVisibility(View.GONE);
        }

        CharSequence htmlData = StringUtils.trimWhiteLines(Html.fromHtml(result.html, new URLImageGetter(mContext, new URLImageGetter.LoadImgListener() {
            @Override
            public void onLoadingComplete() {
                notifyDataSetChanged();
            }
        }), null));
        String trimmedHtml = htmlData.toString().trim();
        if (TextUtils.isEmpty(trimmedHtml)) {
            holder.commentContentView.setVisibility(View.GONE);
        } else {
            holder.commentContentView.setVisibility(View.VISIBLE);
            holder.commentContentView.setRichText(htmlData);
        }
        holder.commentTimeView.setText(mContext.getString(R.string.formatter_comment_floor_time, position + 1, TimeUtils.getTime(reply.created_at)));
        holder.commentUserView.setText(reply.user.login);

        boolean isLike = isLikedItem(reply.id);
        holder.commentLikeActionView.setLikeCountView(reply.likes_count);
        holder.commentLikeActionView.setTag(isLike);
        holder.commentLikeActionView.setChecked(isLike);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.comment_avatar:
            case R.id.comment_user: {
                mListener.onUserClick(position);
                break;
            }

            case R.id.comment_content: {
                mListener.onContentClick(position);
                break;
            }

            case R.id.comment_like_action: {
                mListener.onFavClick(v, position);
                break;
            }
        }
    }

    class ViewHolder {
        @Bind(R.id.comment_floor_time) TextView commentTimeView;
        @Bind(R.id.comment_content) RichTextView commentContentView;
        @Bind(R.id.comment_user) TextView commentUserView;
        @Bind(R.id.recycler) HorizontalPhotosRecyclerView recyclerView;
        @Bind(R.id.comment_avatar) CircleImageView commentUserAvatar;
        @Bind(R.id.comment_like_action) LikeBounceView commentLikeActionView;

        public ViewHolder(final View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    class DeletedViewHolder {

        @Bind(R.id.text) TextView deletedView;

        public DeletedViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onUserClick(int position);

        void onContentClick(int position);

        void onFavClick(View view, int position);
    }

}
