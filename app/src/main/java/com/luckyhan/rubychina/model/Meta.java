package com.luckyhan.rubychina.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Meta implements Parcelable {

    public boolean followed;
    public boolean liked;
    public boolean favorited;
    public boolean blocked;
    public String time;
    public String[] user_liked_reply_ids;
    public int total;

    @Override
    public String toString() {
        return "Meta{" +
                "followed=" + followed +
                ", liked=" + liked +
                ", favorited=" + favorited +
                ", blocked=" + blocked +
                ", time='" + time + '\'' +
                ", user_liked_reply_ids=" + Arrays.toString(user_liked_reply_ids) +
                ", total=" + total +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(followed ? (byte) 1 : (byte) 0);
        dest.writeByte(liked ? (byte) 1 : (byte) 0);
        dest.writeByte(favorited ? (byte) 1 : (byte) 0);
        dest.writeByte(blocked ? (byte) 1 : (byte) 0);
        dest.writeString(this.time);
        dest.writeStringArray(this.user_liked_reply_ids);
        dest.writeInt(this.total);
    }

    public Meta() {
    }

    protected Meta(Parcel in) {
        this.followed = in.readByte() != 0;
        this.liked = in.readByte() != 0;
        this.favorited = in.readByte() != 0;
        this.blocked = in.readByte() != 0;
        this.time = in.readString();
        this.user_liked_reply_ids = in.createStringArray();
        this.total = in.readInt();
    }

    public static final Creator<Meta> CREATOR = new Creator<Meta>() {
        public Meta createFromParcel(Parcel source) {
            return new Meta(source);
        }

        public Meta[] newArray(int size) {
            return new Meta[size];
        }
    };

}
