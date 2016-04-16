package com.luckyhan.rubychina;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.luckyhan.rubychina.ui.activity.MainActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;

public class RubyChinaApp extends Application {

    public static RubyChinaApp instance;

    public static RubyChinaApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initTalkingData();
        initBugly();
        initCrashHandler();
        initHawk();
        initImageLoader();
    }

    private void initTalkingData() {
        TCAgent.LOG_ON = true;
        TCAgent.init(this);
        TCAgent.setReportUncaughtExceptions(false);
    }

    private void initBugly() {
        CrashReport.initCrashReport(this);
    }

    private void initHawk() {
        Hawk.init(this).setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION).build();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initCrashHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                CrashReport.postCatchedException(ex);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                System.exit(1);
            }
        });
    }

}
