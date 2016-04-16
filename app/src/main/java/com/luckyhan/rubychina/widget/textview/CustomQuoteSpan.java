package com.luckyhan.rubychina.widget.textview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.support.annotation.ColorInt;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.style.LeadingMarginSpan;

public class CustomQuoteSpan implements LeadingMarginSpan, ParcelableSpan {

    private static final int QUOTE_SPAN = 9;
    private static final int STRIPE_WIDTH = 10;
    private static final int GAP_WIDTH = 12;

    private final int mColor;

    public CustomQuoteSpan() {
        super();
        mColor = 0xfff4f6f9;
    }

    public CustomQuoteSpan(@ColorInt int color) {
        super();
        mColor = color;
    }

    public CustomQuoteSpan(Parcel src) {
        mColor = src.readInt();
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return QUOTE_SPAN;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(mColor);
    }

    @ColorInt
    public int getColor() {
        return mColor;
    }

    public int getLeadingMargin(boolean first) {
        return STRIPE_WIDTH + GAP_WIDTH;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout layout) {
        Paint.Style style = p.getStyle();
        int color = p.getColor();

        p.setStyle(Paint.Style.FILL);
        p.setColor(mColor);

        c.drawRect(x, top, x + dir * STRIPE_WIDTH, bottom, p);

        p.setStyle(style);
        p.setColor(color);
    }
}
