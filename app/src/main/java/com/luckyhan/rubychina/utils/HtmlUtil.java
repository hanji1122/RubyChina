package com.luckyhan.rubychina.utils;

import android.content.Context;

import com.luckyhan.rubychina.model.HtmlData;
import com.luckyhan.rubychina.model.Image;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

import java.io.File;
import java.util.ArrayList;

import jodd.jerry.Jerry;
import jodd.jerry.JerryFunction;

public class HtmlUtil {

    public static HtmlData removeImgTags(String html) {
        final HtmlData result = new HtmlData();
        Jerry jerry = Jerry.jerry(html);
        jerry.$("img").each(new JerryFunction() {
            @Override
            public boolean onNode(Jerry jerry, int i) {
                if (!jerry.hasClass("emoji")) {
                    if (result.imgList == null) {
                        result.imgList = new ArrayList<>();
                    }
                    Image img = new Image(jerry.attr("src"), jerry.attr("alt"));
                    result.imgList.add(img);
                    jerry.remove();
                }
                return true;
            }
        });
        result.html = jerry.htmlAll(false);
        return result;
    }

    public static HtmlData prepareHtmlData(final Context context, String html) {
        final HtmlData result = new HtmlData();
        Jerry jerry = Jerry.jerry(html);
        jerry.$("img").each(new JerryFunction() {
            @Override
            public boolean onNode(Jerry jerry, int i) {
                String src = jerry.attr("src");
                Image img = new Image(src, jerry.attr("alt"));
                result.addImage(img);
                jerry.attr("id", i + "");
                jerry.attr("src", "file:///android_res/drawable/bg_webview_img_default.png");
                jerry.attr("data-none", "file:///android_res/drawable/bg_webview_img_default.png");
                jerry.attr("data-src", src);
                jerry.attr("data-cache", getFilePath(context, src));
                jerry.attr("onclick", "onImageClicked(this)");
                return true;
            }
        });
        result.html = jerry.htmlAll(false);
        return result;
    }

    private static String getFilePath(Context context, String url) {
        String fileName = new Md5FileNameGenerator().generate(HtmlUtil.fixImgURL(url));
        File file = new File(ImageUtils.getCacheDirectory(context), fileName);
        return file.getPath();
    }

    public static String fixImgURL(String imgUri) {
        String fixedUri;
        if (imgUri.startsWith("//")) {
            fixedUri = "http:" + imgUri;
        } else {
            fixedUri = imgUri;
        }
        return fixedUri;
    }

}
