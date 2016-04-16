package com.luckyhan.rubychina.ui.emoji;

import android.content.Context;

import com.luckyhan.rubychina.utils.DeviceUtil;

public class EmojiUtil {

    public static int getEmojiWidth4Keyboard(Context context) {
        if (DeviceUtil.getScreenWidth(context) > 1000) {
            return 90;
        }
        int deviceDp = DeviceUtil.getDeviceDp(context);
        switch (deviceDp) {
            case 120:
                return 30;
            case 160:
                return 36;
            case 240:
                return 50;
            case 320:
                return 60;
            default:
                return (int) (40F * (float) deviceDp / 160F);
        }
    }

}
