package com.luckyhan.rubychina.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("CommitPrefEdits")
public class SharedPrefUtil {

    private static final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public static void submit(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            mExecutorService.execute(new SubmitRunnable(editor));
        }
    }

    private static class SubmitRunnable implements Runnable {
        private final SharedPreferences.Editor editor;

        public SubmitRunnable(SharedPreferences.Editor editor) {
            this.editor = editor;
        }

        @Override
        public void run() {
            editor.commit();
        }
    }

    private static SharedPreferences getSpFile(Context context, String prefName) {
        if (prefName != null) {
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        } else {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static boolean getBoolean(Context context, String prefName, String key, boolean defaultValue) {
        return getSpFile(context, prefName).getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getBoolean(context, null, key, defaultValue);
    }

    public static int getInt(Context context, String prefName, String key, int defaultValue) {
        return getSpFile(context, prefName).getInt(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getInt(context, null, key, defaultValue);
    }

    public static long getLong(Context context, String prefName, String key, long defaultValue) {
        return getSpFile(context, prefName).getLong(key, defaultValue);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getLong(context, null, key, defaultValue);
    }

    public static String getString(Context context, String prefName, String key, String defaultValue) {
        return getSpFile(context, prefName).getString(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getString(context, null, key, defaultValue);
    }

    public static void putBoolean(Context context, String prefName, String key, boolean value) {
        SharedPreferences.Editor editor = getSpFile(context, prefName).edit();
        editor.putBoolean(key, value);
        submit(editor);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        putBoolean(context, null, key, value);
    }

    public static void putInt(Context context, String prefName, String key, int value) {
        SharedPreferences.Editor editor = getSpFile(context, prefName).edit();
        editor.putInt(key, value);
        submit(editor);
    }

    public static void putInt(Context context, String key, int value) {
        putInt(context, null, key, value);
    }

    public static void putLong(Context context, String prefName, String key, long value) {
        SharedPreferences.Editor editor = getSpFile(context, prefName).edit();
        editor.putLong(key, value);
        submit(editor);
    }

    public static void putLong(Context context, String key, long value) {
        putLong(context, null, key, value);
    }

    public static void putString(Context context, String prefName, String key, String value) {
        SharedPreferences.Editor editor = getSpFile(context, prefName).edit();
        editor.putString(key, value);
        submit(editor);
    }

    public static void putString(Context context, String key, String value) {
        putString(context, null, key, value);
    }

    public static void clear(Context context) {
        clear(context, null);
    }

    public static void clear(Context context, String prefName) {
        SharedPreferences.Editor editor = getSpFile(context, prefName).edit();
        editor.clear();
        submit(editor);
    }

    public static void remove(Context context, String key) {
        remove(context, null, key);
    }

    public static void remove(Context context, String prefName, String key) {
        SharedPreferences.Editor editor = getSpFile(context, prefName).edit();
        editor.remove(key);
        submit(editor);
    }


}
