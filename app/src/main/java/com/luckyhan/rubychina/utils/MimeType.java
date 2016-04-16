package com.luckyhan.rubychina.utils;

public class MimeType {

    public static final String GIF = "image/gif";
    public static final String PNG = "image/png";
    public static final String JPEG = "image/jpeg";
    public static final String WEBP = "image/webp";
    public static final String NOT_FOUND = "error";

    public static String getImageSuffix(String mimeType) {
        if (GIF.equalsIgnoreCase(mimeType)) {
            return "gif";
        }
        if (PNG.equalsIgnoreCase(mimeType)) {
            return "png";
        }
        if (JPEG.equalsIgnoreCase(mimeType)) {
            return "jpeg";
        }
        return "jpg";
    }

}
