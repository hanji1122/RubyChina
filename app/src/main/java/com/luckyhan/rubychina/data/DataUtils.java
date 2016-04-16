package com.luckyhan.rubychina.data;

import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Token;
import com.luckyhan.rubychina.model.User;
import com.orhanobut.hawk.Hawk;

public class DataUtils {

    public static boolean isUserLogin() {
        Token token = Hawk.get(Constants.KEY_TOKEN);
        User user = Hawk.get(Constants.KEY_USER);
        return token != null && user != null;
    }

    public static boolean isTokenExpires() {
        Token token = getUserToken();
        if (token != null) {
            int expireTime = token.created_at + token.expires_in;
            if (System.currentTimeMillis() / 1000 >= expireTime) {
                return true;
            }
        }
        return false;
    }

    public static User getLoginUser() {
        return Hawk.get(Constants.KEY_USER);
    }

    public static Token getUserToken() {
        return Hawk.get(Constants.KEY_TOKEN);
    }

    public static String getToken() {
        if (isUserLogin()) {
            return getUserToken().access_token;
        }
        return "";
    }

}
