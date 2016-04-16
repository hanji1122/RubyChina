package com.luckyhan.rubychina.api.request;

import com.luckyhan.rubychina.BuildConfig;
import com.luckyhan.rubychina.api.basic.UpLoadProgressInterceptor;
import com.luckyhan.rubychina.upload.ImageUploadTask;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public abstract class BaseRequest {

    private static final String API_URL = "https://www.ruby-china.org/api/v3/";
    protected Retrofit.Builder mRetrofitBuilder;

    public BaseRequest() {
        mRetrofitBuilder = new Retrofit.Builder().baseUrl(API_URL);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.interceptors().add(interceptor);
            mRetrofitBuilder.client(httpClient);
        }
    }

    public BaseRequest(ImageUploadTask.ImageUploadCallback listener) {
        mRetrofitBuilder = new Retrofit.Builder().baseUrl(API_URL);
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.networkInterceptors().add(new UpLoadProgressInterceptor(listener));
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(interceptor);
        }
        mRetrofitBuilder.client(httpClient);
    }

    protected Retrofit getBasicRetrofit() {
        return mRetrofitBuilder.build();
    }

    protected Retrofit getRetrofitForSimpleJson() {
        return mRetrofitBuilder
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    protected Retrofit getRetrofitForSimpleJsonUploading() {
        return mRetrofitBuilder
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
