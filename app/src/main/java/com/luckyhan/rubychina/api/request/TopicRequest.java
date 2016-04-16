package com.luckyhan.rubychina.api.request;

import com.luckyhan.rubychina.api.api.TopicApi;
import com.luckyhan.rubychina.model.Status;
import com.luckyhan.rubychina.model.response.LikeResponse;
import com.luckyhan.rubychina.model.response.ReplyResponse;
import com.luckyhan.rubychina.model.response.TopicListResponse;
import com.luckyhan.rubychina.model.response.TopicResponse;
import com.luckyhan.rubychina.model.response.UploadImageResponse;
import com.luckyhan.rubychina.upload.ImageUploadTask;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;

public class TopicRequest extends BaseRequest {

    private TopicApi topicApi;

    public TopicRequest() {
        super();
    }

    public TopicRequest(ImageUploadTask.ImageUploadCallback listener) {
        super(listener);
    }

    public void getTopics(String sorting, String nodeId, int offset, int limit, Callback<TopicListResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.getTopicList(sorting, nodeId, offset, limit).enqueue(callback);
    }

    /**
     * Get topic full content
     *
     * @param topicId  The id of topic
     * @param callback The callback
     */
    public void getTopicDetail(String topicId, String access_token, Callback<TopicResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.getTopicDetail(topicId, access_token).enqueue(callback);
    }

    /**
     * Get replies list of topic
     *
     * @param topicId  The id of topic
     * @param callback The callback
     */
    public void getTopicReplies(String topicId, int offset, int limit, String access_token, Callback<ReplyResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.getReplies(topicId, offset, limit, access_token).enqueue(callback);
    }

    /**
     * Login user add favorite topic
     *
     * @param topicId      The id of topic
     * @param access_token The access token
     * @param callback     The Callback
     */
    public void addTopicFavorite(String topicId, String access_token, Callback<Status> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.addFavorite(topicId, access_token).enqueue(callback);
    }

    /**
     * Login user remove favorite topic
     *
     * @param topicId      The id of topic
     * @param access_token The access token
     * @param callback     The Callback
     */
    public void unFavorite(String topicId, String access_token, Callback<Status> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.unFavorite(topicId, access_token).enqueue(callback);
    }

    public void likeTopic(String topicId, String access_token, Callback<LikeResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.like("topic", topicId, access_token).enqueue(callback);
    }

    public void likeReply(String replyId, String access_token, Callback<LikeResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.like("reply", replyId, access_token).enqueue(callback);
    }

    public void unlikeTopic(String topicId, String access_token, Callback<LikeResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.unlike("topic", topicId, access_token).enqueue(callback);
    }

    public void unlikeReply(String replyId, String access_token, Callback<LikeResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.unlike("reply", replyId, access_token).enqueue(callback);
    }

    /**
     * Add reply for topic
     *
     * @param topicId      The id of topic
     * @param body         The reply content
     * @param access_token The access token
     * @param callback     The Callback
     */
    public void addReply(String topicId, String body, String access_token, Callback<ResponseBody> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.addReply(topicId, body, access_token).enqueue(callback);
    }

    /**
     * Post new topic
     *
     * @param title        The title of new topic
     * @param body         The content of new topic
     * @param nodeId       The nodeId of new topic
     * @param access_token The User access token
     * @param callback     The Callback
     */
    public void postTopic(String title, String body, String nodeId, String access_token,
                          Callback<TopicResponse> callback) {
        topicApi = getRetrofitForSimpleJson().create(TopicApi.class);
        topicApi.postTopic(title, body, nodeId, access_token).enqueue(callback);
    }

    public void uploadTypedPhoto(File photo, String access_token, Callback<UploadImageResponse> callback) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), access_token);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), photo);
        String fileName = "file\"; filename=\"" + photo.getName();
        requestBodyMap.put("access_token", tokenBody);
        requestBodyMap.put(fileName, fileBody);

        getRetrofitForSimpleJsonUploading().client().setReadTimeout(20, TimeUnit.SECONDS);
        topicApi = getRetrofitForSimpleJsonUploading().create(TopicApi.class);
        topicApi.uploadTypedPhoto(requestBodyMap).enqueue(callback);
    }

}
