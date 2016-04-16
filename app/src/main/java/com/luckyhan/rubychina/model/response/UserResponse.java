package com.luckyhan.rubychina.model.response;

import com.luckyhan.rubychina.model.Meta;
import com.luckyhan.rubychina.model.User;

public class UserResponse {

    public User user;
    public Meta meta;

    @Override
    public String toString() {
        return "UserModel{" +
                "user=" + user +
                ", meta=" + meta +
                '}';
    }
}
