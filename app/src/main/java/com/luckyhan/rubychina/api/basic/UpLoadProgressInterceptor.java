package com.luckyhan.rubychina.api.basic;

import com.luckyhan.rubychina.upload.ImageUploadTask;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class UpLoadProgressInterceptor implements Interceptor {

    private ImageUploadTask.ImageUploadCallback progressListener;

    public UpLoadProgressInterceptor(ImageUploadTask.ImageUploadCallback progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (originalRequest.body() == null) {
            return chain.proceed(originalRequest);
        }

        Request progressRequest = originalRequest.newBuilder()
                .method(originalRequest.method(),
                        new CountingRequestBody(originalRequest.body(), progressListener))
                .build();

        return chain.proceed(progressRequest);
    }
}