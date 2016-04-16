package com.luckyhan.rubychina.model.response;

import com.luckyhan.rubychina.model.Meta;
import com.luckyhan.rubychina.model.Reply;

import java.util.List;

public class ReplyResponse {

    public List<Reply> replies;
    public Meta meta;

    @Override
    public String toString() {
        return "ReplyModel{" +
                "replies=" + replies +
                ", meta=" + meta +
                '}';
    }

}
