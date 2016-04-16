package com.luckyhan.rubychina.widget.textview;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.widget.TextView;

public class MultilineEllipseTextView extends TextView {

    private static final String CHS_PATTERN = "[\u4E00-\u9FA5]";
    private int maxLine;

    public MultilineEllipseTextView(Context context) {
        super(context);
        init();
    }

    public MultilineEllipseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultilineEllipseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.maxLine = TextViewCompat.getMaxLines(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (maxLine > 0 && getLineCount() > maxLine) {
            int maxLineEndOffset = getLayout().getLineEnd(maxLine - 1);
            CharSequence ellipsed = getText().subSequence(0, maxLineEndOffset - getEllipsisSpace());
            setText(ellipsed);
            append("...");
        }
    }

    private int getEllipsisSpace() {
        String text = getText().toString();
        int space = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.substring(text.length() - (i + 1), text.length() - i).matches(CHS_PATTERN)) {
                space += 2;
            } else {
                space++;
            }
            if (space > 3) {
                return i;
            }
        }
        return 0;
    }

}
