package com.luckyhan.rubychina.upload;

import com.luckyhan.rubychina.RubyChinaApp;
import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.TopicRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.Post;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.model.response.TopicResponse;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.squareup.tape.Task;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TopicUploadTask implements Task<TopicUploadTask.TopicUploadCallback> {

    private Post mPost;

    public TopicUploadTask(Post post) {
        mPost = post;
    }

    @Override
    public void execute(final TopicUploadCallback callback) {
        StringBuilder sb = new StringBuilder(mPost.content);
        sb.append("\n");
        if (mPost.remoteImageList != null) {
            for (String s : mPost.remoteImageList) {
                sb.append("![](");
                sb.append(s);
                sb.append(")\n");
            }
        }
        new TopicRequest().postTopic(mPost.title, sb.toString(), mPost.node.id, DataUtils.getToken(), new Callback<TopicResponse>() {

            @Override
            public void onResponse(Response<TopicResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    callback.onTopicUploadSuccess(response.body());
                } else {
                    ErrorResponse errorResponse = ErrorUtils.parseError(RubyChinaApp.getContext(), response, retrofit);
                    ToastUtils.showLong(RubyChinaApp.getContext(), errorResponse.toString());
                    callback.onTopicUploadFailure();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onTopicUploadFailure();
            }
        });
    }

    public interface TopicUploadCallback {
        void onTopicUploadSuccess(TopicResponse body);

        void onTopicUploadFailure();
    }

}
