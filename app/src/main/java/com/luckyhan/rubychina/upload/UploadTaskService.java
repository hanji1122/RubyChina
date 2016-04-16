package com.luckyhan.rubychina.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.model.Post;
import com.luckyhan.rubychina.model.response.TopicResponse;
import com.luckyhan.rubychina.utils.CollectionUtils;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.squareup.tape.Task;

public class UploadTaskService extends Service implements ImageUploadTask.ImageUploadCallback, TopicUploadTask.TopicUploadCallback {

    public static final String EXTRA_POST_DATA = "post";

    private Context mContext;
    private Post mPost;
    private UploadTaskQueue mQueue;
    private int mCurrentIndex;

    private int mLastProgress = 0;
    private NotificationHandler mNotificationHandler;
    private boolean mIsRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mIsRunning) {
            ToastUtils.showShort(mContext, R.string.msg_upload_waiting);
            return START_STICKY;
        }
        mNotificationHandler = new NotificationHandler(mContext);
        startForeground(NotificationHandler.NOTIFICATION_ID, mNotificationHandler.getNotificationBuilder().build());

        mPost = intent.getParcelableExtra(EXTRA_POST_DATA);
        initTaskQueue();
        executeNext();
        return START_STICKY;
    }

    private void initTaskQueue() {
        mQueue = UploadTaskQueue.create();
        if (CollectionUtils.isEmpty(mPost.localImageList)) {
            mQueue.add(new TopicUploadTask(mPost));
        } else {
            int i = 0;
            for (Image image : mPost.localImageList) {
                mQueue.add(new ImageUploadTask(mContext, i++, image.realPath));
            }
        }
    }

    private void executeNext() {
        Task task = mQueue.peek();
        if (task == null) {
            return;
        }
        mIsRunning = true;
        if (task instanceof ImageUploadTask) {
            ((ImageUploadTask) task).execute(this);
        }
        if (task instanceof TopicUploadTask) {
            mNotificationHandler.updateNotification(getString(R.string.notification_upload_topic_content));
            ((TopicUploadTask) task).execute(this);
        }
    }

    @Override
    public void onStart(int index) {
        mCurrentIndex = index;
    }

    @Override
    public void onRequestProgress(long bytesWritten, long contentLength) {
        float percent = (float) bytesWritten / (float) contentLength;
        int progress = (int) (percent * 100);
        if (progress > mLastProgress + 2 || progress == 100) {
            mLastProgress = progress;
            mNotificationHandler.updateNotificationProgress(
                    progress,
                    getString(R.string.notification_upload_image_formatter, mCurrentIndex + 1, mPost.getUploadImageCount()));
        }
    }

    @Override
    public void onSuccess(String imageUrl) {
        mPost.addRemoteImage(imageUrl);
        mQueue.remove();
        if (mQueue.size() == 0) {
            mQueue.add(new TopicUploadTask(mPost));
        }
        mLastProgress = 0;
        executeNext();
    }

    @Override
    public void onFailure() {
        mQueue.remove();
        stopService();
        mNotificationHandler.showRetryNotification(mPost);
        //TODO cache the topic
    }

    @Override
    public void onTopicUploadSuccess(TopicResponse body) {
        stopService();
        ToastUtils.showLong(mContext, R.string.notification_upload_topic_success);
        mNotificationHandler.showSuccessNotification(body.topic);
    }

    @Override
    public void onTopicUploadFailure() {
        stopService();
        mNotificationHandler.showRetryNotification(mPost);
        //TODO cache the topic
    }

    private void stopService() {
        stopForeground(false);
        stopSelf();
        mIsRunning = false;
    }

}
