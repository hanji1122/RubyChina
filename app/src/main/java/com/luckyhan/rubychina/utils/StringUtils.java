package com.luckyhan.rubychina.utils;

public class StringUtils {

    public static CharSequence trimWhiteLines(CharSequence text) {
        while (text.length() - 1 >= 0 && text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }

    public static String fixUrl(String uri) {
        String fixedUri;
        if (uri != null && uri.startsWith("//")) {
            fixedUri = "http:" + uri;
        } else {
            fixedUri = uri;
        }
        return fixedUri;
    }

}
