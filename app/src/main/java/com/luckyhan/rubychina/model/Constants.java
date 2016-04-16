package com.luckyhan.rubychina.model;

public interface Constants {

    String KEY_USER = "user";
    String KEY_TOKEN = "token";
    String KEY_CACHE_NODES = "nodes";
    String KEY_CACHE_SUBSCRIBE = "sub";

    interface Pref {
        String BROWSER = "key_browser";
        String SORTING = "key_sorting";
        String AUTO_UPDATE = "auto_update";
    }

    interface Intent {
        String LOGIN_SUCCESS = "rubychina.intent.LOGIN_SUCCESS";
    }

}
