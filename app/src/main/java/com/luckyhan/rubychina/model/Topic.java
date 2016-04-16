package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.luckyhan.rubychina.utils.TimeUtils;

public class Topic implements Parcelable {

    public String id;
    public String title;
    public String created_at;
    public String updated_at;
    public String replied_at;
    public String replies_count;
    public String node_name;
    public String node_id;
    public String last_reply_user_id;
    public String last_reply_user_login;
    public User user;
    public boolean deleted;
    public boolean excellent;
    public Ability ability;
    public String body;
    public String body_html;
    public int hits;
    public int likes_count;
    public String suggested_at;

    public String getCreatePrettyTime() {
        return TimeUtils.getPrettyTime(created_at);
    }

    public String getUpdatePrettyTime() {
        return TimeUtils.getPrettyTime(updated_at);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", replied_at='" + replied_at + '\'' +
                ", replies_count='" + replies_count + '\'' +
                ", node_name='" + node_name + '\'' +
                ", node_id='" + node_id + '\'' +
                ", last_reply_user_id='" + last_reply_user_id + '\'' +
                ", last_reply_user_login='" + last_reply_user_login + '\'' +
                ", user=" + user +
                ", deleted=" + deleted +
                ", excellent=" + excellent +
                ", ability=" + ability +
                ", body='" + body + '\'' +
                ", body_html='" + body_html + '\'' +
                ", hits=" + hits +
                ", likes_count=" + likes_count +
                ", suggested_at='" + suggested_at + '\'' +
                '}';
    }

    public Topic() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.replied_at);
        dest.writeString(this.replies_count);
        dest.writeString(this.node_name);
        dest.writeString(this.node_id);
        dest.writeString(this.last_reply_user_id);
        dest.writeString(this.last_reply_user_login);
        dest.writeParcelable(this.user, 0);
        dest.writeByte(deleted ? (byte) 1 : (byte) 0);
        dest.writeByte(excellent ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.ability, 0);
        dest.writeString(this.body);
        dest.writeString(this.body_html);
        dest.writeInt(this.hits);
        dest.writeInt(this.likes_count);
        dest.writeString(this.suggested_at);
    }

    protected Topic(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.replied_at = in.readString();
        this.replies_count = in.readString();
        this.node_name = in.readString();
        this.node_id = in.readString();
        this.last_reply_user_id = in.readString();
        this.last_reply_user_login = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.deleted = in.readByte() != 0;
        this.excellent = in.readByte() != 0;
        this.ability = in.readParcelable(Ability.class.getClassLoader());
        this.body = in.readString();
        this.body_html = in.readString();
        this.hits = in.readInt();
        this.likes_count = in.readInt();
        this.suggested_at = in.readString();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}
