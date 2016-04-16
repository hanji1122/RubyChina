package com.luckyhan.rubychina.api.api;

import com.luckyhan.rubychina.model.response.ActionResponse;
import com.luckyhan.rubychina.model.response.ActiveUsersResponse;
import com.luckyhan.rubychina.model.response.FollowersArrayResponse;
import com.luckyhan.rubychina.model.response.FollowingArrayResponse;
import com.luckyhan.rubychina.model.response.TopicArrayResponse;
import com.luckyhan.rubychina.model.response.UserResponse;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface UserApi {

    @Deprecated
    @GET("hello")
    Call<ResponseBody> getUserInfo(
            @Query("access_token") String access_token
    );

    @GET("hello")
    Call<UserResponse> getLoginUser(
            @Query("access_token") String access_token
    );


    @GET("users")
    Call<ActiveUsersResponse> getActiveUserList(
            @Query("limit") int limit
    );

    @GET("users/{login}")
    Call<UserResponse> getUserDetailInfo(
            @Path("login") String login,
            @Query("access_token") String access_token
    );

    @GET("users/{login}/topics.json")
    Call<TopicArrayResponse> getPosted(
            @Path("login") String login,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET("users/{login}/favorites.json")
    Call<TopicArrayResponse> getFavorites(
            @Path("login") String login,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET("users/{login}/following.json")
    Call<FollowingArrayResponse> getFollowing(
            @Path("login") String login,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET("users/{login}/followers.json")
    Call<FollowersArrayResponse> getFollowers(
            @Path("login") String login,
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @FormUrlEncoded
    @POST("users/{login}/follow.json")
    Call<ActionResponse> follow(
            @Path("login") String login,
            @Field("access_token") String access_token
    );

    @FormUrlEncoded
    @POST("users/{login}/unfollow.json")
    Call<ActionResponse> unFollow(
            @Path("login") String login,
            @Field("access_token") String access_token
    );

}
