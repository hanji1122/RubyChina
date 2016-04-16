package com.luckyhan.rubychina.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.luckyhan.rubychina.model.HtmlData;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.ui.activity.ImageGalleryActivity;
import com.luckyhan.rubychina.ui.activity.UserCenterActivity;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class FineWebView extends FixedWebView {

    private static final String TEMPLATE_PATH = "file:///android_asset/article.html";
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private WebCallbacks mCallbacks;
    private HtmlData mHtmlData;


    public FineWebView(Context context) {
        super(context);
        setUp();
    }

    public FineWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
    }

    public FineWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
    }

    private void setUp() {
        if (isInEditMode()) {
            return;
        }
        setWebViewClient(new CustomWebViewClient(getContext()));
        setJavaScriptInterface();
    }

    private void setJavaScriptInterface() {
        addJavascriptInterface(new JavaScriptInterface(this), JavaScriptInterface.NAME);
    }

    public void setData(HtmlData htmlData) {
        mHtmlData = htmlData;
        setCallbacks(mWebCallbacks);
        loadUrl(TEMPLATE_PATH);
    }

    public void setImage(String id) {
        if (!TextUtils.isEmpty(id)) {
            invokeJsFunction("setImage", new Object[]{id});
        }
    }

    private void invokeJsFunction(String method, Object[] obj) {
        if (obj != null && obj.length != 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("javascript:").append(method);
            builder.append("(");
            int len = obj.length;
            int j = 0;
            while (j < len) {
                Object o = obj[j];
                if ((o instanceof Integer) || (o instanceof Float) || (o instanceof Double))
                    builder.append(o);
                else if (o instanceof String)
                    builder.append("\"").append(o).append("\"");
                else
                    builder.append("\"").append(String.valueOf(o)).append("\"");
                if (j < len - 1)
                    builder.append(",");
                j++;
            }
            builder.append(")");
            loadUrl(builder.toString());
        }
    }

    private static class JavaScriptInterface {

        public static final String NAME = "app";

        private FineWebView mWebView;

        public JavaScriptInterface(FineWebView webpageview) {
            mWebView = webpageview;
        }

        @JavascriptInterface
        public String getContent() {
            return mWebView.getContentHtml();
        }

        @JavascriptInterface
        public boolean onImageClicked(String imgId) {
            return mWebView.dispatchOnImageClicked(imgId);
        }

        @JavascriptInterface
        public void onAtUserClicked(String atUser) {
            mWebView.dispatchOnAtUserUrlClicked(atUser);
        }

        @JavascriptInterface
        public void onWebReady() {
            mWebView.dispatchOnWebReady();
        }
    }

    public void setCallbacks(WebCallbacks webCallBack) {
        mCallbacks = webCallBack;
    }

    private WebCallbacks mWebCallbacks = new WebCallbacks() {
        @Override
        public boolean onImageClicked(String imgId) {
            int index = Integer.parseInt(imgId);
            Intent intent = new Intent(getContext(), ImageGalleryActivity.class);
            intent.putExtra("position", index);
            intent.putParcelableArrayListExtra("imgList", (ArrayList<? extends Parcelable>) mHtmlData.imgList);
            getContext().startActivity(intent);
            return true;
        }

        @Override
        public boolean onAtUserUrlClicked(String url) {
            if (url.startsWith("@")) {
                String[] slices = url.split("@");
                if (slices.length < 2) {
                    return true;
                }
                User user = new User();
                user.login = slices[1];
                UserCenterActivity.newInstance(getContext(), user);
                return true;
            }
            return false;
        }

        @Override
        public void onWebReady() {
            loadImages(mHtmlData);
        }
    };

    private void loadImages(HtmlData result) {
        List<Image> imgList = result.imgList;
        if (imgList == null) return;

        for (int i = 0; i < imgList.size(); i++) {
            final int finalI = i;
            ImgLoader.INSTANCE.loadImage(imgList.get(i).url, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    setImage(finalI + "");
                }
            });
        }
    }

    public interface WebCallbacks {

        boolean onImageClicked(String imgId);

        boolean onAtUserUrlClicked(String url);

        void onWebReady();
    }

    private String getContentHtml() {
        return mHtmlData.html;
    }

    private boolean dispatchOnAtUserUrlClicked(final String url) {
        Runnable runnable = new Runnable() {
            public void run() {
                if (mCallbacks != null)
                    mCallbacks.onAtUserUrlClicked(url);
            }
        };
        mUiHandler.post(runnable);
        return true;
    }

    private void dispatchOnWebReady() {
        Runnable runnable = new Runnable() {
            public void run() {
                if (mCallbacks != null)
                    mCallbacks.onWebReady();
            }
        };
        mUiHandler.post(runnable);
    }

    private boolean dispatchOnImageClicked(final String imgId) {
        Runnable runnable = new Runnable() {
            public void run() {
                if (mCallbacks != null)
                    mCallbacks.onImageClicked(imgId);
            }
        };
        mUiHandler.post(runnable);
        return true;
    }

    private static class CustomWebViewClient extends WebViewClient {

        private Context context;

        public CustomWebViewClient(Context context) {
            this.context = context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
                AppUtils.openUrl(context, url);
                return true;
            }
            if (url.startsWith("mailto:")) {
                String address = url.split("mailto:")[1];
                AppUtils.openEmail(context, address);
                return true;
            }
            return true;
        }

    }

}
