package com.luckyhan.rubychina.utils;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.RubyChinaApp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public enum ImgLoader {

    INSTANCE;

    private ColorDrawable mOccupyDrawable = new ColorDrawable(ContextCompat.getColor(RubyChinaApp.getContext(), R.color.image_foreground));
    private DisplayImageOptions mAvatarImageOptions;
    private DisplayImageOptions mDisplayImageOptions;
    private DisplayImageOptions mEmptyDisplayImageOptions;

    private DisplayImageOptions getAvatarDisplayOptions() {
        if (mAvatarImageOptions == null) {
            mAvatarImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.avatar_default)
                    .showImageForEmptyUri(R.drawable.avatar_default)
                    .showImageOnFail(R.drawable.avatar_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
        }
        return mAvatarImageOptions;
    }

    public DisplayImageOptions getEmptyDisplayOptions() {
        if (mEmptyDisplayImageOptions == null) {
            mEmptyDisplayImageOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
        }
        return mEmptyDisplayImageOptions;
    }

    public DisplayImageOptions getDisplayOptions() {
        if (mDisplayImageOptions == null) {
            mDisplayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(mOccupyDrawable)
                    .showImageForEmptyUri(mOccupyDrawable)
                    .showImageOnFail(mOccupyDrawable)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
        }
        return mDisplayImageOptions;
    }

    private DisplayImageOptions getDisplayOptions(@DrawableRes int resId) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(resId)
                .showImageForEmptyUri(resId)
                .showImageOnFail(resId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void displayAvatarImage(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(StringUtils.fixUrl(url), imageView, getAvatarDisplayOptions());
    }

    public void displayAvatarImage(String url, ImageView imageView, SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(StringUtils.fixUrl(url), imageView, getAvatarDisplayOptions(), listener);
    }

    public void displayImage(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(StringUtils.fixUrl(url), imageView, getDisplayOptions());
    }

    public void displayImage(String url, ImageView imageView, SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(StringUtils.fixUrl(url), imageView, getDisplayOptions(), listener);
    }

    public void displayImage(String url, ImageView imageView, @DrawableRes int loadingResId) {
        ImageLoader.getInstance().displayImage(StringUtils.fixUrl(url), imageView, getDisplayOptions(loadingResId));
    }

    public void displayImage(String url, ImageView imageView, @DrawableRes int loadingResId, SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(StringUtils.fixUrl(url), imageView, getDisplayOptions(loadingResId), listener);
    }

    public void loadImage(String url, DisplayImageOptions options, SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(StringUtils.fixUrl(url), options, listener);
    }

    public void loadImage(String url, SimpleImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(StringUtils.fixUrl(url), getDisplayOptions(), listener);
    }

}
