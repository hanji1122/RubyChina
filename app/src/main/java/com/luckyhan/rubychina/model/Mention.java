package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mention implements Parcelable {
    // mention_type = 'Reply'
    public String id;
    public String body_html;
    public String created_at;
    public String updated_at;
    public boolean deleted;
    public String topic_id;
    public int likes_count;

    // mention_type = 'Topic'
    public String title;
    public String node_name;
    public String node_id;
    public String replies_count;
    public String last_reply_user_id;
    public String last_reply_user_login;
    public boolean excellent;

    public Mention() {
    }

    @Override
    public String toString() {
        return "Mention{" +
                "id=" + id +
                ", body_html='" + body_html + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", deleted=" + deleted +
                ", topic_id=" + topic_id +
                ", likes_count=" + likes_count +
                ", title='" + title + '\'' +
                ", node_name='" + node_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.body_html);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeByte(deleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.topic_id);
        dest.writeInt(this.likes_count);
        dest.writeString(this.title);
        dest.writeString(this.node_name);
        dest.writeString(this.node_id);
        dest.writeString(this.replies_count);
        dest.writeString(this.last_reply_user_id);
        dest.writeString(this.last_reply_user_login);
        dest.writeByte(excellent ? (byte) 1 : (byte) 0);
    }

    protected Mention(Parcel in) {
        this.id = in.readString();
        this.body_html = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted = in.readByte() != 0;
        this.topic_id = in.readString();
        this.likes_count = in.readInt();
        this.title = in.readString();
        this.node_name = in.readString();
        this.node_id = in.readString();
        this.replies_count = in.readString();
        this.last_reply_user_id = in.readString();
        this.last_reply_user_login = in.readString();
        this.excellent = in.readByte() != 0;
    }

    public static final Creator<Mention> CREATOR = new Creator<Mention>() {
        public Mention createFromParcel(Parcel source) {
            return new Mention(source);
        }

        public Mention[] newArray(int size) {
            return new Mention[size];
        }
    };
}
