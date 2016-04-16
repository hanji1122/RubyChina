package com.luckyhan.rubychina.utils;

import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    private static final ThreadLocal IMG_FILE_NAME_FORMAT = new ThreadLocal() {

        @Override
        protected Object initialValue() {
            return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        }
    };

    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            appCacheDir = context.getExternalCacheDir();
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    public static String getImageLoaderFilePath(Context context, String url) {
        String fileName = new Md5FileNameGenerator().generate(HtmlUtil.fixImgURL(url));
        File file = new File(getCacheDirectory(context), fileName);
        return file.getPath();
    }

    public static File createAppDir() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "RubyChina");
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static File createImageFile(String suffix) {
        String name = ((DateFormat) IMG_FILE_NAME_FORMAT.get()).format(new Date());
        String fileName = (new StringBuilder())
                .append("IMG_").append(name).append(".").append(suffix).toString();
        return new File(createAppDir(), fileName);
    }

}
