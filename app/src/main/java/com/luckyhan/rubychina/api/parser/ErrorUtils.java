package com.luckyhan.rubychina.api.parser;

import android.content.Context;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.OAuthRequest;
import com.luckyhan.rubychina.model.Token;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.orhanobut.hawk.Hawk;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

public class ErrorUtils {

    public static ErrorResponse parseError(Context context, Response<?> response, Retrofit retrofit) {
        Converter<ResponseBody, ErrorResponse> converter = retrofit.responseConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorResponse();
        }
        if (response.code() == 401) {
            requestRefreshToken(context.getApplicationContext());
        }
        return error;
    }

    private static void requestRefreshToken(final Context context) {
        Token token = Hawk.get("token");
        new OAuthRequest().refreshToken(token.refresh_token, new Callback<Token>() {

            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    return;
                }
                ToastUtils.showShort(context, R.string.msg_update_token_success);
                Token token = response.body();
                Hawk.put("token", token);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
