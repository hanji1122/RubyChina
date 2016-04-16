package com.luckyhan.rubychina.utils;

import android.os.Build;
import android.widget.ListView;

public class ListViewUtils {

    /**
     * ListView 滚动到指定的行
     *
     * @param listView ListView
     * @param position 行号(0...n)
     */
    public static void smoothScrollListView(ListView listView, int position) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            listView.smoothScrollToPositionFromTop(position, 0);
        } else {
            listView.setSelection(position);
        }
    }

    /**
     * ListView 滚动到第一行
     *
     * @param listView ListView
     */
    public static void smoothScrollListViewToTop(final ListView listView) {
        if (listView != null) {
            smoothScrollListView(listView, 0);
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.setSelectionFromTop(0, 0);
                }
            }, 200);
        }
    }

}
