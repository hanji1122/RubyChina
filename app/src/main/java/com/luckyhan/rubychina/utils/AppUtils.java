package com.luckyhan.rubychina.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.ui.activity.MainActivity;
import com.luckyhan.rubychina.ui.activity.OAuthActivity;
import com.luckyhan.rubychina.ui.activity.WebViewActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

public class AppUtils {

    public static boolean jumpLogin(final Context context) {
        if (!(context instanceof Activity) || DataUtils.isUserLogin()) return false;
        new AlertDialog.Builder(context).setTitle(context.getString(R.string.dialog_title_need_login)).setMessage(context.getString(R.string.dialog_content_need_login))
                .setNegativeButton(R.string.dialog_cancel, null).setPositiveButton(context.getString(R.string.dialog_login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, OAuthActivity.class);
                ((Activity) context).startActivityForResult(intent, MainActivity.REQUEST_LOGIN);
            }
        }).show();
        return true;
    }

    public static RecyclerView.ItemDecoration getAppItemDecoration(Context context) {
        return new HorizontalDividerItemDecoration.Builder(context)
                .color(ContextCompat.getColor(context, R.color.list_divider))
                .sizeResId(R.dimen.list_divider)
                .marginResId(R.dimen.list_divider_margin, R.dimen.list_divider_margin)
                .build();
    }

    public static void startActivity(Context context, Class cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static void openShareIntent(Context context, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_title)));
    }

    public static void openUrl(Context context, String url) {
        boolean browserType = SharedPrefUtil.getBoolean(context, Constants.Pref.BROWSER, false);
        if (browserType) {
            openLink(context, url);
        } else {
            WebViewActivity.newInstance(context, url);
        }
    }

    public static void openLink(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openAppMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showShort(context, R.string.msg_market_not_installed);
        }
    }

    public static void openEmail(Context context, String address) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.setType("plain/text");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showShort(context, R.string.msg_email_client_not_installed);
        }
    }

}
