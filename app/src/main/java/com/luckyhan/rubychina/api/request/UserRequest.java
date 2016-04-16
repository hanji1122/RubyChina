package com.luckyhan.rubychina.api.request;

import com.luckyhan.rubychina.api.api.UserApi;
import com.luckyhan.rubychina.model.response.ActionResponse;
import com.luckyhan.rubychina.model.response.ActiveUsersResponse;
import com.luckyhan.rubychina.model.response.FollowersArrayResponse;
import com.luckyhan.rubychina.model.response.FollowingArrayResponse;
import com.luckyhan.rubychina.model.response.TopicArrayResponse;
import com.luckyhan.rubychina.model.response.UserResponse;
import com.squareup.okhttp.ResponseBody;

import retrofit.Callback;

public class UserRequest extends BaseRequest {

    private UserApi userApi;

    /**
     * Get user info of current oauth login user
     *
     * @param access_token The access token
     * @param callback     The Callback
     */
    @Deprecated
    public void getUserInfo(String access_token, Callback<ResponseBody> callback) {
        userApi = getBasicRetrofit().create(UserApi.class);
        userApi.getUserInfo(access_token).enqueue(callback);
    }

    /**
     * Get user info of current oauth login user
     *
     * @param access_token The access token
     * @param callback     The Callback
     */
    public void getLoginUser(String access_token, Callback<UserResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.getLoginUser(access_token).enqueue(callback);
    }

    /**
     * Get user detail info
     *
     * @param login    The login name of any user
     * @param callback The Callback
     */
    public void getUserDetailInfo(String login, String access_token, Callback<UserResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.getUserDetailInfo(login, access_token).enqueue(callback);
    }

    /**
     * Get active user list
     *
     * @param limit    The limit of return list
     * @param callback The Callback
     */
    public void getActiveUserList(int limit, Callback<ActiveUsersResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.getActiveUserList(limit).enqueue(callback);
    }

    /**
     * Get user posted topic list
     *
     * @param login    The login name
     * @param offset   The offset of request
     * @param limit    The limit of request
     * @param callback The Callback
     */
    public void getPostedTopics(String login, int offset, int limit, Callback<TopicArrayResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.getPosted(login, offset, limit).enqueue(callback);
    }

    /**
     * Get user favorites topic list
     *
     * @param login    The login name
     * @param offset   The offset of request
     * @param limit    The limit of request
     * @param callback The Callback
     */
    public void getFavorites(String login, int offset, int limit, Callback<TopicArrayResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.getFavorites(login, offset, limit).enqueue(callback);
    }

    /**
     * Get user following list
     *
     * @param login    The login name
     * @param offset   The offset of request
     * @param limit    The limit of request
     * @param callback The Callback
     */
    public void getFollowing(String login, int offset, int limit, Callback<FollowingArrayResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.getFollowing(login, offset, limit).enqueue(callback);
    }

    /**
     * Get user follower list
     *
     * @param login    The login name
     * @param offset   The offset of request
     * @param limit    The limit of request
     * @param callback The Callback
     */
    public void getFollowers(String login, int offset, int limit, Callback<FollowersArrayResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.getFollowers(login, offset, limit).enqueue(callback);
    }

    /**
     * Follow someone
     *
     * @param login        The login name who you want to follow
     * @param access_token The access token
     * @param callback     The Callback
     */
    public void follow(String login, String access_token, Callback<ActionResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.follow(login, access_token).enqueue(callback);
    }

    /**
     * UnFollow someone
     *
     * @param login        The login name who you want to unFollow
     * @param access_token The access token
     * @param callback     The Callback
     */
    public void unFollow(String login, String access_token, Callback<ActionResponse> callback) {
        userApi = getRetrofitForSimpleJson().create(UserApi.class);
        userApi.unFollow(login, access_token).enqueue(callback);
    }

}
