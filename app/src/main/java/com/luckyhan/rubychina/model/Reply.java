package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.luckyhan.rubychina.utils.TimeUtils;

public class Reply implements Parcelable {

    public String id;
    public String body_html;
    public String created_at;
    public String updated_at;
    public boolean deleted;
    public String topic_id;
    public User user;
    public int likes_count;
    public Ability ability;
    public String topic_title;

    public String getPostTime() {
        return TimeUtils.getPrettyTime(created_at);
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
        dest.writeParcelable(this.user, 0);
        dest.writeInt(this.likes_count);
        dest.writeParcelable(this.ability, 0);
        dest.writeString(this.topic_title);
    }

    public Reply() {
    }

    protected Reply(Parcel in) {
        this.id = in.readString();
        this.body_html = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted = in.readByte() != 0;
        this.topic_id = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.likes_count = in.readInt();
        this.ability = in.readParcelable(Ability.class.getClassLoader());
        this.topic_title = in.readString();
    }

    public static final Parcelable.Creator<Reply> CREATOR = new Parcelable.Creator<Reply>() {
        public Reply createFromParcel(Parcel source) {
            return new Reply(source);
        }

        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };

    @Override
    public String toString() {
        return "Reply{" +
                "id='" + id + '\'' +
                ", body_html='" + body_html + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", deleted=" + deleted +
                ", topic_id='" + topic_id + '\'' +
                ", user=" + user +
                ", likes_count=" + likes_count +
                ", ability=" + ability +
                ", topic_title='" + topic_title + '\'' +
                '}';
    }

}
