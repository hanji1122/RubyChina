package com.luckyhan.rubychina.upload;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Post;
import com.luckyhan.rubychina.model.Topic;
import com.luckyhan.rubychina.ui.activity.TopicContentActivity;

public class NotificationHandler {

    public static final int NOTIFICATION_ID = 0x01;
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;

    public NotificationHandler(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_upload_topic_title))
                .setSmallIcon(R.drawable.ic_launcher_icon);
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        return mNotificationBuilder;
    }

    public void updateNotification(CharSequence content) {
        mNotificationBuilder
                .setContentText(content)
                .setProgress(0, 0, true);
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    public void updateNotificationProgress(int progress, CharSequence content) {
        mNotificationBuilder
                .setContentText(content)
                .setProgress(100, progress, false);
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    public void showRetryNotification(Post post) {
        post.remoteImageList = null; // clear successfully uploaded images before network failure occur
        Intent intent = new Intent(mContext, UploadTaskService.class);
        intent.putExtra(UploadTaskService.EXTRA_POST_DATA, post);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 12, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mNotificationBuilder
                .setProgress(0, 0, false)
                .setAutoCancel(true)
                .setContentText(mContext.getString(R.string.notification_upload_retry))
                .setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }

    public void showSuccessNotification(Topic topic) {
        Intent intent = new Intent(mContext, TopicContentActivity.class);
        intent.putExtra(TopicContentActivity.EXTRA_TOPIC, topic);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(TopicContentActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        mNotificationBuilder
                .setProgress(0, 0, false)
                .setAutoCancel(true)
                .setContentText(mContext.getString(R.string.notification_upload_topic_success))
                .setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());
    }

}
