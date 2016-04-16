package com.luckyhan.rubychina.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ThirdPartyUtils {

    public static void openTwitter(Context context, String twitter) {
        try {
            // get the Twitter app if possible
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            AppUtils.openUrl(context, "https://twitter.com/" + twitter);
        }
    }

    public static void openGithub(Context context, String github) {
        AppUtils.openUrl(context, "https://github.com/" + github);
    }

}
