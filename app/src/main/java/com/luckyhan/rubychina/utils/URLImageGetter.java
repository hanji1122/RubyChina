package com.luckyhan.rubychina.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

public class URLImageGetter implements Html.ImageGetter {

    private Context context;
    private LoadImgListener loadComplete;

    public URLImageGetter(Context context, LoadImgListener loadComplete) {
        this.context = context;
        this.loadComplete = loadComplete;
    }

    @Override
    public Drawable getDrawable(String source) {
        String fixedSource = HtmlUtil.fixImgURL(source);
        File file = ImageLoader.getInstance().getDiskCache().get(fixedSource);

        if (file != null && file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
            bitmapDrawable.setBounds(0, 0, 50, 50);
            return bitmapDrawable;
        }
        ImgLoader.INSTANCE.loadImage(fixedSource, ImgLoader.INSTANCE.getEmptyDisplayOptions(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                loadComplete.onLoadingComplete();
            }
        });
        return null;
    }

    public interface LoadImgListener {
        void onLoadingComplete();
    }

}
