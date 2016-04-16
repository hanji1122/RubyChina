package com.luckyhan.rubychina.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luckyhan.rubychina.R;

public class SettingElementLayout extends LinearLayout {

    public SettingElementLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_setting_element, this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SettingElement,
                0, 0);

        try {
            TextView headerView = (TextView) findViewById(R.id.setting_header);
            TextView contentView = (TextView) findViewById(R.id.setting_text);

            headerView.setText(a.getString(R.styleable.SettingElement_headerText));
            int headerTextColor = a.getColor(R.styleable.SettingElement_headerTextColor, -1);
            if (headerTextColor != -1) {
                headerView.setTextColor(headerTextColor);
            }
            float headerTextSize = a.getDimension(R.styleable.SettingElement_headerTextSize, -1);
            if (headerTextSize != -1) {
                headerView.setTextSize(TypedValue.COMPLEX_UNIT_PX, headerTextSize);
            }

            contentView.setText(a.getString(R.styleable.SettingElement_contentText));
            int contentTextColor = a.getColor(R.styleable.SettingElement_contentTextColor, -1);
            if (contentTextColor != -1) {
                contentView.setTextColor(contentTextColor);
            }
            float contentTextSize = a.getDimension(R.styleable.SettingElement_contentTextSize, -1);
            if (contentTextSize != -1) {
                contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
            }

        } finally {
            a.recycle();
        }
    }

    public void setHeaderText(String text) {
        ((TextView) findViewById(R.id.setting_header)).setText(text);
    }

    public void setContentText(String text) {
        ((TextView) findViewById(R.id.setting_text)).setText(text);
    }

}
