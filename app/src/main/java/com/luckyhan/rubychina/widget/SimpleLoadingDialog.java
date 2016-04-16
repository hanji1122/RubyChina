package com.luckyhan.rubychina.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.luckyhan.rubychina.R;

public class SimpleLoadingDialog extends Dialog {

    private Context mContext;

    public static SimpleLoadingDialog show(Context context) {
        return show(context, false);
    }

    public static SimpleLoadingDialog show(Context context, boolean cancelable) {
        SimpleLoadingDialog dialog = new SimpleLoadingDialog(context);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }

    public SimpleLoadingDialog(Context context) {
        this(context, R.style.SimpleProgressDialog);
    }

    public SimpleLoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog, null);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setContentView(view);
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void dismiss() {
        if (!isShowing()) {
            return;
        }
        if (getContext() instanceof ContextWrapper) {
            Context context = ((ContextWrapper) getContext()).getBaseContext();
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    super.dismiss();
                }
            } else {
                super.dismiss();
            }
        } else {
            super.dismiss();
        }
    }

}
