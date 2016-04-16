package com.luckyhan.rubychina.widget.textview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import com.luckyhan.rubychina.R;

public class CustomURLSpan extends URLSpan {

    private Context mContext;
    private RichTextView.OnRichTextListener mListener;

    public CustomURLSpan(Context context, String url, RichTextView.OnRichTextListener listener) {
        super(url);
        mContext = context;
        mListener = listener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(ContextCompat.getColor(mContext, R.color.color_link));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            mListener.onURLClicked(getURL());
        }
    }

}
