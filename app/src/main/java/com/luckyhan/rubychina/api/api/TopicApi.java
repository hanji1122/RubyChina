package com.luckyhan.rubychina.api.api;

import com.luckyhan.rubychina.model.Status;
import com.luckyhan.rubychina.model.response.LikeResponse;
import com.luckyhan.rubychina.model.response.ReplyResponse;
import com.luckyhan.rubychina.model.response.TopicListResponse;
import com.luckyhan.rubychina.model.response.TopicResponse;
import com.luckyhan.rubychina.model.response.UploadImageResponse;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.util.Map;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;

public interface TopicApi {

    @GET("topics")
    Call<TopicListResponse> getTopicList(
            @Query("type") String sorting,
            @Query("node_id") String nodeId,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET("topics/{id}.json")
    Call<TopicResponse> getTopicDetail(
            @Path("id") String topicId,
            @Query("access_token") String access_token
    );

    @GET("topics/{id}/replies.json")
    Call<ReplyResponse> getReplies(
            @Path("id") String topicId,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("topics/{topicId}/favorite.json")
    Call<Status> addFavorite(
            @Path("topicId") String topicId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("topics/{topicId}/unfavorite.json")
    Call<Status> unFavorite(
            @Path("topicId") String topicId,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("topics/{topicId}/replies.json")
    Call<ResponseBody> addReply(
            @Path("topicId") String topicId,
            @Field("body") String body,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("likes.json")
    Call<LikeResponse> like(
            @Field("obj_type") String type,
            @Field("obj_id") String id,
            @Field("access_token") String access_token
    );

    @DELETE("likes.json")
    Call<LikeResponse> unlike(
            @Query("obj_type") String type,
            @Query("obj_id") String id,
            @Query("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("topics")
    Call<TopicResponse> postTopic(
            @Field("title") String title,
            @Field("body") String body,
            @Field("node_id") String nodeId,
            @Field("access_token") String access_token
    );

    @Multipart
    @POST("photos.json")
    Call<UploadImageResponse> uploadTypedPhoto(
            @PartMap Map<String, RequestBody> params
    );

}
