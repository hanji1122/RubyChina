package com.luckyhan.rubychina.api.request;

import android.util.Base64;

import com.luckyhan.rubychina.api.api.OAuthApi;
import com.luckyhan.rubychina.model.Token;
import com.squareup.okhttp.HttpUrl;

import retrofit.Callback;
import retrofit.Retrofit;

public class OAuthRequest extends BaseRequest {

    private static final String OAUTH_URL = "https://ruby-china.org/oauth/";
    private static final String CLIENT_ID = "32d49d11";
    private static final String CLIENT_SECRET = "4df5092f6e480a44652642e40b339a197bc2c0090fb924b0f58f55d211899a77";
    private static final String REDIRECT_URI = "https://ruby-china.org";
    private static final String RESPONSE_TYPE = "code";
    private static final String BASIC_AUTH = "Basic " + new String(Base64.encode((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(), Base64.NO_WRAP));

    private OAuthApi oAuthApi;

    public OAuthRequest() {
        mRetrofitBuilder = new Retrofit.Builder().baseUrl(OAUTH_URL);
    }

    public void getToken(String code, Callback<Token> callBack) {
        String grant_type = "authorization_code";
        oAuthApi = getRetrofitForSimpleJson().create(OAuthApi.class);
        oAuthApi.getToken(BASIC_AUTH, grant_type, code, REDIRECT_URI, CLIENT_ID).enqueue(callBack);
    }

    public void refreshToken(String refresh_token, Callback<Token> callback) {
        String grant_type = "refresh_token";
        oAuthApi = getRetrofitForSimpleJson().create(OAuthApi.class);
        oAuthApi.refreshToken(BASIC_AUTH, grant_type, refresh_token, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI).enqueue(callback);
    }

    public static String getAuthorizeUrl() {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host("www.ruby-china.org")
                .addPathSegment("oauth")
                .addPathSegment("authorize")
                .addQueryParameter("client_id", CLIENT_ID)
                .addQueryParameter("redirect_uri", REDIRECT_URI)
                .addQueryParameter("response_type", RESPONSE_TYPE)
                .build();
        return httpUrl.toString();
    }

}
