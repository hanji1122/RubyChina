package com.luckyhan.rubychina.api.request;

import com.luckyhan.rubychina.api.api.NotificationApi;
import com.luckyhan.rubychina.model.response.ActionResponse;
import com.luckyhan.rubychina.model.response.NotificationResponse;

import retrofit.Callback;

public class NotificationRequest extends BaseRequest {

    private NotificationApi mApi;

    public void getNotifications(int offset, int limit, String access_token, Callback<NotificationResponse> callback) {
        mApi = getRetrofitForSimpleJson().create(NotificationApi.class);
        mApi.getNotifications(offset, limit, access_token).enqueue(callback);
    }

    public void deleteNotification(String id, String access_token, Callback<ActionResponse> callback) {
        mApi = getRetrofitForSimpleJson().create(NotificationApi.class);
        mApi.deleteNotification(id, access_token).enqueue(callback);
    }

    public void clearNotification(String access_token, Callback<ActionResponse> callback) {
        mApi = getRetrofitForSimpleJson().create(NotificationApi.class);
        mApi.clearNotification(access_token).enqueue(callback);
    }

}
