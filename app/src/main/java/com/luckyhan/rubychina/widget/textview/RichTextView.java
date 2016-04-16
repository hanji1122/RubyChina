package com.luckyhan.rubychina.widget.textview;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.webkit.URLUtil;

import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.activity.UserCenterActivity;
import com.luckyhan.rubychina.ui.activity.WebViewActivity;

public class RichTextView extends MultilineEllipseTextView {

    private OnFloorClickListener mFloorClickListener;

    public RichTextView(Context context) {
        super(context);
        init();
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance());
        setOnTouchListener(new CustomLinkTouchListener());
    }

    public void setRichText(CharSequence text) {
        Spannable current = new SpannableString(text);
        handleURLSpan(current);
        handleQuoteSpan(current);
        handleStrikethroughSpan(current);
        setText(current);
    }

    private void handleStrikethroughSpan(Spannable current) {
        StrikethroughSpan[] spans = current.getSpans(0, current.length(), StrikethroughSpan.class);
        for (StrikethroughSpan span : spans) {
            int start = current.getSpanStart(span);
            int end = current.getSpanEnd(span);
            current.removeSpan(span);
            current.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void handleURLSpan(Spannable current) {
        URLSpan[] spans = current.getSpans(0, current.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = current.getSpanStart(span);
            int end = current.getSpanEnd(span);
            current.removeSpan(span);
            current.setSpan(new CustomURLSpan(getContext(), span.getURL(), mOnRichTextListener), start, end, 0);
        }
    }

    private void handleQuoteSpan(Spannable current) {
        QuoteSpan[] quoteSpan = current.getSpans(0, current.length(), QuoteSpan.class);
        for (QuoteSpan span : quoteSpan) {
            int start = current.getSpanStart(span);
            int end = current.getSpanEnd(span);
            current.removeSpan(span);
            current.setSpan(new CustomQuoteSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private OnRichTextListener mOnRichTextListener = new OnRichTextListener() {
        @Override
        public void onURLClicked(String url) {
            // at user tag
            if (url.startsWith("/")) {
                User user = new User();
                user.login = url.substring(1);
                UserCenterActivity.newInstance(getContext(), user);
            }
            // normal web url
            if (URLUtil.isNetworkUrl(url)) {
                WebViewActivity.newInstance(getContext(), url);
            }
            // floor hash tag
            if (url.length() > 6 && url.substring(0, 6).equalsIgnoreCase("#reply")) {
                String floor = url.substring(6);
                if (TextUtils.isDigitsOnly(floor)) {
                    if (mFloorClickListener != null) {
                        mFloorClickListener.onFloorClicked(Integer.valueOf(floor));
                    }
                }
            }
        }
    };

    public void setOnFloorClickListener(OnFloorClickListener listener) {
        mFloorClickListener = listener;
    }

    public interface OnRichTextListener {
        void onURLClicked(String url);
    }

    public interface OnFloorClickListener {
        void onFloorClicked(int floor);
    }

}
