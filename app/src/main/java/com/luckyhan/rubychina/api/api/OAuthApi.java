package com.luckyhan.rubychina.api.api;

import com.luckyhan.rubychina.model.Token;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

public interface OAuthApi {

    @FormUrlEncoded
    @POST("token")
    Call<Token> getToken(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grant_type,
            @Field("code") String code,
            @Field("redirect_uri") String redirect_uri,
            @Field("client_id") String client_id
    );

    @FormUrlEncoded
    @POST("token")
    Call<Token> refreshToken(
            @Header("Authorization") String authorization,
            @Field("grant_type") String grant_type,
            @Field("refresh_token") String refresh_token,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("redirect_uri") String redirect_uri
    );

}
