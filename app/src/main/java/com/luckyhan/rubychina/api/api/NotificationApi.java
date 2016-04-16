package com.luckyhan.rubychina.api.api;

import com.luckyhan.rubychina.model.response.ActionResponse;
import com.luckyhan.rubychina.model.response.NotificationResponse;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface NotificationApi {

    @GET("notifications.json")
    Call<NotificationResponse> getNotifications(
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("access_token") String access_token
    );

    @DELETE("notifications/{id}.json")
    Call<ActionResponse> deleteNotification(
            @Path("id") String id,
            @Query("access_token") String access_token
    );

    @DELETE("notifications/all.json")
    Call<ActionResponse> clearNotification(
            @Query("access_token") String access_token
    );

}
