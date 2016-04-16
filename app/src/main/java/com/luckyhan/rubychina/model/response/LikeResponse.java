package com.luckyhan.rubychina.model.response;

public class LikeResponse {

    public String obj_type;
    public String obj_id;
    public int count;

    @Override
    public String toString() {
        return "ReplyLikeModel{" +
                "obj_type='" + obj_type + '\'' +
                ", obj_id='" + obj_id + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

}
