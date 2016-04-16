package com.luckyhan.rubychina.upload;

import android.content.Context;

import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.TopicRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.model.response.UploadImageResponse;
import com.squareup.tape.Task;

import java.io.File;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ImageUploadTask implements Task<ImageUploadTask.ImageUploadCallback> {

    private Context mContext;
    private File mFile;
    private int mIndex;

    public ImageUploadTask(Context context, int index, String path) {
        mContext = context;
        mIndex = index;
        mFile = new File(path);
    }

    @Override
    public void execute(final ImageUploadCallback callback) {
        callback.onStart(mIndex);
        new TopicRequest(callback).uploadTypedPhoto(mFile, DataUtils.getUserToken().access_token, new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Response<UploadImageResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    callback.onSuccess(response.body().image_url);
                } else {
                    ErrorResponse errorResponse = ErrorUtils.parseError(mContext, response, retrofit);
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public interface ImageUploadCallback {

        void onStart(int index);

        void onRequestProgress(long bytesWritten, long contentLength);

        void onSuccess(String imageUrl);

        void onFailure();

    }

}
