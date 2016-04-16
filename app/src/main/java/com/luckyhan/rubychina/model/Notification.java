package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {

    public String id;
    public String type;             // TopicReply: 回复帖子, Topic: 创建帖子, Mention: 提及, Follow: 关注了你
    public boolean read;
    public User actor;
    public String mention_type;    // Topic
    public Mention mention;
    public Topic topic;
    public Reply reply;
    public Node node;
    public String created_at;
    public String updated_at;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeByte(read ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.actor, 0);
        dest.writeString(this.mention_type);
        dest.writeParcelable(this.mention, flags);
        dest.writeParcelable(this.topic, 0);
        dest.writeParcelable(this.reply, 0);
        dest.writeParcelable(this.node, 0);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    public Notification() {
    }

    protected Notification(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.read = in.readByte() != 0;
        this.actor = in.readParcelable(User.class.getClassLoader());
        this.mention_type = in.readString();
        this.mention = in.readParcelable(Mention.class.getClassLoader());
        this.topic = in.readParcelable(Topic.class.getClassLoader());
        this.reply = in.readParcelable(Reply.class.getClassLoader());
        this.node = in.readParcelable(Node.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", read=" + read +
                ", actor=" + actor +
                ", mention_type='" + mention_type + '\'' +
                ", mention=" + mention +
                ", topic=" + topic +
                ", reply=" + reply +
                ", node=" + node +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
